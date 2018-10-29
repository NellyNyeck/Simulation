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


/**
 * test class for the platooning optimisation problem
 */
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
     * @throws GRBException exception
     */
    @Before
    public void init() throws GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_destinations = new ArrayList<>();
        //IntStream.range(0, 10).boxed().forEach( i -> m_destinations.add( ThreadLocalRandom.current().nextInt( 1, 362 ) ) );
        /*m_destinations.add(32);
        m_destinations.add(52);
        m_destinations.add(9);
        m_destinations.add(78);
        m_destinations.add(287);
        m_destinations.add(269);
        m_destinations.add(340);
        m_destinations.add(191);
        m_destinations.add(167);
        m_destinations.add(337);*/
        m_destinations.add( 169 );
        m_destinations.add( 164 );
        m_opt = new CPSPP( m_env, 1, m_destinations );
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

    /**
     * main function
     * @param p_args cli
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCPlatoonSPP l_test = new TestCPlatoonSPP();
        l_test.init();
        l_test.routeJung();
    }
}
