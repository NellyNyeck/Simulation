package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.optimiser.CPSPP;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;

public class TestCPlatoonSPP
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;
    private CPSPP m_opt;
    private ArrayList<Integer> m_destinations;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_weights.json" ), CInputpojo.class );
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
        /*m_destinations.add( 99 );
        m_destinations.add( 80 );
        m_destinations.add( 77 );
        m_destinations.add( 61 );
        m_destinations.add( 58 );
        m_destinations.add( 42 );
        m_destinations.add( 37 );
        m_destinations.add( 26 );
        m_destinations.add( 14 );
        m_destinations.add( 3 );*/;
        m_destinations.add(169);
        m_opt = new CPSPP( m_env, m_destinations );
    }

    /**
     * testing the jung solver
     * @throws  GRBException exception
     */
    @Test
    public void routeJung() throws GRBException
    {
        m_opt.solve(  m_env, 1, m_destinations );
    }

    /**
     * main function
     * @param p_args cli
     * @throws IOException file
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws IOException, GRBException
    {
        final TestCPlatoonSPP l_test = new TestCPlatoonSPP();
        l_test.init();
        l_test.routeJung();
    }
}
