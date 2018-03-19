package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestCOSMmapNewDehli
{
    private static final int ROUTENUMBER = 2135;
    private COSMEnvironment m_env;

    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/india-latest.osm.pbf", "src/test/India", 28.737985, 28.569368,  77.250663,  77.124005 );
    }

    /**
     * testing the random position
     */
    @Test
    public void randomPosition()
    {
        final GeoPosition l_newnode = m_env.randomnode();
        Assert.assertNotNull( l_newnode );
    }

    /**
     * testing the routing visualization
     */
    @Test
    public void route()
    {
        Assume.assumeNotNull( m_env );
        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        IntStream.range( 0, ROUTENUMBER )
            .boxed()
            .forEach( i -> l_routes.add( m_env.route( m_env.randomnode(), m_env.randomnode(), Stream.empty() ) ) );
        m_env.drawRoutes( l_routes );
    }

    /**
     * testing the heat visualization
     */
    @Test
    public void heat() throws IOException {
        Assume.assumeNotNull( m_env );
        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        IntStream.range( 0, ROUTENUMBER )
            .boxed()
            .forEach( i -> l_routes.add( m_env.route( m_env.randomnode(), m_env.randomnode(), Stream.empty() ) ) );
        m_env.drawHeat( l_routes );
    }

    /**
     * main function
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMmapNewDehli l_test = new TestCOSMmapNewDehli();
        l_test.init();

        /*GeoPosition l_start = new GeoPosition(44.425946, 26.149483 );
        GeoPosition l_finish = new GeoPosition( 44.428458, 26.146017 );
        l_test.m_env.routeOne( l_start, l_finish );
        l_test.m_env.drawRoutes( List.of( l_test.m_env.route( l_start, l_finish, Stream.empty() ) ) );*/
        //l_test.route();
        l_test.heat();
    }
}
