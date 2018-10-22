package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


public class CPSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<Integer, GRBVar[][]> m_xs;

    public CPSPP( final CJungEnvironment p_env, final ArrayList<Integer> p_destinations ) throws GRBException
    {
        m_env = new GRBEnv( "pspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_destinations.forEach( d -> m_xs.put( d, new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );

        /*final ArrayList<Double> l_weights = new ArrayList<>();
        p_env.edges().forEach( e  -> l_weights.add( (Double) e.weight() ) );
        final Double l_maxw = l_weights.stream().mapToDouble( v -> v ).max().orElseThrow( NoSuchElementException::new );*/

        final GRBLinExpr l_obj = new GRBLinExpr();
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            Double l_weight =  ThreadLocalRandom.current().nextDouble(0, 1);
            l_weight = l_weight -0.5;
            try
            {
                m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );
                l_obj.addTerm( e.weight().doubleValue() + ThreadLocalRandom.current().nextDouble( 0, 1 )/100 - 0.005 , m_ys[l_start][l_end] );

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

    public void solve( final CJungEnvironment p_env, final Integer p_origin, final ArrayList<Integer> p_destinations ) throws GRBException
    {
        addConstraints( p_env, p_origin, p_destinations );
        m_model.optimize();
        display( p_env );
        cleanUp();
    }

    private void addConstraints( final CJungEnvironment p_env, final Integer p_origin, final ArrayList<Integer> p_destinations )
    {
        //flow constraint
        p_destinations.forEach( d ->
        {
            GRBVar[][] l_temp = m_xs.get( d );
            IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( i ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( j ->
                {
                    if( l_temp[i][j] != null ) l_expr.addTerm( 1.0, l_temp[i][j] );
                    if ( l_temp[j][i] != null ) l_expr.addTerm( -1.0, l_temp[j][i] );
                } );
                try
                {
                    if  (i.equals(p_origin)) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" + String.valueOf( d ) );
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
        p_destinations.forEach( d ->
        {
            try
            {
                GRBVar[][] l_temp = m_xs.get( d );
                final CSPP l_indiv = new CSPP( p_env );
                l_indiv.solve( p_origin, d, p_env );
                double l_ml = Double.valueOf( l_indiv.length() );
                l_ml = l_ml + l_ml * 0.25;

                final GRBLinExpr l_dist = new GRBLinExpr();
                IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( i ->
                    IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( j ->
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
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            p_destinations.forEach( d ->
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
    }

    private void display( final CJungEnvironment p_env ) throws GRBException
    {
        p_env.edges().forEach( e ->
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
        } );

        p_env.edges().forEach( e ->
        {
            try
            {
                final Integer l_start = Integer.valueOf( e.from().id() );
                final Integer l_end = Integer.valueOf( e.to().id() );
                if( ( m_ys[l_start][l_end] != null ) && ( m_ys[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                {
                    m_xs.keySet().forEach( k -> {
                        try
                        {
                            System.out.print( m_xs.get( k )[l_start][l_end].get( GRB.StringAttr.VarName ) + " " + m_xs.get( k )[l_start][l_end].get( GRB.DoubleAttr.X ) + " ");
                        }
                        catch (GRBException e1)
                        {
                            e1.printStackTrace();
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

        /*IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( i ->
        {
            IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( j ->
            {
                try
                {
                    if ( ( m_ys[i][j] != null ) && ( m_ys[i][j].get( GRB.DoubleAttr.X ) == 1 ) ) System.out.println( m_ys[i][j].get( GRB.StringAttr.VarName ) + " " + m_ys[i][j].get( GRB.DoubleAttr.X ) );
                }
                catch (final GRBException l_e1)
                {
                    l_e1.printStackTrace();
                }
            });
        });*/

        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
    }

    private void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
