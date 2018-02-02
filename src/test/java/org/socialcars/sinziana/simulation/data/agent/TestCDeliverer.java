package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInput;
import org.socialcars.sinziana.simulation.elements.CDeliverer;

import java.io.File;
import java.io.IOException;

/**
 * test class for deliverer
 */
public class TestCDeliverer
{
    private CDeliverer m_del;

    /**
     * initializing
     * @throws IOException cuz file
     */
    @Before
    public void init() throws IOException
    {
        final CInput l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInput.class );
        m_del = new CDeliverer( l_configuration.getProviders().get( 0 ) );
    }

    /**
     * test max clients
     */
    @Test
    public void maxclients()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.maxClients().equals( 1 ) );
    }

    /**
     * testing maxpodtime
     */
    @Test
    public void maxPodsTime()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.maxPodsTime().equals( 1.0 ) );
    }

    /**
     * testing location
     */
    @Test
    public void location()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.location().name().contentEquals( "node0" ) );
    }

    /**
     * testing the colour
     */
    @Test
    public void colour()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.colour().contentEquals( "yellow" ) );
    }

    /**
     * testing the name
     */
    @Test
    public void name()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.getName().contentEquals( "DHL" ) );
    }

    /**
     * testing the filename
     */
    @Test
    public void filename()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.getFilename().isEmpty() );
    }

    /**
     * testing the agenttype
     */
    @Test
    public void agenttype()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.getAgentType().contentEquals( "provider" ) );
    }

    /**
     * testing the depot
     */
    @Test
    public void depots()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.depots().size() == 1 );
        Assert.assertTrue( m_del.depots().get( 0 ).name().contentEquals( "node0" ) );
    }

    /**
     * testing the pods
     */
    @Test
    public void pods()
    {
        Assume.assumeNotNull( m_del );
        Assert.assertTrue( m_del.pods().size() == 1 );
        Assert.assertTrue( m_del.pods().get( 0 ).getName().contentEquals( "pod1" ) );
    }
}
