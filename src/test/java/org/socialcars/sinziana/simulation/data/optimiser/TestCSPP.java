package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
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
     * initialisation
     * @throws IOException file
     * @throws GRBException gurobi
     */
    @Before
    public void init() throws IOException, GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_opt = new CSPP( m_env );
    }

    @Test
    public void routeJung() throws GRBException
    {
        m_opt.solveJung( 0, 70, m_env );
    }

    /**
     * main functions
     * @param p_args cli args
     * @throws IOException file
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws IOException, GRBException
    {
        final TestCSPP l_test = new TestCSPP();
        l_test.init();
        l_test.routeJung();
    }
}
