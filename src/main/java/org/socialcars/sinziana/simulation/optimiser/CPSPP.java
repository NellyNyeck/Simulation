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
        p_destinations.forEach( d ->
        {
            GRBVar[][] l_temp = new GRBVar[p_env.size() + 1][p_env.size() + 1];
            m_xs.put( d, l_temp );
        } );
        final GRBLinExpr l_obj = new GRBLinExpr();


        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            try
            {
                m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "y" + l_start  + "_" + l_end );
                l_obj.addTerm( (Double) e.weight(), m_ys[l_start][l_end] );
                for (Integer d : p_destinations)
                {
                    m_xs.get( d )[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + "-" + d + l_start + "_" + l_end );
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
        //x<=y
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            p_destinations.forEach( d ->
            {
                try
                {
                    final GRBLinExpr l_expr = new GRBLinExpr();
                    l_expr.addTerm( 1.0, m_xs.get( d )[l_start][l_end]);
                    l_expr.addTerm( -1.0, m_ys[l_start][l_end] );
                    m_model.addConstr( l_expr, GRB.LESS_EQUAL, 0.0, "x<=y" );
                }
                catch ( final GRBException l_ex )
                {
                    System.out.println( l_ex.getErrorCode() );
                }
            } );
        } );

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
                    if  (i.equals( p_origin ) ) m_model.addConstr( l_expr, GRB.EQUAL, 1, "Origin" + String.valueOf( d ) );
                    else if ( i.equals( d ) ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" + String.valueOf( d )  );
                    else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "Flow" + String.valueOf( d ) );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );
    }

    private void display( final CJungEnvironment p_env ) throws GRBException
    {
        IntStream.range( 0, p_env.size() + 1 ).boxed().forEach( i ->
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
        });

        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
    }

    private void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
