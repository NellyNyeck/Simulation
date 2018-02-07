package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * test class for environment
 */
public final class TestCJungEnvironment
{
    private static CInputpojo s_input;

    static
    {
        try
        {
            s_input = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    private CJungEnvironment m_env;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new CJungEnvironment( s_input.getGraph() );
    }

    /**
     * graph output
     */
    @Test
    public void graph()
    {
        System.out.println( m_env );
    }

    /**
     * heatmap
     */
    @Test
    public void heatmap()
    {
        final Map<String, Integer> l_heatmap = new HashMap<>();

        IntStream.range( 0, 1000 )
                 .boxed()
                 .flatMap( i -> m_env.route( m_env.randomnodebyname(), m_env.randomnodebyname() ).stream() )
                 .forEach( i -> l_heatmap.put( i.id(), l_heatmap.getOrDefault( i.id(), 0 ) + 1 ) );

        System.out.println( l_heatmap );
    }

    /**
     * main
     *
     * @param p_args cli arguments
     */
    public static void main( final String[] p_args )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        l_frame.setSize( new Dimension( 900, 900 ) );
        l_frame.getContentPane().add( new CJungEnvironment( s_input.getGraph() ).panel( l_frame.getSize() ) );
        l_frame.setVisible( true );
    }

}
