package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.optimiser.CSPP;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;


/**
 * test class for single shortest path optimisation
 */
public class TestCSPP
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;
    private CSPP m_opt;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/model_opt.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_opt = new CSPP();
    }

    @Test
    public void routeJung()
    {
        m_opt.solveJung( 0, 14, m_env );
    }

    /**
     * main function
     * @param p_args cli
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCSPP l_test = new TestCSPP();
        l_test.init();
        l_test.routeJung();
    }
}
