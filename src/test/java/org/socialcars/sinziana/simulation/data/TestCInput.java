package org.socialcars.sinziana.simulation.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CAgent;
import org.socialcars.sinziana.simulation.data.input.CInput;

import java.io.File;
import java.io.IOException;
import java.util.Map;


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
        Assert.assertTrue( m_configuration.getConfiguration().getLengthUnit().contentEquals( "meter" ) );
        Assert.assertTrue( m_configuration.getConfiguration().getSpeedUnit().contentEquals( "m/s" ) );
        Assert.assertTrue( m_configuration.getConfiguration().getTimeUnit().contentEquals( "second" ) );
        Assert.assertTrue( m_configuration.getConfiguration().getNumberOfWaitingClients() == 1 );
        Assert.assertTrue( m_configuration.getConfiguration().getNumberOfBikes() == 0 );
        Assert.assertTrue( m_configuration.getConfiguration().getNumberOfHumans() == 0 );
        Assert.assertTrue( m_configuration.getConfiguration().getNumberOfVehicles() == 0 );
        Assert.assertTrue( m_configuration.getConfiguration().getAdditionalProperties().size() == 0 );
    }

    /**
     * test scenario agents
     */
    @Test
    public void testagents()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getAgents() );
        Assert.assertTrue( m_configuration.getAgents().size() == 1 );
        final CAgent l_prov = m_configuration.getAgents().get( 0 );
        Assert.assertNotNull( l_prov );
        Assert.assertTrue( l_prov.getName().contentEquals( "DHL" ) );
        Assert.assertTrue( l_prov.getFilename().contentEquals( "" ) );
        final Map<String, Object> l_extra = l_prov.getAdditionalProperties();
        Assert.assertNotNull( l_extra.get( "agent-type" ) );
        Assert.assertTrue( l_extra.get( "agent-type" ).equals( "provider" ) );
        Assert.assertNotNull( l_extra.get( "colour" ) );
        Assert.assertTrue( l_extra.get( "colour" ).equals( "yellow" ) );
        Assert.assertNotNull( l_extra.get( "available pods" ) );
        Assert.assertTrue( l_extra.get( "available pods" ).equals( 1 ) );
        Assert.assertNotNull( l_extra.get( "maximum number of customers" ) );
        Assert.assertTrue( l_extra.get( "maximum number of customers" ).equals( 1 ) );
        Assert.assertNotNull( l_extra.get( "maximum outgoing pods/time unit" ) );
        Assert.assertTrue( l_extra.get( "maximum outgoing pods/time unit" ).equals( 1 ) );
        //final List<>l_depots = l_extra.get( "depots" );

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
