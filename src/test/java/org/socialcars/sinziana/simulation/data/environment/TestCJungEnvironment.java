package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.visualization.CHeatFunction;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;



/**
 * test class for environment
 */
public final class TestCJungEnvironment
{
    private static final int ROUTENUMBER = 1000;

    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_density.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     */
    @Before
    public void init()
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );

    }

    /**
     * graph only
     */
    @Test
    public final void graph()
    {
        final JFrame l_frame = new JFrame();
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        l_frame.setSize( new Dimension( 1000, 1000 ) );
        l_frame.getContentPane().add( new CJungEnvironment( INPUT.getGraph() ).panel( l_frame.getSize() ) );
        l_frame.setVisible( true );

    }

    /**
     * heatmap
     */
    @Test
    public final void heatmap()
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 1000, 1000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, ROUTENUMBER )
                 .boxed()
                 .flatMap( i -> l_env.route( l_env.randomnodebyname(), l_env.randomnodebyname() ).stream() )
                 .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    /**
     * creates a heatmap of the most popular p_nbofvehicles destination
     * @param p_nbofvehicles number of destinations
     */
    public final void popularHeatmap( final Integer p_nbofvehicles )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 1200, 1200 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        final ArrayList<INode> l_destinations = new ArrayList<>();
        final LinkedHashMap<INode, Integer> l_nodes = m_env.nodesPop();
        final INode l_origin = l_nodes.keySet().iterator().next();
        final List<Map.Entry<INode, Integer>> l_entries = new ArrayList<>( l_nodes.entrySet() );
        IntStream.range( l_entries.size() - p_nbofvehicles, l_entries.size() ).boxed().forEach( i -> l_destinations.add( l_entries.get( i ).getKey() ) );



        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, p_nbofvehicles )
            .boxed()
            .flatMap( i -> l_env.route( l_origin, l_destinations.get( i ) ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        System.out.println( "Origin is: " + l_origin.id() );
        System.out.println();

        final HashMap<INode, Double> l_singlecosts = new HashMap<>();
        l_destinations.forEach( d ->
        {
            final AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
            l_env.route( l_origin, d ).forEach( e ->
            {
                System.out.println( "Destination " + d.id() + ": "  + e.id() );
                l_cost.getAndUpdate( v -> v + e.weight().doubleValue() );
            } );
            l_singlecosts.put( d, l_cost.get() );
            System.out.println();
        } );

        final HashMap<INode, Double> l_platooncosts = new HashMap<>();
        final HashMap<INode, Double> l_tourcost = new HashMap<>();
        l_destinations.forEach( d ->
        {
            final AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
            final AtomicReference<Double> l_tour = new AtomicReference<>( 0.00 );
            l_env.route( l_origin, d ).forEach( e ->
            {
                l_cost.getAndUpdate( v -> v + e.weight().doubleValue() / l_countingmap.get( e ) );
                l_tour.getAndUpdate( v -> v + e.weight().doubleValue() );
            } );
            l_platooncosts.put( d, l_cost.get() );
            l_tourcost.put( d, l_tour.get() );
        } );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );


        System.out.println( "The number of platooning vehicles per edge are: " );
        l_countingmap.keySet().forEach( k -> System.out.println( k.id() + ": " + l_countingmap.get( k ) ) );
        System.out.println();

        System.out.println( "The costs are as follows:" );
        l_singlecosts.keySet().forEach( k ->
            System.out.println( "Destination " + k.id()
                    + " original cost:" + l_singlecosts.get( k )
                    + " platoon cost:" + l_platooncosts.get( k )
                    + " tour cost:" + l_tourcost.get( k ) ) );
        System.out.println();

        System.out.println( "Total costs are: " );

        final AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
        l_countingmap.keySet().forEach( k -> l_cost.getAndUpdate( v -> v + k.weight().doubleValue() ) );
        System.out.print( l_cost );
    }

    /**
     * testing out the routing
     */
    @Test
    public final void route()
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, 1 )
            .boxed()
            .flatMap( i -> l_env.route( l_env.randomnodebyname(), l_env.randomnodebyname() ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    /**
     * testing the zones
     */
    @Test
    public void testZones()
    {
        final HashMap<String, List<INode>> l_zones = m_env.getZones();
        Assert.assertEquals( 26, l_zones.size() );
        System.out.println( m_env.randomnodebyzone( String.valueOf( 3 ) ).id() );
    }

    /**
     * testing the density of the network
     * @throws IOException file
     */
    @Test
    public void testDensity() throws IOException
    {
        final HashMap<IEdge, Double> l_density = new HashMap<>();
        m_env.edges().forEach(  e -> l_density.put( e, e.weight().doubleValue() / ( m_env.edgeLength( e ).doubleValue() * 10 ) ) );

        l_density.keySet().forEach( k -> System.out.println( l_density.get( k ) ) );


        final Writer l_out = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream( "edgeDensity.json" ), StandardCharsets.UTF_8 ) );
        final JSONObject l_json =  new JSONObject( l_density );
        l_out.write( l_json.toJSONString() );
        l_out.flush();
        l_out.close();
    }

    /**
     * main
     * @param p_args cli arguments
     */
    public static void main( final String[] p_args )
    {

        final TestCJungEnvironment l_test = new TestCJungEnvironment();
        l_test.init();
        l_test.route();
        l_test.graph();
        l_test.heatmap();
        l_test.popularHeatmap( 6 );
        l_test.testZones();
    }
}
