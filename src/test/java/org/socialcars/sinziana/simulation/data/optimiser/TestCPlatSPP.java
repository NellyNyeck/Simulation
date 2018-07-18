package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
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
 * test class for solver
 */
public class TestCPlatSPP
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;
    private CPlatSPP m_opt;
    private ArrayList<Integer> m_destinations;

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
     * @throws GRBException exception
     */
    @Before
    public void init() throws IOException, GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_destinations = new ArrayList();
        m_destinations.add( 99 );
        m_destinations.add( 80 );
        m_destinations.add( 77 );
        m_destinations.add( 61 );
        m_destinations.add( 58 );
        m_destinations.add( 42 );
        m_destinations.add( 37 );
        m_destinations.add( 26 );
        m_destinations.add( 14 );
        m_destinations.add( 3 );
        m_opt = new CPlatSPP( m_env, m_destinations );
    }

    /**
     * testing the jung solver
     * @throws  GRBException exception
     */
    @Test
    public void routeJung() throws GRBException
    {
        m_opt.solveJung( 0, m_destinations, m_env );
    }

    /**
     * main function
     * @param p_args cli
     * @throws IOException file
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws IOException, GRBException
    {
        final TestCPlatSPP l_test = new TestCPlatSPP();
        l_test.init();
        l_test.routeJung();
    }
}
