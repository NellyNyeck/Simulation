package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * testing movement with time for more pods
 */
public class TestCMovementJungMultiple
{
    private ArrayList<CPod> m_pods = new ArrayList<>();
    private CJungEnvironment m_env;
    private CUnits m_unit;


    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5HtoL.json" ), CInputpojo.class );
        final Set<CPodpojo> l_pods = new HashSet<>();
        l_configuration.getProviders().forEach( p ->
        {
            final Set<CPodpojo> l_po = p.getPods();
            l_po.forEach( d -> l_pods.add( d ) );
        } );
        m_env = new CJungEnvironment( l_configuration.getGraph() );
        l_pods.forEach( p -> m_pods.add( new CPod( p, 0 ) ) );
        m_unit = new CUnits( 1, 10 );
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
}
