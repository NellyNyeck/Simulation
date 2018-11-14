package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.optimiser.CPSPP;
import org.socialcars.sinziana.simulation.visualization.CHeatFunction;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


/**
 * test class for the optimiser paired with the jung environmnent
 */
public class TestCJungOptimiser
{
    private static final CInputpojo INPUT;
    private CJungEnvironment m_env;
    private CPSPP m_opt;
    private ArrayList<Integer> m_destinations;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5HlH.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     * @throws GRBException gurobi
     */
    @Before
    public void init() throws GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_destinations = new ArrayList<>();
    }

    /**
     * testing with popular origin and destinations
     * @throws GRBException gurobi
     */
    @Test
    public void testPopular( final Integer p_nbofvehicles ) throws GRBException
    {
        final LinkedHashMap<INode, Integer> l_nodes = m_env.nodesPop();
        final INode l_origin = l_nodes.keySet().iterator().next();
        final List<Map.Entry<INode, Integer>> l_entries = new ArrayList<>( l_nodes.entrySet() );
        IntStream.range( l_entries.size() - p_nbofvehicles, l_entries.size() ).boxed().forEach( i -> m_destinations.add( Integer.valueOf( l_entries.get( i ).getKey().id() ) ) );

        m_opt = new CPSPP( m_env, Integer.valueOf( l_origin.id() ), m_destinations );
        m_opt.solve();
        m_opt.display();
        final Map<IEdge, Integer> l_countingmap = m_opt.returnResults();
        heatmap( l_countingmap );
    }


    /**
     * testing the optimiser with random origins and destinations
     * @throws GRBException gurobi
     */
    @Test
    public void randomNodes( final Integer p_nbofvehicles ) throws GRBException
    {
        IntStream.range( 0, p_nbofvehicles ).boxed().forEach( i -> m_destinations.add( ThreadLocalRandom.current().nextInt( 1, m_env.size() ) ) );
        /*m_destinations.add( 47 );
        m_destinations.add( 222 );
        m_destinations.add( 77 );
        m_destinations.add( 78 );
        m_destinations.add( 254 );
        m_destinations.add( 26 );
        m_destinations.add( 187 );
        m_destinations.add( 51 );
        m_destinations.add( 27 );
        m_destinations.add( 352 );*/
        m_opt = new CPSPP( m_env, 0, m_destinations );
        m_opt.solve();
        m_opt.display();
        final Map<IEdge, Integer> l_countingmap = m_opt.returnResults();
        heatmap( l_countingmap );
    }

    /**
     * heatmap
     */
    @Test
    private void heatmap( final Map<IEdge, Integer> p_countingmap )
    {
        //creates frame
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 800, 800 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        //adds graph to frame
        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        //paints vertices and edges
        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( p_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );

        //adds interactive mouse
        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

    }


    /**
     * main
     * @param p_args cli arguments
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        System.out.println( System.currentTimeMillis() );
        final TestCJungOptimiser l_test = new TestCJungOptimiser();
        l_test.init();
        l_test.randomNodes( 20 );
        System.out.println( System.currentTimeMillis() );
        //l_test.testPopular( 10 );
    }

}
