package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.socialcars.sinziana.simulation.data.input.CInput;
import org.socialcars.sinziana.simulation.environment.CEnvironment;
import org.socialcars.sinziana.simulation.environment.CIntersection;
import org.socialcars.sinziana.simulation.environment.CStreet;

import java.io.File;
import java.io.IOException;

/**
 * test class for environment
 */
public final class TestCEnvironment
{
    private CEnvironment<String, CIntersection, CStreet> m_env;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInput l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInput.class );
        m_env = new CEnvironment<>( l_configuration.getGraph() );
    }
}
