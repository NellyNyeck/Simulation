package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;
import org.socialcars.sinziana.simulation.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * testing movement in an OSM environment
 */
public class TestCMovementOSMSingle
{
    private CPod m_pod;
    private COSMEnvironment m_env;
    private CUnits m_unit;

    /**
     * initialiser
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/spain-latest.osm.pbf", "src/test/Barcelona", 41.412895, 41.409428,  2.184461, 2.174316 );
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/BarcelonaSmallPods.json" ), CInputpojo.class );
        final Set<CPodpojo> l_pods = l_configuration.getProviders().get( 0 ).getPods();
        l_pods.forEach( p -> m_pod = new CPod( p, 0 ) );
        m_unit = new CUnits( 1, 10 );
    }

    /**
     * testing movement
     */
    @Test
    public void movement()
    {
        final List<GeoPosition> l_route = m_env.route( new GeoPosition( 41.412895, 2.184461 ), new GeoPosition( 41.409428, 2.174316 ), Stream.empty() );
        l_route.forEach( l -> System.out.println( l.toString() ) );
        final AtomicReference<Integer> l_time = new AtomicReference<>( 0 );
        final GeoPosition[] l_last = new GeoPosition[1];

        l_route.forEach( p ->
        {
            if ( l_last[0] == null ) l_last[0] = p;
            else
            {
                System.out.println( m_env.sectionLength( l_last[0], p ) );
                m_pod.departed( l_last[0], l_time.get() );
                while ( m_pod.position() < m_env.sectionLength( l_last[0], p ) )
                {
                    m_pod.move( m_unit );
                    l_time.getAndSet( l_time.get() + 1 );
                }
                m_pod.arrived( p, l_time.get() );
                l_last[0] = p;
            }
        } );

    }
}
