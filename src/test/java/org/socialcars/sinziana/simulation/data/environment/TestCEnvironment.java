package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.CEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * test class for environment
 */
public final class TestCEnvironment
{
    private CEnvironment m_env;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new CEnvironment( new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class ).getGraph() );
    }

    @Test
    public void graph()
    {
        System.out.println( m_env );
    }

    @Test
    public void heatmap()
    {
        final Map<String, Integer> l_heatmap = new HashMap<>();

        IntStream.range( 0, 1000 )
                 .boxed()
                 .flatMap( i -> m_env.route( m_env.randomnode(), m_env.randomnode() ).stream() )
                 .forEach( i -> l_heatmap.put( i.id(), l_heatmap.getOrDefault( i.id(), 0 ) + 1 ) );

        System.out.println( l_heatmap );
    }
}
