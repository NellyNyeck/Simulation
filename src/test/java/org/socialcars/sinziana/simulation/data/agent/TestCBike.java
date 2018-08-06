package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/8-3x3.json" ), CInputpojo.class );
        final List<CBicyclepojo> l_bikes = l_configuration.getBikes();
        m_bike = new CBike( l_bikes.get( 0 ) );
    }
}
