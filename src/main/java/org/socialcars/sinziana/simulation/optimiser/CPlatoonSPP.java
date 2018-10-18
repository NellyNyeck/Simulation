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

public class CPlatoonSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ytarg;
    private final ArrayList<GRBVar[][]> m_xs;

    public CPlatoonSPP( final CJungEnvironment p_network, final ArrayList<Integer> p_destinations ) throws GRBException
    {
        m_env = new GRBEnv( "sppJung.log" );
        m_model = new GRBModel( m_env );
        m_ytarg = new GRBVar[p_network.size() + 1][p_network.size() + 1];
        m_xs = new ArrayList<>();
        //IF PROBLEM HERE
        //GRBVar[][] l_empty = new GRBVar[p_network.size() + 1][p_network.size() + 1];
        //m_xs.add( l_empty );
        p_destinations.forEach( c -> m_xs.add( new GRBVar[p_network.size() + 1][p_network.size() + 1] ) );

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
                //AND HERE
                for ( int i = 0; i < p_destinations.size(); i++ )
                {
                    final GRBVar[][] l_temp = m_xs.get( i );
                    l_temp[l_start][l_end] = m_model.addVar(  0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + String.valueOf( i ) + l_start + "_" + l_end );
                }
            }
            catch (GRBException e)
            {
                e.printStackTrace();
            }

        } );
    }

    public void solve( final int p_origin, final ArrayList<Integer> p_destinations, final CJungEnvironment p_network ) throws GRBException
    {
        setObjective( p_network.edges() );
        addConstraints( p_network, p_destinations, p_origin, p_network.size() );
        m_model.optimize();
        display( p_network.size() );
        cleanUp();
    }

    private void setObjective( final Collection<IEdge> p_edges ) throws GRBException
    {
        final GRBLinExpr l_obj = new GRBLinExpr();
        p_edges.forEach( c -> l_obj.addTerm((Double) c.weight(), m_ytarg[Integer.valueOf( c.from().id() )][Integer.valueOf( c.to().id() )] ) );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    private void addConstraints( final CJungEnvironment p_network, final ArrayList<Integer> p_destinations, final int p_origin, final Integer p_networksize )
    {
        //the network constraint
        p_network.edges().forEach( c ->
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
            IntStream.range( 1, p_networksize + 1 )
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

        //lengt & cost constraint
        IntStream.range( 0, p_destinations.size() ).boxed().forEach( k ->
        {
            try
            {
                final CSPP l_indiv = new CSPP( p_network );
                l_indiv.solve( p_origin, p_destinations.get( k ), p_network );
                Double l_ml = Double.valueOf( l_indiv.length() );
                l_ml = l_ml + l_ml * 0.25;

                final GRBVar[][] l_temp = m_xs.get( k );
                final GRBLinExpr l_dist = new GRBLinExpr();
                IntStream.range( 0, p_networksize ).boxed().forEach( i ->
                {
                    IntStream.range( 0, p_networksize ).boxed().forEach( j ->
                    {
                        if ( l_temp[i][j] != null )
                            l_dist.addTerm( 1.0, l_temp[i][j] );
                    } );
                } );
                m_model.addConstr( l_dist, GRB.LESS_EQUAL, l_ml, "maxdist" + String.valueOf( k ) );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );

        //cost constraint
    }

    private void display( final int p_networksize )
    {
        //displaying the target
        IntStream.range( 1, p_networksize + 1 )
            .boxed()
            .forEach( i ->
            {
                IntStream.range( 1, p_networksize + 1 )
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
