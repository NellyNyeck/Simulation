package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.blocks.CJungBlock;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * test class for jung block environment
 */
public class TestCJungBlock
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_jungenv;
    private CJungBlock m_env;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     */
    @Before
    public void init()
    {
        m_jungenv = new CJungEnvironment( INPUT.getGraph() );
        m_env = new CJungBlock( m_jungenv, 1.00 );
    }

    /**
     * tests the sizes of the blocks environment
     */
    @Test
    public void testSize()
    {
        Assume.assumeNotNull( m_jungenv );
        Assume.assumeNotNull( m_env );
        m_env.returnsizes();
    }

    /**
     * main
     * @param p_args cli
     */
    public static void main( final String[] p_args )
    {
        final TestCJungBlock l_test = new TestCJungBlock();
        l_test.init();
        l_test.testSize();
    }
}
