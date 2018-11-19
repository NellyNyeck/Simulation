package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.visualization.CHeatFunction;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_weights.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
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

    public final void popularHeatmap( final Integer p_nbofvehicles )
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

        ArrayList<INode> m_destinations = new ArrayList<>();
        final LinkedHashMap<INode, Integer> l_nodes = m_env.nodesPop();
        final INode l_origin = l_nodes.keySet().iterator().next();
        final List<Map.Entry<INode, Integer>> l_entries = new ArrayList<>( l_nodes.entrySet() );
        IntStream.range( l_entries.size() - p_nbofvehicles, l_entries.size() ).boxed().forEach( i -> m_destinations.add( l_entries.get( i ).getKey() ) );



        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, p_nbofvehicles )
            .boxed()
            .flatMap( i -> l_env.route( l_origin, m_destinations.get( i ) ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        final HashMap<INode, Double> l_singlecosts = new HashMap<>();
        m_destinations.forEach( d ->
        {
            AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
            l_env.route( l_origin, d ).stream().forEach( e -> l_cost.getAndUpdate( v -> v + e.weight().doubleValue() ) );
            l_singlecosts.put( d, l_cost.get() );
        } );

        final HashMap<INode, Double> l_platooncosts = new HashMap<>();
        m_destinations.forEach( d ->
        {
            AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
            l_env.route( l_origin, d ).stream().forEach( e -> l_cost.getAndUpdate( v -> v + e.weight().doubleValue() / l_countingmap.get( e ) ) );
            l_platooncosts.put( d, l_cost.get() );
        } );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );


        System.out.println( "The number of platooning vehicles per edge are: " );
        l_countingmap.keySet().forEach( k -> System.out.println( k.id() + ": " + l_countingmap.get( k ) ) );

        System.out.println( "The costs are as follows:" );
        l_singlecosts.keySet().forEach( k ->
        {
            System.out.println( "Destination " + k.id() + " original cost:" + l_singlecosts.get( k ).doubleValue() + " platoon cost:" + l_platooncosts.get( k ) );
        } );

        System.out.println( "Total costs are: " );

        AtomicReference<Double> l_cost = new AtomicReference<>( 0.00 );
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
        Assert.assertTrue( l_zones.size() == 26 );
        System.out.println( m_env.randomnodebyzone( String.valueOf( 3 ) ).id() );
    }

    /**
     * main
     * @param p_args cli arguments
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCJungEnvironment l_test = new TestCJungEnvironment();
        l_test.init();
        //l_test.route();
        //l_test.graph();
        //l_test.heatmap();
        l_test.popularHeatmap( 8 );
        //l_test.testZones();
    }
}
