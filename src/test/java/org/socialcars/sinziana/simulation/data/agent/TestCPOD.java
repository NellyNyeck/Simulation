package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInput;
import org.socialcars.sinziana.simulation.data.input.CPod;
import org.socialcars.sinziana.simulation.elements.CPOD;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * test class for podmvn
 *
 */
public class TestCPOD
{

    private CPOD m_pod;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInput l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInput.class );
        final Set<CPod> l_pods = l_configuration.getProviders().get( 0 ).getPods();
        l_pods.stream().forEach( p ->
        {
            m_pod = new CPOD( p );
        } );
    }

    /**
     * testing name
     */
    @Test
    public void name()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getName().contains( "pod" ) );
    }

    /**
     * testing max accel
     */
    @Test
    public void maxaccel()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getMaxAccel().equals( 0.5 ) );
    }

    /**
     * testing maxdecel
     */
    @Test
    public void maxdecel()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getMaxDeccel().equals( 0.3 ) );
    }

    /**
     * testing maxspeed
     */
    @Test
    public void maxspeed()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getMaxSpeed().equals( 1.0 ) );
    }

    /**
     * testing speed
     */
    @Test
    public void speed()
    {
        Assume.assumeNotNull( m_pod );
        m_pod.setSpeed( 8 );
        Assert.assertTrue( m_pod.getSpeed().equals( 8 ) );
    }

    /**
     * testing location
     */
    @Test
    public void location()
    {
        Assume.assumeNotNull( m_pod );
        m_pod.setLocation( 10 );
        Assert.assertTrue( m_pod.getLocation().equals( 10 ) );
    }

    /**
     * testing start
     */
    @Test
    public void start()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getStart().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_pod.getStart().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_pod.getStart().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing finish
     */
    @Test
    public void finish()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getFinish().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_pod.getFinish().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_pod.getFinish().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing middle
     */
    @Test
    public void middle()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertNull( m_pod.getMiddle() );
    }

    /**
     * testing filename
     */
    @Test
    public void filename()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getFilename().isEmpty() );
    }

    /**
     * testing agenttype
     */
    @Test
    public void agenttype()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getAgentType().contentEquals( "pod" ) );
    }

    /**
     * testing provider
     */
    @Test
    public void provider()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getProvider().contentEquals( "DHL" ) );
    }

    /**
     * testing capacity
     */
    @Test
    public void capacity()
    {
        Assume.assumeNotNull( m_pod );
        Assert.assertTrue( m_pod.getCapacity().equals( 1 ) );
    }
}
