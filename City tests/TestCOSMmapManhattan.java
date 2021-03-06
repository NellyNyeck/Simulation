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

/**
 * test class for manhattan
 */
public class TestCOSMmapManhattan
{
    private static final int ROUTENUMBER = 4090;
    private COSMEnvironment m_env;

    /**
     * init
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/new-york-latest.osm.pbf", "src/test/Manhattan", 40.873161, 40.701797,  -73.963944, -74.008104  );

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
     * heat
     * @throws IOException file
     */
    @Test
    public void heat() throws IOException
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
     * main
     * @param p_args cli
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMmapManhattan l_test = new TestCOSMmapManhattan();
        l_test.init();
        //l_test.paintOne();
        //l_test.route();
        l_test.heat();

    }
}
