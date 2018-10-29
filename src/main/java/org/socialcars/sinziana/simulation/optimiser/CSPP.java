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


public class CSPP implements ISPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_xs;
    private Integer m_length;
    private double m_cost;
    private ArrayList<IEdge> m_results;
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
        m_results = new ArrayList<>();

        p_network.edges().forEach( iEdge ->
        {
            final INode l_start = iEdge.from();
            final INode l_end = iEdge.to();
            try
            {
                if ( iEdge.weight().doubleValue() == 0.0 )
                    m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] = m_model.addVar( 0.0, 1.0, iEdge.weight().doubleValue() + 0.000001,
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );
                else m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] = m_model.addVar( 0.0, 1.0, iEdge.weight().doubleValue(),
                    GRB.BINARY,
                    "y" + l_start  + "-" + l_end );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * solve function
     * @throws GRBException gurobi
     */
    public void solve() throws GRBException
    {
        addConstraints( m_graph, m_origin, m_destination );
        m_model.optimize();
        saveResults();
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

    private void saveResults() throws GRBException
    {
        m_graph.edges().forEach( e ->
        {
            try
            {
                if ( ( m_xs[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] != null )
                    && ( m_xs[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )].get( GRB.DoubleAttr.X ) == 1 ) )
                {
                    m_results.add( e );
                    m_length++;
                }
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );


    }

    /**
     * displays the result
     * @throws GRBException gurobi
     */
    public void display() throws GRBException
    {
        m_results.forEach( e -> System.out.println( e.id() ) );
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
