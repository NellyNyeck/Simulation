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

/**
 * the solver class for determining a single shortest path
 */
public class CPlatSPP
{
    private GRBEnv m_env;
    private GRBModel m_model;

    boolean anyOf( final int p_comp, final ArrayList<Integer> p_list )
    {
        return p_list.stream().anyMatch( c -> p_comp == c );
    }

    /**
     * creation of the optimisation model for a Jung type environment
     * @param p_origin the origin node
     * @param p_destinations the list of destinations
     * @param p_network the graph
     */
    public void solveJung( final int p_origin, final ArrayList<Integer> p_destinations, final CJungEnvironment p_network )
    {
        try
        {
            //creating the environment and model
            m_env = new GRBEnv( "sppJung.log" );
            m_model = new GRBModel( m_env );

            //getting all edges of an environment
            final Collection<IEdge> l_edges = p_network.edges();

            //creating the variables
            final GRBVar[][] l_ytarg = new GRBVar[p_network.size()][p_network.size()];
            final ArrayList<GRBVar[][]> l_xs = new ArrayList<>();
            p_destinations.forEach( c -> l_xs.add( new GRBVar[p_network.size()][p_network.size()] ) );

            //adding variables to the model
            l_edges.forEach( iEdge ->
            {
                final int l_start = Integer.valueOf( iEdge.from().id() );
                final int l_end = Integer.valueOf( iEdge.to().id() );
                try
                {
                    l_ytarg[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "y" + l_start  + "_" + l_end );
                    for ( int i = 0; i < p_destinations.size(); i++ )
                    {
                        final GRBVar[][] l_temp = l_xs.get( i );
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

            // Set objective
            final GRBLinExpr l_obj = new GRBLinExpr();
            l_edges.forEach( c -> l_obj.addTerm( 1.0, l_ytarg[Integer.valueOf( c.from().id() )][Integer.valueOf( c.to().id() )] ) );
            m_model.setObjective( l_obj, GRB.MINIMIZE );

            //Adding constraints

            //the network constraint
            l_edges.forEach( c ->
            {
                final int l_from = Integer.valueOf( c.from().id() );
                final int l_to = Integer.valueOf( c.to().id() );
                l_xs.forEach( d ->
                {
                    final GRBLinExpr l_expr = new GRBLinExpr();
                    final GRBVar[][] l_temp = d;
                    l_expr.addTerm( 1.0, l_temp[l_from][l_to] );
                    l_expr.addTerm( -1.0, l_ytarg[l_from][l_to] );
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
            l_xs.forEach( x ->
            {
                final GRBVar[][] l_temp = x;
                for ( int i = 0; i < p_network.size(); i++ )
                {
                    final GRBLinExpr l_expr = new GRBLinExpr();
                    for ( int j = 0; j < p_network.size(); j++ )
                    {
                        if ( ( l_ytarg[i][j] != null ) && ( l_ytarg[j][i] != null ) )
                        {
                            l_expr.addTerm( 1.0, l_temp[i][j] );
                            l_expr.addTerm( -1.0, l_temp[j][i] );

                        }
                    }
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
                }
                l_ks.getAndIncrement();
            } );

            //optimizing model
            m_model.optimize();


            //displaying the target
            for ( int i = 0; i < p_network.size(); i++ )
            {
                for ( int j = 0; j < p_network.size(); j++ )
                {
                    if (  ( l_ytarg[i][j] != null ) && ( l_ytarg[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                    {
                        System.out.println( l_ytarg[i][j].get( GRB.StringAttr.VarName ) + ":" + l_ytarg[i][j].get( GRB.DoubleAttr.X ) );
                    }
                }
            }

            //displaying each variable
            l_xs.forEach( r ->
            {
                System.out.println();
                for ( int i = 0; i < p_network.size(); i++ )
                {
                    for ( int j = 0; j < p_network.size(); j++ )
                    {
                        try
                        {
                            if (  ( l_ytarg[i][j] != null ) && ( l_ytarg[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                            {
                                System.out.print( r[i][j].get( GRB.StringAttr.VarName ) + ":" + r[i][j].get( GRB.DoubleAttr.X ) + " " );
                            }
                        }
                        catch ( final GRBException l_err )
                        {
                            l_err.printStackTrace();
                        }
                    }
                }
            } );
            System.out.println();
            System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) + " " + l_obj.getValue() );
            System.out.println();

            //cleaning up
            m_model.dispose();
            m_env.dispose();


        }
        catch ( final GRBException l_err )
        {
            System.out.println( "Error code: " + l_err.getErrorCode() + ". " + l_err.getMessage() );
        }
    }
}
