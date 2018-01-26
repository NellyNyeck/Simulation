package org.socialcars.sinziana.simulation.data;

import org.socialcars.sinziana.simulation.data.input.CAgent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.socialcars.sinziana.simulation.data.input.CParameter;
import org.junit.Assert;
import org.junit.Assume;
import org.socialcars.sinziana.simulation.data.input.CInput;
import org.junit.Before;
import org.socialcars.sinziana.simulation.data.input.CEdge;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CEntryPoint;
import org.socialcars.sinziana.simulation.data.input.CGraph;
import org.socialcars.sinziana.simulation.data.input.CFunction;
import org.socialcars.sinziana.simulation.data.input.CCoordinates;
import org.socialcars.sinziana.simulation.data.input.CConfiguration;


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
     * testing the whole input
     */
    @Test
    public void input()
    {
        Assert.assertTrue( m_configuration.equals( m_configuration ) );
        Assert.assertTrue( !m_configuration.toString().isEmpty() );
        Assert.assertTrue( m_configuration.getAdditionalProperties().size() == 0 );
        Assert.assertTrue( m_configuration.hashCode() != 0 );
    }

    /**
     * test scenario configuration
     */
    @Test
    public void configuration()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getConfiguration() );
        final CConfiguration l_config = m_configuration.getConfiguration();
        Assert.assertTrue( l_config.equals( l_config ) );
        Assert.assertTrue( l_config.getLengthUnit().contentEquals( "meter" ) );
        Assert.assertTrue( l_config.getSpeedUnit().contentEquals( "m/s" ) );
        Assert.assertTrue( l_config.getTimeUnit().contentEquals( "second" ) );
        Assert.assertTrue( l_config.getNumberOfWaitingClients() == 1 );
        Assert.assertTrue( l_config.getNumberOfBikes() == 0 );
        Assert.assertTrue( l_config.getNumberOfHumans() == 0 );
        Assert.assertTrue( l_config.getNumberOfVehicles() == 0 );
        Assert.assertTrue( l_config.getAdditionalProperties().size() == 0 );
        l_config.setNumberOfBikes( 1 );
        l_config.setNumberOfHumans( 1 );
        l_config.setNumberOfVehicles( 1 );
        l_config.setAdditionalProperty( "extra0", 1 );
        Assert.assertTrue( l_config.getAdditionalProperties().size() == 1 );
        Assert.assertTrue( l_config.getNumberOfVehicles() == 1 );
        Assert.assertTrue( l_config.getNumberOfHumans() == 1 );
        Assert.assertTrue( l_config.getNumberOfBikes() == 1 );
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
        Assert.assertTrue( l_prov.equals( l_prov ) );
        Assert.assertTrue( !l_prov.toString().isEmpty() );
        Assert.assertTrue( 0 != l_prov.hashCode() );
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
        Assert.assertTrue( l_graph.equals( l_graph ) );
        Assert.assertTrue( !l_graph.toString().isEmpty() );
        Assert.assertTrue( l_graph.getAdditionalProperties().size() == 0 );
        l_graph.setAdditionalProperty( "extra1", 1 );
        Assert.assertTrue( l_graph.getAdditionalProperties().size() == 1 );
    }

    /**
     * testing the nodes in the graph
     */
    @Test
    public void nodes()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getGraph() );
        final CGraph l_graph = m_configuration.getGraph();
        Assert.assertNotNull( l_graph.getNodes() );
        final Set<CEntryPoint> l_nodes = l_graph.getNodes();
        Assert.assertTrue( l_nodes.size() == 8 );
        l_nodes.forEach( j ->
        {
            Assert.assertTrue( j.getName().contains( "node" ) );
            Assert.assertTrue( j.getCoordinates().getType().contentEquals( "synthetic" ) );
            Assert.assertTrue( j.getCoordinates().getFirstCoordinate() % 5 == 0 );
            final CCoordinates l_coo = j.getCoordinates();
            Assert.assertTrue( l_coo.equals( l_coo ) );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 0 );
            l_coo.setAdditionalProperty( "extra2", 1 );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( j.getCoordinates().getSecondCoordinate() % 5 == 0 );
            Assert.assertTrue( j.getAdditionalProperties().size() == 0 );
            Assert.assertTrue( j.equals( j ) );
            Assert.assertTrue( !j.toString().isEmpty() );
            j.setAdditionalProperty( "plus", 1 );
            Assert.assertTrue( j.getAdditionalProperties().size() == 1 );

        } );
    }

    /**
     * testing the edges
     */
    @Test
    public void edges()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getGraph() );
        final CGraph l_graph = m_configuration.getGraph();
        Assert.assertNotNull( l_graph.getEdges() );
        Assert.assertTrue( l_graph.getEdges().size() == 20 );
        final Set<CEdge> l_edges = l_graph.getEdges();
        l_edges.forEach( e ->
        {
            Assert.assertTrue( e.equals( e ) );
            Assert.assertTrue( e.getAdditionalProperties().size() == 0 );
            e.setAdditionalProperty( "extra3", 1 );
            Assert.assertTrue( e.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( e.getProvider() == null );
            e.setProvider( "bla" );
            Assert.assertTrue( e.getProvider().contentEquals( "bla" ) );
            Assert.assertTrue( e.getName().contains( "edge" ) );
            Assert.assertTrue( e.getFrom().contains( "node" ) );
            Assert.assertTrue( e.getTo().contains( "node" ) );
            Assert.assertTrue( ( e.getWeight() == 1.0 ) || ( e.getWeight() == 0.75 ) );
            Assert.assertNotNull( e.getFunction() );
            final CFunction l_funct = e.getFunction();
            Assert.assertTrue( l_funct.getName().equals( "even" )  );
            Assert.assertTrue( l_funct.getParameters().size() == 1 );
            final Set<CParameter> l_params = l_funct.getParameters();
            l_params.forEach( p ->
            {
                Assert.assertTrue( p.getName().equals( "dist" ) );
                Assert.assertTrue( p.getValue() == 10.0 );
                Assert.assertTrue( p.getAdditionalProperties().size() == 0 );
                p.setAdditionalProperty( "extra4", 1 );
                Assert.assertTrue( p.getAdditionalProperties().size() == 1 );

            } );
            Assert.assertTrue( l_funct.equals( l_funct ) );
            Assert.assertTrue( !l_funct.toString().isEmpty() );
            Assert.assertTrue( l_funct.getAdditionalProperties().size() == 0 );
            l_funct.setAdditionalProperty( "extra5", 1 );
            Assert.assertTrue( l_funct.getAdditionalProperties().size() == 1 );
        } );
    }

}
