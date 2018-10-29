package org.socialcars.sinziana.simulation.optimiser;

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
import java.util.stream.IntStream;


/**
 * class for the platooning optimisation problem
 */
public class CPSPP implements IPSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<Integer, GRBVar[][]> m_xs;
    private final Integer m_source;
    private final ArrayList<Integer> m_destinations;
    private final CJungEnvironment m_graph;

    private final HashMap<Integer, ArrayList<IEdge>> m_indivres;
    private final HashMap<IEdge, Integer> m_results;

    /**
     * ctor
     * @param p_env jung graph
     * @param p_source source
     * @param p_destinations destinations
     * @throws GRBException gurobi
     */
    public CPSPP( final CJungEnvironment p_env, final Integer p_source, final ArrayList<Integer> p_destinations ) throws GRBException
    {
        m_env = new GRBEnv( "pspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_destinations.forEach( d -> m_xs.put( d, new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );
        m_source = p_source;
        m_destinations = p_destinations;
        m_graph = p_env;
        m_results = new HashMap<>();
        m_indivres = new HashMap<>();
        p_destinations.forEach( d -> m_indivres.put( d, new ArrayList<>() ) );

        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            try
            {
                if ( e.weight().doubleValue() == 0.0 ) m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, e.weight().doubleValue() + 0.000001,
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );
                else m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, e.weight().doubleValue(),
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );

                for ( final Integer l_ds : p_destinations )
                {
                    final GRBVar[][] l_temp = m_xs.get( l_ds );
                    l_temp[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + "_" + l_ds + ":" + l_start + "-" + l_end );
                }
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }
        } );
    }

    /**
     * function to solve the optimisation problem
     * @throws GRBException gurobi
     */
    public void solve() throws GRBException
    {
        addConstraints();
        m_model.optimize();
        saveResults();
        System.out.println( "Objective function solution: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
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
                    if  ( i.equals( m_source ) ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" + String.valueOf( d ) );
                    else if ( i.equals( d ) ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" + String.valueOf( d ) );
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
                final GRBVar[][] l_temp = m_xs.get( d );
                final CSPP l_indiv = new CSPP( m_graph, m_source, d );
                l_indiv.solve();
                double l_ml = Double.valueOf( l_indiv.length() );
                l_ml = l_ml + l_ml * 0.25;

                final GRBLinExpr l_dist = new GRBLinExpr();
                IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( i ->
                    IntStream.range( 0, m_graph.size() + 1 ).boxed().forEach( j ->
                    {
                        if ( l_temp[i][j] != null )
                            l_dist.addTerm( 1.0, l_temp[i][j] );
                    } ) );
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
                final GRBVar[][] l_temp = m_xs.get( d );
                l_expr.addTerm( 1.0, l_temp[l_start][l_end] );
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

    private void saveResults()
    {
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            m_destinations.forEach( d ->
            {
                try
                {
                    final GRBVar[][] l_temp = m_xs.get( d );
                    final ArrayList<IEdge> l_res = m_indivres.get( d );
                    if ( ( l_temp[l_start][l_end] != null ) && ( l_temp[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                    {
                        m_results.put( e, m_results.getOrDefault( e, 0 ) + 1 );
                        l_res.add( e );
                    }
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );


    }

    /**
     * displays the result of the optimisation problem
     * @throws GRBException gurobi
     */
    public void display() throws GRBException
    {
        m_indivres.keySet().forEach( d ->
        {
            m_indivres.get( d ).forEach( x -> System.out.println( "x" + d + ":" + x.id() ) );
        } );

        m_results.keySet().forEach( y -> System.out.println( "y:" + y.id() ) );

        System.out.print( "Destinations are: " );
        m_destinations.forEach( d -> System.out.print( d + " " ) );
        System.out.println();
    }

    public HashMap<IEdge, Integer> returnResults()
    {
        return m_results;
    }

    private void cleanUp() throws GRBException
    {
        m_model.dispose();
        m_env.dispose();
    }
}
