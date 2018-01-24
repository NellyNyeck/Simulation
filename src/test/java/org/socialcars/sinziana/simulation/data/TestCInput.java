package org.socialcars.sinziana.simulation.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInput;

import java.io.File;
import java.io.IOException;


/**
 * unit-test for data input
 */
public final class TestCInput
{
    /**
     * configuration
     */
    private CInput m_configuration;

    /**
     * initialize configuration
     *
     * @throws IOException on example reading error
     */
    @Before
    public final void init() throws IOException
    {
        m_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInput.class );
    }


    /**
     * test scenario configuration
     */
    @Test
    public void configuration()
    {
        Assume.assumeNotNull( m_configuration );

        Assert.assertNotNull( m_configuration.getConfiguration() );
        // @todo test configuration items
    }

    /**
     * test scenario agents
     */
    public void testagents()
    {
        Assume.assumeNotNull( m_configuration );

        Assert.assertNotNull( m_configuration.getAgents() );
        // @todo test agent data
    }


    /**
     * test scenario graph
     */
    public void testgraph()
    {
        Assume.assumeNotNull( m_configuration );

        Assert.assertNotNull( m_configuration.getGraph() );
        // @todo test graph data
    }

}
