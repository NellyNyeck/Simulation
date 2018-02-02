package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CHumanpojo;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.elements.CHuman;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * the human test class
 */
public class TestCHuman
{
    private CHuman m_human;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
        final List<CHumanpojo> l_humans = l_configuration.getHumans();
        m_human = new CHuman( l_humans.get( 0 ) );
    }

    /**
     * testing name
     */
    @Test
    public void name()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getName().contains( "human" ) );
    }

    /**
     * testing max accel
     */
    @Test
    public void maxaccel()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getMaxAccel().equals( 0.0 ) );
    }

    /**
     * testing maxdecel
     */
    @Test
    public void maxdecel()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getMaxDeccel().equals( 0.0 ) );
    }

    /**
     * testing maxspeed
     */
    @Test
    public void maxspeed()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getMaxSpeed().equals( 0.0 ) );
    }

    /**
     * testing speed
     */
    @Test
    public void speed()
    {
        Assume.assumeNotNull( m_human );
        m_human.setSpeed( 8 );
        Assert.assertTrue( m_human.getSpeed().equals( 8 ) );
    }

    /**
     * testing location
     */
    @Test
    public void location()
    {
        Assume.assumeNotNull( m_human );
        m_human.setLocation( 10 );
        Assert.assertTrue( m_human.getLocation().equals( 10 ) );
    }

    /**
     * testing start
     */
    @Test
    public void start()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getStart().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_human.getStart().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_human.getStart().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing finish
     */
    @Test
    public void finish()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getFinish().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_human.getFinish().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_human.getFinish().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing middle
     */
    @Test
    public void middle()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertNull( m_human.getMiddle() );
    }

    /**
     * testing filename
     */
    @Test
    public void filename()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getFilename().isEmpty() );
    }

    /**
     * testing agenttype
     */
    @Test
    public void agenttype()
    {
        Assume.assumeNotNull( m_human );
        Assert.assertTrue( m_human.getAgentType().contentEquals( "human" ) );
    }

}
