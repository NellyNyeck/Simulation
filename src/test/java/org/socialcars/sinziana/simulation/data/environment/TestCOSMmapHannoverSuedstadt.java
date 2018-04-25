package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CDemandpojo;
import org.socialcars.sinziana.simulation.environment.demand.CInstance;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * test class for hannover suedstadt
 */

public class TestCOSMmapHannoverSuedstadt
{
    private static final CDemandpojo INPUT;

    private static final int ROUTENUMBER = 104718;
    private COSMEnvironment m_env;
    private Set<GeoPosition> l_fixed;


    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/HSVN.json" ), CDemandpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/niedersachsen-latest.osm.pbf", "src/test/Hannover", 52.373400, 52.346500,  9.77800, 9.746206 );
        l_fixed = new HashSet<>();
        INPUT.getDemand().forEach( j -> l_fixed.add( new GeoPosition( j.getFrom().getLat(), j.getFrom().getLon() ) ) );
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
     * testing heat
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

    /**
     * main funt
     * @param p_args cli arg
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMmapHannoverSuedstadt l_test = new TestCOSMmapHannoverSuedstadt();
        l_test.init();
        //l_test.route();
        l_test.heat();
    }
}
