package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.elements.CProvider;

import java.io.File;
import java.io.IOException;

/**
 * test class for deliverer
 */
public class TestCDeliverer
{
    private CProvider m_del;

    /**
     * initializing
     * @throws IOException cuz file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
        m_del = new CProvider( l_configuration.getProviders().get( 0 ) );
    }
}
