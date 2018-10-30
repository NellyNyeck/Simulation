package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.optimiser.CPSPP;


import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


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
        final LinkedHashMap<INode, Integer> l_nodes = m_env.nodesPop();
        final INode l_origin = l_nodes.keySet().iterator().next();
        m_destinations = new ArrayList<>();
        final List<Map.Entry<INode, Integer>> entries = new ArrayList<Map.Entry<INode, Integer>>( l_nodes.entrySet() );
        IntStream.range( entries.size() - 10, entries.size() ).boxed().forEach( i -> m_destinations.add( Integer.valueOf( entries.get( i ).getKey().id() ) ) );
        //IntStream.range( 0, 10 ).boxed().forEach( i -> m_destinations.add( ThreadLocalRandom.current().nextInt( 1, 362 ) ) );
        m_opt = new CPSPP( m_env, Integer.valueOf( l_origin.id() ), m_destinations );
    }

    /**
     * testing the jung solver
     * @throws  GRBException exception
     */
    @Test
    public void routeJung() throws GRBException
    {
        m_opt.solve();
        m_opt.display();
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
