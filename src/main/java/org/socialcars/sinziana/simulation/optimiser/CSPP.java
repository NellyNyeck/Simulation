package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;

import java.util.Collection;
import java.util.stream.IntStream;

/**
 * the solver class for determining a single shortest path
 */
public class CSPP
{
    private final GRBEnv m_env;
    private final GRBModel m_model;
    private final GRBVar[][] m_ex;


    /**
     * ctor
     * @param p_network the network
     * @throws GRBException exception
     */
    public CSPP( final CJungEnvironment p_network ) throws GRBException
    {
        //creating the environment and model
        m_env = new GRBEnv( "sppJung.log" );
        m_model = new GRBModel( m_env );
        m_ex = new GRBVar[p_network.size()][p_network.size()];

        //adding variables to the model
        p_network.edges().forEach( iEdge ->
        {
            final int l_start = Integer.valueOf( iEdge.from().id() );
            final int l_end = Integer.valueOf( iEdge.to().id() );
            try
            {
                m_ex[l_start][l_end] = m_model.addVar( 0.0, 1.0, 0.0, GRB.BINARY, "x" + l_start  + "_" + l_end );
            }
            catch ( final GRBException l_err )
            {
                l_err.printStackTrace();
            }
        } );
    }

    /**
     * the solver
     * @param p_origin origin
     * @param p_destination destination
     * @param p_network network
     * @throws GRBException exeption
     */
    public void solveJung( final int p_origin, final int p_destination, final CJungEnvironment p_network ) throws GRBException
    {
        setObjective( p_network.edges() );
        setConstraints( p_destination, p_origin, p_network.size() );
        m_model.optimize();
        display( p_network.size() );
        cleanUp();
    }

    private void setObjective( final Collection<IEdge> p_edges ) throws GRBException
    {
        final GRBLinExpr l_obj = new GRBLinExpr();
        p_edges.forEach( c -> l_obj.addTerm( 1.0, m_ex[Integer.valueOf( c.from().id() )][Integer.valueOf( c.to().id() )] ) );
        m_model.setObjective( l_obj, GRB.MINIMIZE );
    }

    private void setConstraints( final int p_destination, final int p_origin, final Integer p_networksize )
    {
        IntStream.range( 0, p_networksize )
            .boxed()
            .forEach( i ->
            {
                final GRBLinExpr l_expr = new GRBLinExpr();
                IntStream.range( 0, p_networksize )
                    .boxed()
                    .forEach( j ->
                    {
                        if ( ( m_ex[i][j] != null ) && ( m_ex[j][i] != null ) )
                        {
                            l_expr.addTerm( 1.0, m_ex[i][j] );
                            l_expr.addTerm( -1.0, m_ex[j][i] );

                        }
                    } );
                try
                {
                    if ( i == p_origin )
                    {
                        m_model.addConstr( l_expr, GRB.EQUAL, 1.0, "OriginConstraint"  );
                    }
                    else if ( p_destination == i )
                    {
                        m_model.addConstr( l_expr, GRB.EQUAL, -1.0, "DestinationConstraint"  );
                    }
                    else m_model.addConstr( l_expr, GRB.EQUAL, 0.0, "FlowConstraint" + i );
                }
                catch ( final GRBException l_err )
                {
                    l_err.printStackTrace();
                }
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
                            if (  ( m_ex[i][j] != null ) && ( m_ex[i][j].get( GRB.DoubleAttr.X ) == 1 ) )
                            {
                                System.out.println( m_ex[i][j].get( GRB.StringAttr.VarName ) + ":" + m_ex[i][j].get( GRB.DoubleAttr.X ) );
                            }
                        }
                        catch ( final GRBException l_err )
                        {
                            l_err.printStackTrace();
                        }
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
