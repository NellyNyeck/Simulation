package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * Shortest path optimiser based on pods with preferences
 */
public class CPodsSPP implements IPSPP
{

    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ys;
    private final HashMap<String, GRBVar[][]> m_xs;
    private final Integer m_source;
    private final CJungEnvironment m_graph;
    private Double m_opt;
    private ArrayList<CPod> m_pods;
    private Integer m_time;
    private HashMap<String, Integer> m_endtimes = new HashMap<>();
    private Double m_speed;
    private HashMap<String, Double> m_lengths = new HashMap<>();

    private final HashMap<CPod, HashSet<IEdge>> m_indivres;
    private final HashMap<IEdge, Integer> m_results;


    /**
     * ctor
     * @param p_env jung environemnt
     * @param p_source source node
     * @param p_pods pods
     * @param p_time time step
     * @throws GRBException gurobi exception
     */
    public CPodsSPP( final CJungEnvironment p_env, final Integer p_source, final ArrayList<CPod> p_pods, final Integer p_time ) throws GRBException
    {
        m_env = new GRBEnv( "pspp.log" );
        m_model = new GRBModel( m_env );
        m_ys = new GRBVar[ p_env.size() + 1][p_env.size() + 1];
        m_xs = new HashMap<>();
        p_pods.forEach( p -> m_xs.put( p.name(), new GRBVar[p_env.size() + 1][p_env.size() + 1] ) );
        m_source = p_source;
        m_pods = p_pods;
        m_graph = p_env;
        m_results = new HashMap<>();
        m_indivres = new HashMap<>();
        p_pods.forEach( p -> m_indivres.put( p, new HashSet<>() ) );
        m_time = p_time;

        final GRBLinExpr l_obj = new GRBLinExpr();
        p_env.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );
            try
            {
                if ( e.weight().doubleValue() == 0.0 )
                {
                    m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.00,
                            GRB.BINARY,
                            "y" + l_start  + "-" + l_end );
                    l_obj.addTerm( e.weight().doubleValue() + 0.00001, m_ys[l_start][l_end] );
                }

                else
                {
                    m_ys[l_start][l_end] = m_model.addVar( 0.0, 1.0, e.weight().doubleValue(),
                            GRB.BINARY,
                            "y" + l_start  + "-" + l_end );
                    l_obj.addTerm( e.weight().doubleValue(), m_ys[l_start][l_end] );
                }


                p_pods.forEach( p ->
                {
                    final String l_ds = p.name();
                    final GRBVar[][] l_temp = m_xs.get( l_ds );
                    try
                    {
                        l_temp[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                                GRB.BINARY,
                                "x" + "_" + l_ds + ":" + l_start + "-" + l_end );
                    }
                    catch ( final GRBException l_err )
                    {
                        l_err.printStackTrace();
                    }
                } );
            }
            catch ( final GRBException l_e1 )
            {
                l_e1.printStackTrace();
            }

        } );
        m_model.setObjective( l_obj );
        m_opt = null;
    }

    @Override
    public void solve() throws GRBException
    {
        addConstraints();
        addLengthConstraint();
        addSpeedConstrain();
        addTimeConstraint();
        m_model.optimize();
        saveResults();
        System.out.println( "Objective function solution: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
        m_opt = m_model.get( GRB.DoubleAttr.ObjVal );
        cleanUp();
    }

    private void addConstraints()
    {
        //flow constraint
        m_pods.forEach( p ->
        {
            final GRBVar[][] l_temp = m_xs.get( p.name() );
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
                    if  ( i.equals( m_source ) )
                        m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "Origin" +  p );
                    else if ( i == Integer.valueOf( p.destination() ) )
                        m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "Destination" +  p );
                    else
                        m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "Flow" +  p );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );

        //x<=y
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            m_pods.forEach( p ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                final GRBVar[][] l_temp = m_xs.get( p.name() );
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

    private void addLengthConstraint()
    {
        m_pods.forEach( p ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                final double l_ml = p.preferences().lengthLimit();
                final GRBLinExpr l_dist = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_dist.addTerm( e.length(), l_temp[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] ) );
                m_model.addConstr( l_dist, GRB.LESS_EQUAL, l_ml, "maxdist" + p.name() );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    private void addTimeConstraint()
    {
        final HashMap<IEdge, Double> l_times = new HashMap<>();
        m_graph.edges().forEach( e -> l_times.put( e, e.length() / m_speed ) );
        m_pods.forEach( p ->
        {
            try
            {
                final GRBVar[][] l_temp = m_xs.get( p.name() );
                final Integer l_tl = p.preferences().timeLimit();
                final GRBLinExpr l_time = new GRBLinExpr();
                m_graph.edges().forEach( e -> l_time.addTerm( l_times.get( e ), l_temp[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] ) );
                m_model.addConstr( l_time, GRB.LESS_EQUAL, l_tl, "maxtime" + p );
                m_model.addConstr( l_time, GRB.GREATER_EQUAL, m_time, "startime" );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    private void addSpeedConstrain()
    {
        final ArrayList<Double> l_minspeeds  = new ArrayList<>();
        final ArrayList<Double> l_maxspeeds = new ArrayList<>();
        m_pods.forEach( p ->
        {
            l_minspeeds.add( p.preferences().minSpeed() );
            l_maxspeeds.add( p.preferences().maxSpeed() );
        } );
        final Double l_maxspeed = Collections.min( l_maxspeeds );
        final Double l_minspeed = Collections.max( l_minspeeds );
        m_speed = Math.max( l_maxspeed, l_minspeed );
    }

    private void saveResults()
    {
        m_graph.edges().forEach( e ->
        {
            final Integer l_start = Integer.valueOf( e.from().id() );
            final Integer l_end = Integer.valueOf( e.to().id() );

            m_pods.forEach( p ->
            {
                try
                {
                    final GRBVar[][] l_temp = m_xs.get( p.name() );
                    final HashSet<IEdge> l_res = m_indivres.get( p );
                    if ( ( l_temp[l_start][l_end] != null ) && ( l_temp[l_start][l_end].get( GRB.DoubleAttr.X ) == 1 ) )
                    {
                        m_results.put( e, m_results.getOrDefault( e, 0 ) + 1 );
                        l_res.add( e );
                        m_endtimes.put( p.name(), m_endtimes.getOrDefault( p.name(), 0 ) + Integer.valueOf( Math.toIntExact( Math.round( e.length() / m_speed ) ) ) );
                        m_lengths.put( p.name(), m_lengths.getOrDefault( p.name(), 0.0 ) + e.length() );
                    }
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );
        } );
    }

    public HashMap<CPod, HashSet<IEdge>> getRoutes()
    {
        return m_indivres;
    }

    /**
     * calculates the costs of the route
     */
    public void getCosts()
    {
        final HashMap<IEdge, Integer> l_np = new HashMap<>();
        final HashMap<CPod, Double> l_costs = new HashMap<>();

        m_pods.forEach( p -> m_indivres.get( p ).forEach( e -> l_np.put( e, l_np.getOrDefault( e, 0 ) + 1 ) ) );

        m_indivres.keySet().forEach( k ->
        {
            final Set<IEdge> l_edges = m_indivres.get( k );
            final AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
            final AtomicReference<Double> l_total = new AtomicReference<>( 0.00 );
            final AtomicReference<Double> l_distcost = new AtomicReference<>( 0.00 );
            final AtomicReference<Double> l_function = new AtomicReference<>( 0.00 );
            l_edges.forEach( e ->
            {
                l_total.getAndUpdate( v -> v + e.weight().doubleValue() );
                l_cost.getAndUpdate( v -> v + e.weight().doubleValue() / l_np.get( e ) );
                l_distcost.getAndUpdate( v -> v + 2 * e.length() + ( e.length() / m_speed ) - l_cost.get() );
                l_function.getAndUpdate( v -> v + 0.6 * e.length() / m_speed + 0.4 * e.weight().doubleValue() / l_np.get( e ) - l_cost.get() );
            } );
            l_costs.put( k, l_function.get() );
        } );

        System.out.println();

        System.out.println( "The number of platooning vehicles per edge are: " );
        l_np.keySet().forEach( k -> System.out.println( k.id() + ": " + l_np.get( k ) ) );
        System.out.println();

        System.out.println( "The costs are as follows:" );
        l_costs.keySet()
                .forEach( k -> System.out.println( "Destination " + k.destination() + " platoon cost:" + l_costs.get( k ) ) );
        System.out.println();

        System.out.println( "Speed is: " + m_speed );
        System.out.println( "System optimum is: " + m_opt );

        System.out.println( "Endtimes are:" );
        m_endtimes.keySet().forEach( k ->
                System.out.println( "Destination " + k.toString() + " endtime " + m_endtimes.get( k ) ) );

        System.out.println( "Distances are: " );
        m_lengths.keySet().forEach( k -> System.out.println( "Destination " + k.toString() + "length of route " + m_lengths.get( k ) ) );
    }

    @Override
    public void display() throws GRBException
    {
        System.out.println( "Origin is: " + m_source.toString() );
        System.out.println( "Destinations are: " );
        m_pods.forEach( p -> System.out.print( p.name() + ": " +  p.destination() + " " ) );
        System.out.println();
        System.out.println();

        m_indivres.keySet().forEach( d -> m_indivres.get( d ).forEach( x -> System.out.println( "x" + d + ":" + x.id() ) ) );
        System.out.println();

        m_results.keySet().forEach( y -> System.out.println( "y:" + y.id() ) );
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
