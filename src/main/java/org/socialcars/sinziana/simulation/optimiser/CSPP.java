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

public class CSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final ArrayList<GRBVar> m_variables;
    private final GRBVar[][] m_xs;
    private Integer m_length;


    public CSPP( final CJungEnvironment p_network ) throws GRBException
    {
        m_env = new GRBEnv( "spp.log" );
        m_model = new GRBModel( m_env );
        m_variables = new ArrayList<>();
        m_xs = new GRBVar[p_network.size() + 1][p_network.size() + 1];
        m_length = 0;

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
                l_obj.addTerm((Double) iEdge.weight(), m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] );
                m_variables.add( m_xs[Integer.valueOf( l_start.id() )][Integer.valueOf( l_end.id() )] );
            }
            catch (GRBException e)
            {
                e.printStackTrace();
            }
        } );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    public void solve( final int p_origin, final int p_destination, final CJungEnvironment p_network ) throws GRBException
    {
        addConstraints( p_network, p_origin, p_destination );
        m_model.optimize();
        display( p_network );
        System.out.println( "Obj: " + m_model.get( GRB.DoubleAttr.ObjVal ) );
        System.out.println();
        m_model.dispose();
        m_env.dispose();
    }

    private void addConstraints( final CJungEnvironment p_network, final Integer p_origin, final Integer p_destination ) throws GRBException
    {
        /*m_variables.forEach( c ->
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
        } );*/

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

    public void display( final CJungEnvironment p_network ) throws GRBException
    {
        for ( int i = 0; i < p_network.size(); i++ )
        {
            for ( int j = 0; j < p_network.size(); j++ )
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

}
