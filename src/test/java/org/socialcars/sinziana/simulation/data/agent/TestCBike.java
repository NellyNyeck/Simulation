package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.elements.CBike;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * the bike test class
 */
public class TestCBike
{
    private CBike m_bike;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
        final List<CBicyclepojo> l_bikes = l_configuration.getBikes();
        m_bike = new CBike( l_bikes.get( 0 ) );
    }

    /**
     * testing name
     */
    @Test
    public void name()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getName().contains( "bike" ) );
    }

    /**
     * testing max accel
     */
    @Test
    public void maxaccel()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getMaxAccel().equals( 0.0 ) );
    }

    /**
     * testing maxdecel
     */
    @Test
    public void maxdecel()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getMaxDeccel().equals( 0.0 ) );
    }

    /**
     * testing maxspeed
     */
    @Test
    public void maxspeed()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getMaxSpeed().equals( 0.0 ) );
    }

    /**
     * testing speed
     */
    @Test
    public void speed()
    {
        Assume.assumeNotNull( m_bike );
        m_bike.setSpeed( 8 );
        Assert.assertTrue( m_bike.getSpeed().equals( 8 ) );
    }

    /**
     * testing location
     */
    @Test
    public void location()
    {
        Assume.assumeNotNull( m_bike );
        m_bike.setLocation( 10 );
        Assert.assertTrue( m_bike.getLocation().equals( 10 ) );
    }

    /**
     * testing start
     */
    @Test
    public void start()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getStart().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_bike.getStart().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_bike.getStart().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing finish
     */
    @Test
    public void finish()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getFinish().name().contentEquals( "node0" ) );
        Assert.assertTrue( m_bike.getFinish().coord().getFirstCoordinate() == 0 );
        Assert.assertTrue( m_bike.getFinish().coord().getSecondCoordinate() == 0 );
    }

    /**
     * testing middle
     */
    @Test
    public void middle()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertNull( m_bike.getMiddle() );
    }

    /**
     * testing filename
     */
    @Test
    public void filename()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getFilename().isEmpty() );
    }

    /**
     * testing agenttype
     */
    @Test
    public void agenttype()
    {
        Assume.assumeNotNull( m_bike );
        Assert.assertTrue( m_bike.getAgentType().contentEquals( "bike" ) );
    }

}
