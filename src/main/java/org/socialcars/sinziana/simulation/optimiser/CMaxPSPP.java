package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

/**
 * platooning modeled as maximization problem
 */
public class CMaxPSPP implements IPSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<Integer, GRBVar[][]> m_xs;
    private final Integer m_source;
    private final ArrayList<Integer> m_destinations;
    private final CJungEnvironment m_graph;


    /**
     * ctor
     * @param p_env jung graph
     * @param p_source source node
     * @param p_destinations destination nodes
     * @throws GRBException gurobi
     */
    public CMaxPSPP( final CJungEnvironment p_env, final Integer p_source, final ArrayList<Integer> p_destinations ) throws GRBException
    {
        m_env = new GRBEnv( "maxpspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_destinations.forEach( d -> m_xs.put( d, new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );
        m_graph = p_env;
        m_source = p_source;
        m_destinations = p_destinations;

        final GRBLinExpr l_obj = new GRBLinExpr();
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            try
            {
                m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "y" + l_start + "-" + l_end );
                l_obj.addTerm( 1.0, m_ys[l_start][l_end] );
                for ( final Integer l_de : p_destinations )
                {
                    final GRBVar[][] l_temp = m_xs.get( l_de );
                    l_temp[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + "_" + l_de + ":" + l_start + "-" + l_end );
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
        m_model.setObjective( l_obj, GRB.MAXIMIZE );
    }


    @Override
    public void solve() throws GRBException
    {
        addConstraints();
        m_model.optimize();
        display();
        cleanUp();
    }

    private void addConstraints()
    {
        //flow constraint
        m_destinations.forEach( d ->
        {
            final GRBVar[][] l_temp = m_xs.get( d );
            IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
                {
                    if ( l_temp[i][j] != null ) l_expr.addTerm( 1.0, l_temp[i][j] );
                    if ( l_temp[j][i] != null ) l_expr.addTerm( -1.0, l_temp[j][i] );
                } );
                try
                {
                    if ( i.equals( m_source ) ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" + String.valueOf( d ) );
                    else if ( i.equals( d ) ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" + String.valueOf( d )  );
                    else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "Flow" + String.valueOf( d ) );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );

        //overlap constraint
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            final GRBVar[] l_array = new GRBVar[ m_destinations.size() ];
            IntStream.range( 0, m_destinations.size() ).boxed().forEach( i ->
            {
                final GRBVar[][] l_temp = m_xs.get( m_destinations.get( i ) );
                l_array[i] = l_temp[l_start][l_end];
            } );
            try
            {
                m_model.addGenConstrAnd( m_ys[l_start][l_end], l_array, "edge" + String.valueOf( l_start ) + "-" + String.valueOf( l_end ) );
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } );

        //min cost contraint
        m_destinations.forEach( d ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( d );
                final CSPP l_indiv = new CSPP( m_graph, m_source, d );
                l_indiv.solve();
                final double l_mc = l_indiv.cost();
                //l_mc = l_mc + l_mc * 0.5;

                final GRBLinExpr l_costs = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_costs.addTerm( e.weight().doubleValue(), l_temp[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] ) );
                m_model.addConstr( l_costs, GRB.LESS_EQUAL, l_mc, "maxdist" + String.valueOf( d ) );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    @Override
    public void display()
    {
        IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
            IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
            {
                try
                {
                    if ( ( m_ys[i][j] != null ) && ( m_ys[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                        System.out.println( m_ys[i][j].get( GRB.StringAttr.VarName ) + " " + m_ys[i][j].get( GRB.DoubleAttr.X ) );
                }
                catch ( final GRBException l_e1 )
                {
                    l_e1.printStackTrace();
                }
            } ) );

        /*p_env.edges().forEach( e ->
        {
            try
            {
                final Integer l_start = Integer.valueOf( e.from().id() );
                final Integer l_end = Integer.valueOf( e.to().id() );
                if( ( m_ys[l_start][l_end] != null ) && ( m_ys[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                {
                    System.out.println( m_ys[l_start][l_end].get( GRB.StringAttr.VarName ) + " " + m_ys[l_start][l_end].get( GRB.DoubleAttr.X ) );
                }
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } ); */

        m_graph.edges().forEach( e ->
        {
            try
            {
                final Integer l_start = Integer.valueOf( e.from().id() );
                final Integer l_end = Integer.valueOf( e.to().id() );
                if ( ( m_ys[l_start][l_end] != null ) && ( m_ys[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                {
                    m_xs.keySet().forEach( k ->
                    {
                        try
                        {
                            System.out.print( m_xs.get( k )[l_start][l_end].get( GRB.StringAttr.VarName )
                                + " "
                                + m_xs.get( k )[l_start][l_end].get( GRB.DoubleAttr.X ) + " " );
                        }
                        catch ( final GRBException l_err )
                        {
                            l_err.printStackTrace();
                        }
                    } );
                    System.out.println();
                }
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } );
    }

    private void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
