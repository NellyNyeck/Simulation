package org.socialcars.sinziana.simulation.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.data.input.CClientpojo;
import org.socialcars.sinziana.simulation.data.input.CConfigurationpojo;
import org.socialcars.sinziana.simulation.data.input.CCoordinatespojo;
import org.socialcars.sinziana.simulation.data.input.CEdgepojo;
import org.socialcars.sinziana.simulation.data.input.CFunctionpojo;
import org.socialcars.sinziana.simulation.data.input.CGraphpojo;
import org.socialcars.sinziana.simulation.data.input.CHumanpojo;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CParameterpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.data.input.CProviderpojo;
import org.socialcars.sinziana.simulation.data.input.CStartpojo;
import org.socialcars.sinziana.simulation.data.input.CVehiclepojo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * unit-test for data input
 */
public final class TestCInput
{
    /**
     * configuration
     */
    private CInputpojo m_configuration;

    /**
     * initialize configuration
     *
     * @throws IOException on example reading error
     */
    @Before
    public final void init() throws IOException
    {
        m_configuration = new ObjectMapper().readValue( new File( "src/test/resources/biggest.json" ), CInputpojo.class );
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
        Assert.assertNotNull( m_configuration.getBikes() );
        Assert.assertNotNull( m_configuration.getClients() );
        Assert.assertNotNull( m_configuration.getHumans() );
        Assert.assertNotNull( m_configuration.getVehicles() );
        Assert.assertNotNull( m_configuration.getBikes() );
    }

    /**
     * test scenario configuration
     */
    @Test
    public void configuration()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getConfiguration() );
        final CConfigurationpojo l_config = m_configuration.getConfiguration();
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
     * test scenario providers
     */
    @Test
    public void testproviders()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getProviders() );
        Assert.assertTrue( m_configuration.getProviders().size() == 1 );
        final CProviderpojo l_prov = m_configuration.getProviders().get( 0 );
        Assert.assertNotNull( l_prov );
        Assert.assertTrue( l_prov.equals( l_prov ) );
        Assert.assertTrue( !l_prov.toString().isEmpty() );
        Assert.assertTrue( 0 != l_prov.hashCode() );
        Assert.assertTrue( l_prov.getName().contentEquals( "DHL" ) );
        Assert.assertTrue( l_prov.getFilename().contentEquals( "" ) );
        Assert.assertNotNull( l_prov.getAgentType() );
        Assert.assertTrue( l_prov.getAgentType().contentEquals( "provider" ) );
        Assert.assertNotNull( l_prov.getColour() );
        Assert.assertTrue( l_prov.getColour().contentEquals( "yellow" ) );
        Assert.assertNotNull( l_prov.getAvailablePods() );
        Assert.assertNotNull( l_prov.getAvailablePods() == 1 );
        Assert.assertNotNull( l_prov.getMaximumCustomers() );
        Assert.assertTrue( l_prov.getMaximumCustomers() == 1 );
        Assert.assertNotNull( l_prov.getMaxOutPodsTime() );
        Assert.assertTrue( l_prov.getMaxOutPodsTime() == 1  );
        Assert.assertNotNull( l_prov.getFunction() );
        Assert.assertTrue( l_prov.getFunction().getName().contentEquals( "normal" ) );
        Assert.assertTrue( l_prov.getFunction().getParameters().size() == 2 );
        Assert.assertNotNull( l_prov.getAdditionalProperties() );
        Assert.assertTrue( l_prov.getAdditionalProperties().size() == 0 );
        Assert.assertNotNull( l_prov.getPods() );
        Assert.assertTrue( l_prov.getPods().size() == 1 );
        Assert.assertNotNull( l_prov.getDepots() );
        Assert.assertNotNull( l_prov.getDepots().size() == 1 );
        l_prov.setAdditionalProperty( "extra ?", 1 );
        Assert.assertTrue( l_prov.getAdditionalProperties().size() == 1 );
    }

    /**
     * pod testing
     */
    @Test
    public void testpod()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getProviders() );
        final CProviderpojo l_prov = m_configuration.getProviders().get( 0 );
        final Set<CPodpojo> l_pods = l_prov.getPods();
        l_pods.forEach( p ->
        {
            Assert.assertNotNull( p.getName() );
            Assert.assertTrue( p.getName().contains( "pod" ) );
            Assert.assertTrue( p.getFilename().contentEquals( "" ) );
            Assert.assertTrue( p.getAgentType().contentEquals( "pod" ) );
            Assert.assertTrue( p.getCapacity() == 1 );
            Assert.assertTrue( p.getProvider().contentEquals( "DHL" ) );
            Assert.assertNotNull( p.getStart() );
            Assert.assertNotNull( p.getFinish() );
            Assert.assertTrue( p.getMaxAccel() == 0.5 );
            Assert.assertTrue( p.getMaxDecel() == 0.3 );
            Assert.assertTrue( p.getMaxSpeed() == 1 );
            Assert.assertTrue( p.getMiddle().size() == 0 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 0 );
            p.setAdditionalProperty( "extraness", 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
        } );
    }

    /**
     * testing humans MUAHAHHAHAHAH
     */
    @Test
    public void testhuman()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getHumans() );
        final List<CHumanpojo> l_humans = m_configuration.getHumans();
        l_humans.forEach( p ->
        {
            Assert.assertNotNull( p.getName() );
            Assert.assertTrue( p.getName().contains( "human" ) );
            Assert.assertTrue( p.getFilename().contentEquals( "" ) );
            Assert.assertTrue( p.getAgentType().contentEquals( "human" ) );
            Assert.assertNotNull( p.getStart() );
            Assert.assertNotNull( p.getFinish() );
            Assert.assertTrue( p.getMiddle().size() == 0 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 0 );
            p.setAdditionalProperty( "extraness", 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( p.getMaxAccel() == 0 );
            Assert.assertTrue( p.getMaxDecel() == 0 );
            Assert.assertTrue( p.getMaxSpeed() == 0 );
        } );
    }

    /**
     * testing bikes
     */
    @Test
    public void testbikes()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getBikes() );
        final List<CBicyclepojo> l_bikes = m_configuration.getBikes();
        l_bikes.forEach( p ->
        {
            Assert.assertNotNull( p.getName() );
            Assert.assertTrue( p.getName().contains( "bike" ) );
            Assert.assertTrue( p.getFilename().contentEquals( "" ) );
            Assert.assertTrue( p.getAgentType().contentEquals( "bike" ) );
            Assert.assertNotNull( p.getStart() );
            Assert.assertNotNull( p.getFinish() );
            Assert.assertTrue( p.getMiddle().size() == 0 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 0 );
            p.setAdditionalProperty( "extraness", 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( p.getMaxAccel() == 0 );
            Assert.assertTrue( p.getMaxDecel() == 0 );
            Assert.assertTrue( p.getMaxSpeed() == 0 );
        } );
    }

    /**
     * testing the vehicles
     */
    @Test
    public void testvehs()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getVehicles() );
        final List<CVehiclepojo> l_vehicles = m_configuration.getVehicles();
        l_vehicles.forEach( p ->
        {
            Assert.assertNotNull( p.getName() );
            Assert.assertTrue( p.getName().contains( "vehicle" ) );
            Assert.assertTrue( p.getFilename().contentEquals( "" ) );
            Assert.assertTrue( p.getAgentType().contentEquals( "vehicle" ) );
            Assert.assertNotNull( p.getStart() );
            Assert.assertNotNull( p.getFinish() );
            Assert.assertTrue( p.getMiddle().size() == 0 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 0 );
            p.setAdditionalProperty( "extraness", 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( p.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( p.getMaxAccel() == 0 );
            Assert.assertTrue( p.getMaxDecel() == 0 );
            Assert.assertTrue( p.getMaxSpeed() == 0 );
        } );
    }

    /**
     * testing clients
     */
    @Test
    public void testclient()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getClients() );
        final List<CClientpojo> l_clients = m_configuration.getClients();
        l_clients.stream().forEach( c ->
        {
            Assert.assertTrue( c.getName().contains( "client" ) );
            Assert.assertTrue( c.getFilename().isEmpty() );
            Assert.assertTrue( c.getAgentType().contentEquals( "client" ) );
            Assert.assertNotNull( c.getLocation() );
            Assert.assertTrue( c.getLabels().size() == 0 );
            Assert.assertTrue( c.getAdditionalProperties().size() == 0 );
            c.setAdditionalProperty( "miss extra", 1 );
            Assert.assertTrue( c.getAdditionalProperties().size() == 1 );
            Assert.assertTrue( c.equals( c ) );
        } );
    }


    /**
     * test scenario graph
     */
    @Test
    public void testgraph()
    {
        Assume.assumeNotNull( m_configuration );
        Assert.assertNotNull( m_configuration.getGraph() );
        final CGraphpojo l_graph = m_configuration.getGraph();
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
        final CGraphpojo l_graph = m_configuration.getGraph();
        Assert.assertNotNull( l_graph.getNodes() );
        final Set<CStartpojo> l_nodes = l_graph.getNodes();
        Assert.assertTrue( l_nodes.size() == 126 );
        l_nodes.forEach( j ->
        {
            Assert.assertTrue( j.getName().contains( "node" ) );
            final CCoordinatespojo l_coo = j.getCoordinates();
            Assert.assertTrue( l_coo.equals( l_coo ) );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 0 );
            l_coo.setAdditionalProperty( "extra2", 1 );
            Assert.assertTrue( l_coo.getAdditionalProperties().size() == 1 );
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
        final CGraphpojo l_graph = m_configuration.getGraph();
        Assert.assertNotNull( l_graph.getEdges() );
        Assert.assertTrue( l_graph.getEdges().size() == 498 );
        final Set<CEdgepojo> l_edges = l_graph.getEdges();
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
            final CFunctionpojo l_funct = e.getFunction();
            Assert.assertTrue( l_funct.getName().equals( "even" )  );
            Assert.assertTrue( l_funct.getParameters().size() == 1 );
            final Set<CParameterpojo> l_params = l_funct.getParameters();
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
