package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * test class for podmvn
 *
 */
public class TestCPOD
{

    private CPod m_pod;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/8-3x3.json" ), CInputpojo.class );
        final Set<CPodpojo> l_pods = l_configuration.getProviders().get( 0 ).getPods();
        l_pods.forEach( p -> m_pod = new CPod( p, 0 ) );
    }

    @Test
    public void testPosition()
    {
        System.out.println( m_pod.position() );
    }


    @Test
    public void testDestination()
    {
        System.out.println( m_pod.destination() );
    }

}
