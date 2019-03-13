package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.optimiser.CPodsSPP;
import org.socialcars.sinziana.simulation.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * testing the optimiser class to bring everything together
 */
public class TestCTotalSPP
{
    private static final CInputpojo INPUT;
    private CJungEnvironment m_env;
    private CPodsSPP m_opt;
    private ArrayList<CPod> m_pods;
    private CUnits m_unit;
    private Integer m_time;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5Total.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     */
    @Before
    public void init()
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_pods = new ArrayList<>();
        final Set<CPodpojo> l_pods = new HashSet<>();
        INPUT.getProviders().forEach( p ->
        {
            final Set<CPodpojo> l_po = p.getPods();
            l_po.forEach( d -> l_pods.add( d ) );
        } );
        l_pods.forEach( p -> m_pods.add( new CPod( p, 0 ) ) );
        m_unit = new CUnits( 1, 10 );
        m_time = 0;
    }

    /**
     * testing the optimiser
     * @param p_pods pods
     * @param p_origin origin node
     * @throws GRBException gurobi
     */
    @Test
    public void runOptimiser( final ArrayList<CPod> p_pods, final Integer p_origin, final Integer p_time ) throws GRBException
    {
        m_opt = new CPodsSPP( m_env, p_origin, m_pods, p_time );
        m_opt.solve();
        m_opt.display();
        m_opt.getCosts();
    }

    /**
     * testing pod movement
     */
    @Test
    public void movement()
    {
        final HashMap<CPod, List<IEdge>> l_routes = new HashMap<>();
        m_pods.forEach( p -> l_routes.put( p, m_env.route( p.origin(), p.destination() ) ) );
        final AtomicReference<Integer> l_time = new AtomicReference<>( 0 );
        final HashMap<CPod, String> l_status = new HashMap<>();
        m_pods.forEach( p -> l_status.put( p, "Incomplete" ) );

        while ( l_status.containsValue( "Incomplete" ) )
        {
            m_pods.forEach( p ->
            {
                if ( l_routes.get( p ).isEmpty() ) l_status.put( p, "Complete" );
                else
                {
                    final IEdge l_edge = l_routes.get( p ).iterator().next();
                    if ( p.position().equals( 0.0 ) ) p.departed( l_edge, l_time.get() );
                    if ( p.position() < l_edge.length() ) p.move( m_unit );
                    else
                    {
                        p.arrived( l_edge, l_time.get() );
                        l_routes.get( p ).remove( 0 );
                    }
                }
            } );
            l_time.getAndSet( l_time.get() + 1 );
        }
    }

    /**
     * main
     * @param p_args cli
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCTotalSPP l_test = new TestCTotalSPP();
        l_test.init();
        l_test.runOptimiser( l_test.m_pods, 0, l_test.m_time );
    }
}
