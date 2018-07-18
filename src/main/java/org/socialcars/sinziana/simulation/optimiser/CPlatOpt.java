package org.socialcars.sinziana.simulation.optimiser;

import net.sf.jmpi.main.MpDirection;
import net.sf.jmpi.main.MpOperator;
import net.sf.jmpi.main.MpProblem;
import net.sf.jmpi.main.MpResult;
import net.sf.jmpi.main.MpSolver;
import net.sf.jmpi.main.expression.MpExpr;
import net.sf.jmpi.main.expression.MpExprTerm;
import net.sf.jmpi.solver.gurobi.SolverGurobi;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


/**
 * another platooning optimiser classxs
 */
public class CPlatOpt
{
    private final MpSolver m_solver;
    private final MpProblem m_problem;
    private final MpExprTerm[][] m_ys;
    private final ArrayList<MpExprTerm[][]> m_xlist;

    /**
     * ctor
     * @param p_origin the origin
     * @param p_destinations the destinations
     * @param p_network the network
     */
    public CPlatOpt( final int p_origin, final ArrayList<Integer> p_destinations, final CJungEnvironment p_network )
    {
        m_solver = new SolverGurobi();
        m_problem = new MpProblem();

        m_ys = new MpExprTerm[p_network.size()][p_network.size()];
        p_network.edges().forEach( e ->
        {
            final String l_yy = "y" + e.from().id() + "_" + e.to().id();
            m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] = new MpExprTerm( 1, l_yy );
        } );

        m_xlist = new ArrayList<>();
        IntStream.range( 0, p_destinations.size() )
            .boxed()
            .forEach( i ->
            {
                final MpExprTerm[][] l_xs = new MpExprTerm[p_network.size()][p_network.size()];
                m_xlist.add( l_xs );
            } );
        final AtomicInteger l_count = new AtomicInteger();
        m_xlist.forEach( x ->
        {
            p_network.edges().forEach( e ->
            {
                final String l_xx = x + String.valueOf( l_count.get() ) + e.from().id() + "_" + e.to().id();
                x[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] = new MpExprTerm( 1, l_xx );
                l_count.getAndIncrement();
            } );
        } );



        addVariables( p_network );
        setObjective( p_network );
        setConstraints( p_origin, p_destinations, p_network );
    }

    private void addVariables( final CJungEnvironment p_network )
    {
        p_network.edges().forEach( e ->
        {
            m_problem.addVar( m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], Integer.class );
        } );

        m_xlist.forEach( x ->
        {
            p_network.edges().forEach( e ->
            {
                m_problem.addVar( x[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], Integer.class );
            } );
        } );
    }

    private void setObjective( final CJungEnvironment p_network )
    {
        final MpExpr l_objective = new MpExpr();
        p_network.edges().forEach( e ->
        {
            l_objective.addTerm( m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )] );
        } );
        m_problem.setObjective( l_objective, MpDirection.MIN );
    }

    private void setConstraints( final int p_origin, final ArrayList<Integer> p_destinations, final CJungEnvironment p_network )
    {
        p_network.edges().forEach( e ->
        {
            m_problem.add( MpExpr.prod( m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], 1 ), MpOperator.GE, 0 );
            m_problem.add( MpExpr.prod( m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], 1 ), MpOperator.LE, 1 );

        } );

        m_xlist.forEach( x ->
        {
            final AtomicInteger l_count = new AtomicInteger();

            p_network.edges().forEach( e ->
            {
                m_problem.add( MpExpr.prod( x[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], 1 ), MpOperator.GE, 0 );
                m_problem.add( MpExpr.prod( x[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], 1 ), MpOperator.LE, 1 );
                m_problem.add( MpExpr.sum(
                    MpExpr.prod( x[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], 1 ),
                    MpExpr.prod( m_ys[Integer.valueOf( e.from().id() )][Integer.valueOf( e.to().id() )], -1 ) ),
                    MpOperator.LE, 0 );
            } );
            IntStream.range( 0, p_network.size() )
                .boxed()
                .forEach( i ->
                {
                    final MpExpr l_sum = new MpExpr();
                    IntStream.range( 0, p_network.size() )
                        .boxed()
                        .forEach( j ->
                        {
                            if ( x[i][j] != null )
                            {
                                l_sum.addTerm( x[i][j] );
                            }
                            if ( x[j][i] != null )
                            {
                                l_sum.addTerm( x[j][i].mul( -1 ) );
                            }

                        } );
                    if ( i == p_origin )
                    {
                        m_problem.add( l_sum, MpOperator.EQ, 1 );
                    }
                    else if ( i == p_destinations.get( l_count.get() ) )
                    {
                        m_problem.add( l_sum, MpOperator.EQ, -1 );
                    }
                    else
                    {
                        m_problem.add( l_sum, MpOperator.EQ, 0 );
                    }
                } );
            l_count.getAndIncrement();
        } );
    }

    /**
     * the solve function
     */
    public void solve()
    {
        m_solver.add( m_problem );
        final MpResult l_result = m_solver.solve();
        System.out.println( l_result );
    }
}
