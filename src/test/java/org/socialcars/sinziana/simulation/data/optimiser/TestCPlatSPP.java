package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.optimiser.CPlatSPP;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;


/**
 * test class for single shortest path optimisation
 */
public class TestCPlatSPP
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;
    private CPlatSPP m_opt;

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
        m_opt = new CPlatSPP();
    }

    /**
     * testing the jung solver
     */
    @Test
    public void routeJung()
    {
        final ArrayList<Integer> l_destinations = new ArrayList();
        l_destinations.add( 99 );
        l_destinations.add( 80 );
        l_destinations.add( 77 );
        l_destinations.add( 61 );
        l_destinations.add( 58 );
        l_destinations.add( 42 );
        l_destinations.add( 37 );
        l_destinations.add( 26 );
        l_destinations.add( 14 );
        l_destinations.add( 3 );
        m_opt.solveJung( 0, l_destinations, m_env );
    }

    /**
     * main function
     * @param p_args cli
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCPlatSPP l_test = new TestCPlatSPP();
        l_test.init();
        l_test.routeJung();
    }
}
