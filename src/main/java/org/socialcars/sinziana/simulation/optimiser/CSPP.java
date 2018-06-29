package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.INode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * the solver class for determining a single shortest path
 */
public class CSPP
{
    private GRBEnv m_env;
    private GRBModel m_model;

    /**
     * creation of the optimisation model for a Jung type environment
     * @param p_origin the origin node
     * @param p_destination the destination node
     * @param p_network the graph
     * @return the number of edges in shortest path
     */
    public double solveJung( final int p_origin, final int p_destination, final CJungEnvironment p_network )
    {
        try
        {
            m_env = new GRBEnv( "spp.log" );
            m_model = new GRBModel( m_env );
            final List<GRBVar> l_variables = new ArrayList<>();


            final Collection<IEdge> l_edges = p_network.edges();
            final GRBVar[][] l_ex = new GRBVar[p_network.size()][p_network.size()];
            l_edges.forEach( iEdge ->
            {
                final INode l_start = iEdge.from();
                final INode l_end = iEdge.to();
                try
                {
                    l_ex[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] = m_model.addVar( 0.0, 1.0, 0.0,
                        GRB.BINARY,
                        "x" + l_start.id()  + "_" + l_end.id() );
                    l_variables.add( l_ex[Integer.valueOf( l_start.id() ) ][Integer.valueOf( l_end.id() )] );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );

            // Set objective
            final GRBLinExpr l_obj = new GRBLinExpr();
            l_variables.forEach( c -> l_obj.addTerm( 1.0, c ) );
            m_model.setObjective( l_obj, GRB.MINIMIZE );

            //Adding constraints
            l_variables.forEach( c ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                l_expr.addTerm( 1.0, c );
                try
                {
                    m_model.addConstr( l_expr, GRB.LESS_EQUAL, 1,
                        "NetworkConstraint" );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
            } );


            for ( int i = 0; i < p_network.size(); i++ )
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                for ( int j = 0; j < p_network.size(); j++ )
                {
                    if ( l_ex[i][j] != null ) l_expr.addTerm( 1.0, l_ex[i][j] );
                    if ( l_ex[j][i] != null ) l_expr.addTerm( -1.0, l_ex[j][i] );
                }
                if ( i == Integer.valueOf( p_origin ) ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "OriginConstraint" );
                else if ( i == Integer.valueOf( p_destination ) ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "DestinationConstraint" );
                else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "FlowConstraint" );
            }

            m_model.optimize();

            for ( int i = 0; i < p_network.size(); i++ )
            {
                for ( int j = 0; j < p_network.size(); j++ )
                {
                    if ( l_ex[i][j] != null )
                    {
                        System.out.println( l_ex[i][j].get( GRB.StringAttr.VarName ) + " " + l_ex[i][j].get( GRB.DoubleAttr.X ) );
                    }
                }
            }

            System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) + " " + l_obj.getValue() );
            System.out.println();

            final double l_result = l_obj.getValue();

            m_model.dispose();
            m_env.dispose();

            return l_result;

        }
        catch ( final GRBException l_err )
        {
            System.out.println( "Error code: " + l_err.getErrorCode() + ". " + l_err.getMessage() );
        }
        return Double.parseDouble( null );
    }

}
