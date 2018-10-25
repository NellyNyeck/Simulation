package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
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
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class TestCJungOptimiser
{
    private static final CInputpojo INPUT;
    private CJungEnvironment m_env;
    private CPSPP m_optimiser;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue(new File("src/test/resources/tiergarten.json"), CInputpojo.class);
        } catch (final IOException l_exception) {
            throw new UncheckedIOException(l_exception);
        }
    }

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws GRBException
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        final ArrayList<Integer> l_destinations = new ArrayList<>();
        IntStream.range(0, 10).boxed().forEach( i -> l_destinations.add( ThreadLocalRandom.current().nextInt( 1, m_env.size() ) ) );
        /*l_destinations.add(32);
        l_destinations.add(52);
        l_destinations.add(9);
        l_destinations.add(78);
        l_destinations.add(287);
        l_destinations.add(269);
        l_destinations.add(340);
        l_destinations.add(191);
        l_destinations.add(167);
        l_destinations.add(337);*/

        m_optimiser = new CPSPP(m_env, 1, l_destinations);
    }

    /**
     * heatmap
     */
    @Test
    public final void heatmap() throws GRBException
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 1000, 1000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        m_optimiser.solve();
        final Map<IEdge, Integer> l_countingmap = m_optimiser.returnResults();

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );

        l_countingmap.keySet().forEach( e ->
        {
            System.out.println( e.id() + ": " + l_countingmap.get(e).toString() );
        });
    }


    /**
     * main
     * @param p_args cli arguments
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCJungOptimiser l_test = new TestCJungOptimiser();
        l_test.init();
        l_test.heatmap();
        //l_test.m_optimiser.display();
        l_test.m_optimiser.cleanUp();
    }

}
