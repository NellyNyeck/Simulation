package org.socialcars.sinziana.simulation.data.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * test class for agent movement
 */
public class TestCMovementJungSingle
{
    private CPod m_pod;
    private CJungEnvironment m_env;
    private CUnits m_unit;


    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        final CInputpojo l_configuration = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5HtoL.json" ), CInputpojo.class );
        final Set<CPodpojo> l_pods = l_configuration.getProviders().get( 0 ).getPods();
        m_env = new CJungEnvironment( l_configuration.getGraph() );
        l_pods.forEach( p -> m_pod = new CPod( p, 0 ) );
        m_unit = new CUnits( 1, 10 );
    }

    /**
     * testing pod movement
     */
    @Test
    public void movement()
    {
        final List<IEdge> l_route = m_env.route( m_pod.location(), m_pod.destination() );
        final INode l_start = m_env.getNodebyName( m_pod.location() );
        final AtomicReference<Integer> l_time = new AtomicReference<>( 0 );

        l_route.forEach( e ->
        {
            m_pod.departed( e, l_time.get() );
            while ( m_pod.position() < e.length() )
            {
                m_pod.move( m_unit );
                l_time.getAndSet( l_time.get() + 1 );
            }
            m_pod.arrived( e, l_time.get() );
            System.out.println( "Pod " + m_pod.name() + " arrived at node " + e.to().id() + " at timestep  " + l_time.get() );
        } );
    }
}
