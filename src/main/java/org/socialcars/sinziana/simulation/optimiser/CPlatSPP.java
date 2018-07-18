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
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * the solver class
 */
public final class CPlatSPP
{

    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ytarg;
    private final ArrayList<GRBVar[][]> m_xs;

    /**
     * ctor
     * @param p_network the network
     * @param p_destinations array for destinations
     * @throws GRBException exception
     */
    public CPlatSPP( final CJungEnvironment p_network,  final ArrayList<Integer> p_destinations ) throws GRBException
    {
        //creating the environment and model
        m_env = new GRBEnv( "sppJung.log" );
        m_model = new GRBModel( m_env );
        m_ytarg = new GRBVar[p_network.size()][p_network.size()];
        m_xs = new ArrayList<>();
        p_destinations.forEach( c -> m_xs.add( new GRBVar[p_network.size()][p_network.size()] ) );

        //adding variables to the model
        p_network.edges().forEach( iEdge ->
        {
            final int l_start = Integer.valueOf( iEdge.from().id() );
            final int l_end = Integer.valueOf( iEdge.to().id() );
            try
            {
                m_ytarg[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "y" + l_start  + "_" + l_end );
                for ( int i = 0; i < p_destinations.size(); i++ )
                {
                    final GRBVar[][] l_temp = m_xs.get( i );
                    l_temp[l_start][l_end] = m_model.addVar(  0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + String.valueOf( i ) + l_start + "_" + l_end );
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * creation of the optimisation model for a Jung type environment
     * @param p_origin the origin node
     * @param p_destinations the list of destinations
     * @param p_network the graph
     * @throws GRBException exception
     */
    public void solveJung( final int p_origin, final ArrayList<Integer> p_destinations, final CJungEnvironment p_network ) throws GRBException
    {
        setObjective( p_network.edges() );
        setConstraints( p_network.edges(), p_destinations, p_origin, p_network.size() );
        m_model.optimize();
        display( p_network.size() );
        cleanUp();
    }

    private void setObjective( final Collection<IEdge> p_edges ) throws GRBException
    {
        final GRBLinExpr l_obj = new GRBLinExpr();
        p_edges.forEach( c -> l_obj.addTerm( 1.0, m_ytarg[Integer.valueOf( c.from().id() )][Integer.valueOf( c.to().id() )] ) );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    private void setConstraints( final Collection<IEdge> p_edges, final ArrayList<Integer> p_destinations, final int p_origin, final Integer p_networksize )
    {
        //the network constraint
        p_edges.forEach( c ->
        {
            final int l_from = Integer.valueOf( c.from().id() );
            final int l_to = Integer.valueOf( c.to().id() );
            m_xs.forEach( d ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                final GRBVar[][] l_temp = d;
                l_expr.addTerm( 1.0, l_temp[l_from][l_to] );
                l_expr.addTerm( -1.0, m_ytarg[l_from][l_to] );
                try
                {
                    m_model.addConstr( l_expr, GRB.LESS_EQUAL, 0.0, "NetworkConstraint" );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );

        } );

        //the flow constraint
        final AtomicInteger l_ks = new AtomicInteger();
        m_xs.forEach( x ->
        {
            final GRBVar[][] l_temp = x;
            IntStream.range( 0, p_networksize )
                .boxed()
                .forEach( i ->
                {
                    final GRBLinExpr l_expr = new GRBLinExpr();
                    IntStream.range( 0, p_networksize )
                        .boxed()
                        .forEach( j ->
                        {
                            if ( ( m_ytarg[i][j] != null ) && ( m_ytarg[j][i] != null ) )
                            {
                                l_expr.addTerm( 1.0, l_temp[i][j] );
                                l_expr.addTerm( -1.0, l_temp[j][i] );

                            }
                        } );
                    try
                    {
                        if ( i == Integer.valueOf( p_origin ) )
                        {
                            m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "OriginConstraint" + String.valueOf( l_ks.get() ) );
                        }
                        else if ( p_destinations.get( l_ks.get() ) == i )
                        {
                            m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "DestinationConstraint" + String.valueOf( l_ks.get() ) );
                        }
                        else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "FlowConstraint" + String.valueOf( l_ks.get() ) );
                    }
                    catch ( final GRBException l_err )
                    {
                        l_err.printStackTrace();
                    }
                } );
            l_ks.getAndIncrement();
        } );
    }

    private void display( final int p_networksize )
    {
        //displaying the target
        IntStream.range( 0, p_networksize )
            .boxed()
            .forEach( i ->
            {
                IntStream.range( 0, p_networksize )
                    .boxed()
                    .forEach( j ->
                    {
                        try
                        {
                            if (  ( m_ytarg[i][j] != null ) && ( m_ytarg[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                            {
                                System.out.println( m_ytarg[i][j].get( GRB.StringAttr.VarName ) + ":" + m_ytarg[i][j].get( GRB.DoubleAttr.X ) );
                            }
                        }
                        catch ( final GRBException l_err )
                        {
                            l_err.printStackTrace();
                        }
                    } );
            } );

        //displaying each variable
        m_xs.forEach( r ->
        {
            System.out.println();
            IntStream.range( 0, p_networksize )
                .boxed()
                .forEach( i ->
                {
                    IntStream.range( 0, p_networksize )
                        .boxed()
                        .forEach( j ->
                        {
                            try
                            {
                                if (  ( m_ytarg[i][j] != null ) && ( m_ytarg[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                                {
                                    System.out.print( r[i][j].get( GRB.StringAttr.VarName ) + ":" + r[i][j].get( GRB.DoubleAttr.X ) + " " );
                                }
                            }
                            catch ( final GRBException l_err )
                            {
                                l_err.printStackTrace();
                            }
                        } );
                } );
        } );
    }

    private void cleanUp() throws GRBException
    {
        //cleaning up
        m_model.dispose();
        m_env.dispose();
    }
}
