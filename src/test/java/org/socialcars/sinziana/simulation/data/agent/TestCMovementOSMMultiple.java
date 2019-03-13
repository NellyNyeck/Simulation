package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

/**
 * testing movement with multiple pods in a real environment
 */
public class TestCMovementOSMMultiple
{
    private ArrayList<CPod> m_pods = new ArrayList<>();
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
        final Set<CPodpojo> l_pods = new HashSet<>();
        l_configuration.getProviders().forEach( p ->
        {
            final Set<CPodpojo> l_po = p.getPods();
            l_po.forEach( d -> l_pods.add( d ) );
        } );
        l_pods.forEach( p -> m_pods.add( new CPod( p, 0 ) ) );
        m_unit = new CUnits( 1, 10 );
    }

    /**
     * testing movement
     */
    @Test
    public void movement()
    {
        final HashMap<CPod, List<GeoPosition>> l_routes = new HashMap<>();
        Assert.assertTrue( m_pods.size() == 3 );
        l_routes.put( m_pods.get( 0 ), m_env.route( new GeoPosition( 41.412895, 2.184461 ), new GeoPosition( 41.413039708428975, 2.182420391941526 ), Stream.empty() ) );
        l_routes.put( m_pods.get( 1 ), m_env.route( new GeoPosition( 41.412895, 2.184461 ), new GeoPosition( 41.413424530925404, 2.181158822353754 ), Stream.empty() ) );
        l_routes.put( m_pods.get( 2 ), m_env.route( new GeoPosition( 41.409428, 2.174316 ), new GeoPosition( 41.412031272322636, 2.175443854379144 ), Stream.empty() ) );
        final AtomicReference<Integer> l_time = new AtomicReference<>( 0 );

        final HashMap<CPod, String> l_status = new HashMap<>();
        m_pods.forEach( p -> l_status.put( p, "Incomplete" ) );

        final HashMap<CPod, GeoPosition> l_lasts = new HashMap<>();
        m_pods.forEach( p ->
        {
            l_lasts.put( p, l_routes.get( p ).iterator().next() );
            l_routes.get( p ).remove( 0 );
        } );

        while ( l_status.containsValue( "Incomplete" ) )
        {
            m_pods.forEach( p ->
            {
                if ( l_routes.get( p ).isEmpty() ) l_status.put( p, "Complete" );
                else
                {
                    final GeoPosition l_pos = l_routes.get( p ).iterator().next();
                    if ( p.position().equals( 0.0 ) ) p.departed( l_lasts.get( p ), l_time.get() );
                    if ( p.position() < m_env.sectionLength( l_lasts.get( p ), l_pos ) ) p.move( m_unit );
                    else
                    {
                        System.out.println( "Pod " + p.name() + " : " + m_env.sectionLength( l_lasts.get( p ), l_pos ) );
                        p.arrived( l_pos, l_time.get() );
                        l_lasts.replace( p, l_pos );
                        l_routes.get( p ).remove( 0 );
                    }
                }
            } );
            l_time.getAndSet( l_time.get() + 1 );
        }

    }
}
