package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CDemandspojo;
import org.socialcars.sinziana.simulation.environment.demand.CInstance;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * test class for hannover suedstadt with real traffic demand
 */
public class TestCOSMHsvn
{
    private static final CDemandspojo INPUT;

    private ArrayList<CInstance> m_demand;
    private COSMEnvironment m_env;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/HSVN.json" ), CDemandspojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initialising the class
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/niedersachsen-latest.osm.pbf", "src/test/Hannover", 52.373400, 52.346500,  9.77800, 9.746206 );
        m_demand = new ArrayList<>();
        INPUT.getDemand().forEach( j ->
        {
            final CInstance l_new = new CInstance( j );
            m_demand.add( l_new );
        } );
    }

    /**
     * testing heat
     * @throws IOException file
     */
    @Test
    public void heat() throws IOException
    {
        final AtomicInteger l_nb = new AtomicInteger();
        Assume.assumeNotNull( m_env );
        Assume.assumeNotNull( m_demand );
        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        m_demand.forEach( j ->
        {
            l_nb.set( l_nb.get() + j.howMany() );
            IntStream.range( 0, j.howMany() )
                .boxed()
                .forEach( i -> l_routes.add( m_env.route( new GeoPosition( j.from().latitude(), j.from().longitude() ),
                    new GeoPosition( j.to().latitude(), j.to().longitude() ), Stream.empty() ) ) );
        } );
        m_env.drawHeat( l_routes );
        System.out.println( l_nb );
    }

    /**
     * testing multiple routes through routing with middle points
     * @throws IOException file
     */
    public void routeMultiple() throws IOException
    {
        Assume.assumeNotNull( m_env );
        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        final ArrayList<GeoPosition> l_destinations = new ArrayList<GeoPosition>();
        IntStream.range( 0, 10000 )
            .boxed()
            .forEach( i -> l_destinations.add( m_env.randomnode() ) );
        l_routes.add( m_env.route( m_env.randomnode(), m_env.randomnode(), l_destinations.stream() ) );
        m_env.drawHeat( l_routes );
    }

    /**
     * checks if the environment was propperly created
     * @throws IOException file
     */
    @Test
    public void check() throws IOException
    {
        Assume.assumeNotNull( m_env );
        System.out.println( m_env.getEdges().size() );
    }

    /**
     * main funt
     * @param p_args cli arg
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMHsvn l_test = new TestCOSMHsvn();
        l_test.init();
        //l_test.routeMultiple();
        //l_test.heat();
        //l_test.check();
    }
}
