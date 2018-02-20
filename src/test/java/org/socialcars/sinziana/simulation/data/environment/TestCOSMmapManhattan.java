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

public class TestCOSMmapManhattan
{
    private static final int ROUTENUMBER = 10000;
    private COSMEnvironment m_env;

    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/new-york-latest.osm.pbf", "src/test/Manhattanbike", 40.876488, 40.688021,  -73.925974,  -74.009709 );
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
    public void heat()
    {
        Assume.assumeNotNull( m_env );
        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        IntStream.range( 0, ROUTENUMBER )
            .boxed()
            .forEach( i -> l_routes.add( m_env.route( m_env.randomnode(), m_env.randomnode(), Stream.empty() ) ) );
        m_env.drawHeat( l_routes );
    }

    public void paintOne()
    {
        m_env.drawRoutes( List.of( m_env.route( m_env.randomnode(), m_env.randomnode(), Stream.empty() ) ) );
    }

    /**
     * main function
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMmapManhattan l_test = new TestCOSMmapManhattan();
        l_test.init();
        l_test.paintOne();

    }
}
