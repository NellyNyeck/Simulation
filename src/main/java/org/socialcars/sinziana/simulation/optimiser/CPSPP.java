package org.socialcars.sinziana.simulation.optimiser;

import com.vividsolutions.jts.io.InStream;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.IntStream;


public class CPSPP implements IPSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<Integer, GRBVar[][]> m_xs;
    private final Integer m_source;
    private final ArrayList<Integer> m_destinations;
    private final CJungEnvironment m_graph;
    private final HashMap<IEdge, Integer> m_results;

    public CPSPP( final CJungEnvironment p_env, final Integer p_source, final ArrayList<Integer> p_destinations ) throws GRBException
    {

        //@TODO : 25.10.18 redo structure in a way so that is accesible after cleanup
        m_env = new GRBEnv( "pspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_destinations.forEach( d -> m_xs.put( d, new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );
        m_source = p_source;
        m_destinations = p_destinations;
        m_graph = p_env;
        m_results = new HashMap<>();

        final GRBLinExpr l_obj = new GRBLinExpr();
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            try
            {
                m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );
                l_obj.addTerm( e.weight().doubleValue(), m_ys[l_start][l_end] );

                for (Integer d : p_destinations)
                {
                    GRBVar[][] l_temp = m_xs.get( d );
                    l_temp[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + "_" + d + ":" + l_start + "-" + l_end );
                }
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    public void solve() throws GRBException
    {
        addConstraints();
        m_model.optimize();
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf(e.from().id());
            final Integer l_end = Integer.valueOf(e.to().id());

            m_destinations.forEach( d ->
            {
                try
                {
                    final GRBVar[][] l_temp = m_xs.get(d);
                    if( ( l_temp[l_start][l_end] != null ) && ( l_temp[l_start][l_end].get( GRB.DoubleAttr.X) == 1 ) ) m_results.put( e, m_results.getOrDefault( e, 0 ) + 1 );
                }
                catch (GRBException e1)
                {
                    e1.printStackTrace();
                }
            });
        } );
        display();
        cleanUp();
    }

    private void addConstraints()
    {
        //flow constraint
        m_destinations.forEach( d ->
        {
            GRBVar[][] l_temp = m_xs.get( d );
            IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
                {
                    if( l_temp[i][j] != null ) l_expr.addTerm( 1.0, l_temp[i][j] );
                    if ( l_temp[j][i] != null ) l_expr.addTerm( -1.0, l_temp[j][i] );
                } );
                try
                {
                    if  ( i.equals(m_source) ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" + String.valueOf( d ) );
                    else if (i.equals(d)) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" + String.valueOf( d )  );
                    else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "Flow" + String.valueOf( d ) );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );

        //length constraint
        m_destinations.forEach( d ->
        {
            try
            {
                GRBVar[][] l_temp = m_xs.get( d );
                final CSPP l_indiv = new CSPP( m_graph );
                l_indiv.solve( m_source, d, m_graph );
                double l_ml = Double.valueOf( l_indiv.length() );
                l_ml = l_ml + l_ml * 0.25;

                final GRBLinExpr l_dist = new GRBLinExpr();
                IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
                    IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
                    {
                        if ( l_temp[i][j] != null )
                            l_dist.addTerm( 1.0, l_temp[i][j] );
                    } ));
                m_model.addConstr( l_dist, GRB.LESS_EQUAL, l_ml, "maxdist" + String.valueOf( d ) );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );

        //x<=y
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            m_destinations.forEach( d ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                GRBVar[][] l_temp = m_xs.get( d );
                l_expr.addTerm( 1.0, l_temp[l_start][l_end]);
                l_expr.addTerm( -1.0, m_ys[l_start][l_end] );
                try
                {
                    m_model.addConstr( l_expr, GRB.LESS_EQUAL, 0.0, "x<=y" );
                }
                catch ( final GRBException l_ex )
                {
                    System.out.println( l_ex.getErrorCode() );
                }
            } );
        } );

        //y<y
        /*IntStream.range( 0, m_graph.size() + 1).boxed().forEach( j ->
        {
            IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
            {
               IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( k ->
               {
                   if( ( m_ys[k][j] != null ) && ( m_ys[i][j] != null ) )
                   {
                       try
                       {
                           final GRBLinExpr l_expr1 = new GRBLinExpr();
                           l_expr1.addTerm( 1.0, m_ys[k][j] );
                           l_expr1.addTerm( -1.0, m_ys[i][j] );
                           m_model.addConstr( l_expr1, GRB.LESS_EQUAL, 0, "y<y" );
                           final GRBLinExpr l_expr2 = new GRBLinExpr();
                           l_expr2.addTerm( -1.0, m_ys[k][j] );
                           l_expr2.addTerm( 1.0, m_ys[i][j] );
                           m_model.addConstr( l_expr2, GRB.LESS_EQUAL, 0, "y<=y" );
                       }
                       catch (GRBException e)
                       {
                           e.printStackTrace();
                       }
                   }
               } );
            } ) ;
        } );*/

    }

    public void display() throws GRBException
    {

        final HashSet<GRBVar> l_ys = new HashSet<>();

        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            m_destinations.forEach( d ->
            {
                try
                {
                    if ( (m_ys[l_start][l_end] != null) && ( m_xs.get(d)[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) ) l_ys.add(m_ys[l_start][l_end]);
                }
                catch (GRBException e1)
                {
                    e1.printStackTrace();
                }
            } );
        } );



        m_graph.edges().forEach( e ->
        {
            try
            {
                final Integer l_start = Integer.valueOf( e.from().id() );
                final Integer l_end = Integer.valueOf( e.to().id() );

                for (Integer k : m_xs.keySet())
                {
                    if ( (m_xs.get(k)[l_start][l_end] != null ) && ( m_xs.get(k)[l_start][l_end].get(GRB.DoubleAttr.X) == 1 ) )
                    {
                        System.out.print(m_xs.get(k)[l_start][l_end].get(GRB.StringAttr.VarName) + " " + m_xs.get(k)[l_start][l_end].get( GRB.DoubleAttr.X ) );
                        System.out.println();
                    }
                }
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } );

        System.out.println();

        l_ys.forEach( v ->
        {
            try
            {
                System.out.println( v.get( GRB.StringAttr.VarName ) + " " + v.get( GRB.DoubleAttr.X ) );
            }
            catch (GRBException e)
            {
                e.printStackTrace();
            }
        });

        System.out.print("Destinations are: ");
        m_destinations.forEach( d -> System.out.print( d + " " ) );
        System.out.println();

        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
    }

    public HashMap<IEdge, Integer> returnResults()
    {
        return m_results;
    }

    public void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
