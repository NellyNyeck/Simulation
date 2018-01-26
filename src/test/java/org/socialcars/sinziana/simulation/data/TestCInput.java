package org.socialcars.sinziana.simulation.data;

import org.socialcars.sinziana.simulation.data.input.CInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.socialcars.sinziana.simulation.data.input.CAgent;
import org.junit.Assert;
import org.socialcars.sinziana.simulation.data.input.CEntryPoint;
import org.junit.Assume;
import org.socialcars.sinziana.simulation.data.input.CEdge;
import org.junit.Before;
import org.socialcars.sinziana.simulation.data.input.CGraph;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CFunction;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;


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
        /*final List<CEntryPoint> l_depots = (List<CEntryPoint>) l_extra.get( "depots" );
        Assert.assertNotNull( l_depots );
        Assert.assertTrue( l_depots.size() == 1 );
        final CEntryPoint l_depo = l_depots.get( 0 );
        Assert.assertNotNull( l_depo );
        Assert.assertTrue( l_depo.getName().contentEquals( "node0" ) );
        Assert.assertTrue( l_depo.getCoordinates().getType().contentEquals( "synthetic" ) );
        Assert.assertTrue( l_depo.getCoordinates().getFirstCoordinate() == 0.00 );
        Assert.assertTrue( l_depo.getCoordinates().getSecondCoordinate() == 0.00);*/
    }


    /**
     * test scenario graph
     */
    @Test
    public void testgraph()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getGraph() );
        final CGraph l_graph = m_configuration.getGraph();
        Assert.assertNotNull( l_graph.getNodes() );
        final Set<CEntryPoint> l_nodes = l_graph.getNodes();
        Assert.assertTrue( l_nodes.size() == 8 );

        // @todo warnings fixen
        // @todo Loop entfernen und durch Streams ersetzen


        l_nodes.forEach( j ->
        {
            Assert.assertTrue( j.getName().contains( "node" ) );
            Assert.assertTrue( j.getCoordinates().getType().contentEquals( "synthetic" ) );
            Assert.assertTrue( j.getCoordinates().getFirstCoordinate() % 5 == 0 );
            Assert.assertTrue( j.getCoordinates().getSecondCoordinate() % 5 == 0 );
            Assert.assertTrue( j.getAdditionalProperties().size() == 0 );

        } );
        Assert.assertNotNull( l_graph.getEdges() );
        Assert.assertTrue( l_graph.getEdges().size() == 20 );
        final Set<CEdge> l_edges = l_graph.getEdges();

        l_edges.forEach( e ->
        {
            Assert.assertTrue( e.getName().contains( "edge" ) );
            Assert.assertTrue( e.getFrom().contains( "node" ) );
            Assert.assertTrue( e.getTo().contains( "node" ) );
            Assert.assertTrue( ( e.getWeight() == 1.0 ) || ( e.getWeight() == 0.75 ) );
            Assert.assertNotNull( e.getFunction() );
            final CFunction l_funct = e.getFunction();
            Assert.assertTrue( l_funct.getName().equals( "even" )  );
            Assert.assertTrue( l_funct.getParameters().size() == 1 );
        } );
    }

}
