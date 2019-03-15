package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/8-3x3.json" ), CInputpojo.class );
        final List<CHumanpojo> l_humans = l_configuration.getHumans();
        m_human = new CHuman( l_humans.get( 0 ), 0 );
    }

}
