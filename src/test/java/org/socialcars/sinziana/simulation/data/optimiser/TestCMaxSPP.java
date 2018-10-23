package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.optimiser.CMaxPSPP;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class TestCMaxSPP
{
    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;
    private CMaxPSPP m_opt;
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
     * @throws GRBException exception
     */
    @Before
    public void init() throws GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_destinations = new ArrayList<>();
        // m_destinations.add( 99 );
        //m_destinations.add( 80 );
        //m_destinations.add( 77 );
        //m_destinations.add( 61 );
        //m_destinations.add( 58 );
        //m_destinations.add( 42 );
        //m_destinations.add( 37 );
        //m_destinations.add( 26 );
        //m_destinations.add( 164 );
        m_destinations.add( 169 );
        //m_destinations.add( 164 );
        m_opt = new CMaxPSPP( m_env, 1, m_destinations );
    }

    /**
     * testing the jung solver
     * @throws  GRBException exception
     */
    @Test
    public void routeJung() throws GRBException
    {
        m_opt.solve();
    }

    @Test
    public void adjmat()
    {
        IntStream.range( 0, m_env.size() + 1 ).boxed().forEach(i ->
        {
            IntStream.range( 0, m_env.size() + 1 ).boxed().forEach( j ->
            {

            } );
        } );

    }

    /**
     * main function
     * @param p_args cli
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCMaxSPP l_test = new TestCMaxSPP();
        l_test.init();
        l_test.routeJung();
    }
}
