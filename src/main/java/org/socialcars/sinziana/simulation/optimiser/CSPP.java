package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;

import java.util.ArrayList;

public class CSPP implements ISPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_xs;
    private Integer m_length;
    private double m_cost;
    private CJungEnvironment m_graph;
    private Integer m_origin;
    private Integer m_destination;

    /**
     * ctor
     * @param p_network jung graph
     * @throws GRBException gurobi
     */
    public CSPP( final CJungEnvironment p_network, final Integer p_origin, final Integer p_destination ) throws GRBException
    {
        m_env = new GRBEnv( "spp.log" );
        m_model = new GRBModel( m_env );
        m_xs = new GRBVar[p_network.size() + 1][p_network.size() + 1];
        m_length = 0;
        m_cost = 0;
        m_graph = p_network;
        m_origin = p_origin;
        m_destination = p_destination;

        final GRBLinExpr l_obj = new GRBLinExpr();
        p_network.edges().forEach( iEdge ->
        {
            final INode l_start = iEdge.from();
            final INode l_end = iEdge.to();
            try
            {
                m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] = m_model.addVar( 0.0, 1.0, 0.0,
                    GRB.BINARY,
                    "x" + l_start.id()  + "_" + l_end.id() );
                l_obj.addTerm( iEdge.weight().doubleValue(), m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    public void solve() throws GRBException
    {
        addConstraints( m_graph, m_origin, m_destination );
        m_model.optimize();
        display();
        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
        m_cost = m_model.get( GRB.DoubleAttr.ObjVal );
        System.out.println();
        m_model.dispose();
        m_env.dispose();
    }

    private void addConstraints( final CJungEnvironment p_network, final Integer p_origin, final Integer p_destination ) throws GRBException
    {
        for ( int i = 0; i < p_network.size(); i++ )
        {
            final GRBLinExpr l_expr = new GRBLinExpr();
            for ( int j = 0; j < p_network.size(); j++ )
            {
                if ( m_xs[i][j] != null ) l_expr.addTerm( 1.0, m_xs[i][j] );
                if ( m_xs[j][i] != null ) l_expr.addTerm( -1.0, m_xs[j][i] );
            }
            if ( i == Integer.valueOf( p_origin ) ) m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "OriginConstraint" );
            else if ( i == Integer.valueOf( p_destination ) ) m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "DestinationConstraint" );
            else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "FlowConstraint" );
        }
    }

    /**
     * displays the result
     * @throws GRBException gurobi
     */
    public void display() throws GRBException
    {
        for ( int i = 0; i < m_graph.size(); i++ )
        {
            for ( int j = 0; j < m_graph.size(); j++ )
            {
                if ( m_xs[i][j] != null && m_xs[i][j].get( GRB.DoubleAttr.X ) == 1 )
                {
                    System.out.println( m_xs[i][j].get( GRB.StringAttr.VarName ) + " " + m_xs[i][j].get( GRB.DoubleAttr.X ) );
                    m_length++;
                }
            }
        }
    }

    public Integer length()
    {
        return m_length;
    }

    @Override
    public Double cost()
    {
        return m_cost;
    }

}
