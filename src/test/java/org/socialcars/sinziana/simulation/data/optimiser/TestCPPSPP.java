package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.elements.CPreference;
import org.socialcars.sinziana.simulation.elements.IPreference;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.optimiser.CPPSPP;
import org.socialcars.sinziana.simulation.visualization.CHeatFunction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * testing the preference based platooning shortest path problem
 */
public class TestCPPSPP
{
    private static final CInputpojo INPUT;
    private CJungEnvironment m_env;
    private CPPSPP m_opt;
    private ArrayList<IPreference> m_preferences;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5HtoL.json" ), CInputpojo.class );
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
        m_preferences = new ArrayList<>();
    }

    /**
     * testing with popular origin and destinations
     * @throws GRBException gurobi
     */
    @Test
    public void testPopular( final Integer p_nbofvehicles ) throws GRBException
    {
        final LinkedHashMap<INode, Integer> l_nodes = m_env.nodesPop();
        final List<Map.Entry<INode, Integer>> l_entries = new ArrayList<>( l_nodes.entrySet() );
        final INode l_origin = l_nodes.keySet().iterator().next();
        IntStream.range( l_entries.size() - p_nbofvehicles, l_entries.size() )
                .boxed()
                .forEach( i ->  m_preferences.add( new CPreference( Integer.valueOf( l_entries.get( i ).getKey().id() ),
                        ThreadLocalRandom.current().nextDouble( 20, 40 ),
                        ThreadLocalRandom.current().nextDouble( 30, 50 ),
                        1000000000, 100000000000000.0 ) ) );
        m_opt = new CPPSPP( m_env, Integer.valueOf( l_origin.id() ), m_preferences, 0 );
        m_opt.solve();


        System.out.println();

        m_opt.display();

        m_opt.getCosts();

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
        IntStream.range( 0, p_nbofvehicles ).boxed().forEach( i -> m_preferences.add(
                new CPreference( ThreadLocalRandom.current().nextInt( 1, m_env.size() ), 30.0, 50.0, 1000000000, 100000000000000.0 ) ) );
        m_opt = new CPPSPP( m_env, 0, m_preferences, 10 );
        m_opt.solve();
        m_opt.display();
        final Map<IEdge, Integer> l_countingmap = m_opt.returnResults();
        m_opt.getCosts();
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
        l_frame.setSize( new Dimension( 1200, 1200 ) );
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

    private void paintWeights()
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
        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        m_env.edges().forEach( e -> l_countingmap.put( e, e.weight().intValue() ) );
        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
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
        final TestCPPSPP l_test = new TestCPPSPP();
        l_test.init();
        //l_test.paintWeights();
        //l_test.randomNodes( 5 );
        l_test.testPopular( 5 );
    }
}
