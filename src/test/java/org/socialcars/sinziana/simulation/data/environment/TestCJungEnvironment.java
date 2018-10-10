package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * test class for environment
 */
public final class TestCJungEnvironment
{
    private static final int ROUTENUMBER = 10;

    private static final CInputpojo INPUT;

    private CJungEnvironment m_env;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/100-10x10.json" ), CInputpojo.class );
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

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, ROUTENUMBER )
                 .boxed()
                 .flatMap( i -> l_env.route( l_env.randomnodebyname(), l_env.randomnodebyname() ).stream() )
                 .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
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

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, 1 )
            .boxed()
            .flatMap( i -> l_env.route( l_env.randomnodebyname(), l_env.randomnodebyname() ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
        // create get node function
        //create function to read destinations from file
        /*System.out.println( "From Depot 1" );
        System.out.println( l_env.route( "node0", "node6", Stream.empty() ) );
        System.out.println( l_env.route( "node0", "node12", Stream.empty() ) );
        System.out.println( l_env.route( "node0", "node13", Stream.empty() ) );
        System.out.println( l_env.route( "node0", "node8", Stream.empty() ) );
        System.out.println( "From Depot 2" );
        System.out.println( l_env.route( "node20", "node24", Stream.empty() ) );
        System.out.println( l_env.route( "node20", "node17", Stream.empty() ) );
        System.out.println( l_env.route( "node20", "node12", Stream.empty() ) );
        System.out.println( l_env.route( "node20", "node13", Stream.empty() ) );*/
    }

    /**
     * testing the zones
     */
    @Test
    public void testZones()
    {
        final HashMap<String, List<INode>> l_zones = m_env.getZones();
        Assert.assertTrue( l_zones.size() == 10 );
        IntStream.range( 1, l_zones.size() + 1 ).forEach( i ->
        {
            final List<INode> l_test = l_zones.get( String.valueOf( i ) );
            l_test.forEach( k ->
            {
                System.out.println( "node " + k.id() );
            } );
        } );
        System.out.println();

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
        l_test.route();
        l_test.graph();
        l_test.heatmap();
    }

}
