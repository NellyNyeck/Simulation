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
import java.util.Map;


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
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_weights.json" ), CInputpojo.class );
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
        //IntStream.range(0, 10).boxed().forEach( i -> m_destinations.add( ThreadLocalRandom.current().nextInt( 1, 362 ) ) );
        m_destinations.add( 32 );
        m_destinations.add( 52 );
        m_destinations.add( 9 );
        m_destinations.add( 78 );
        m_destinations.add( 287 );
        m_destinations.add( 269 );
        m_destinations.add( 340 );
        m_destinations.add( 191 );
        m_destinations.add( 167 );
        m_destinations.add( 337 );
        m_opt = new CPSPP( m_env, 1, m_destinations );
    }

    /**
     * heatmap
     * @throws GRBException gurobi
     */
    @Test
    public final void heatmap() throws GRBException
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 1700, 1700 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUT.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        m_opt.solve();
        final Map<IEdge, Integer> l_countingmap = m_opt.returnResults();

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        l_countingmap.keySet().forEach( e ->
        {
            System.out.println( e.id() + ": " + l_countingmap.get( e ).toString() );
        } );
    }


    /**
     * main
     * @param p_args cli arguments
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCJungOptimiser l_test = new TestCJungOptimiser();
        l_test.init();
        l_test.heatmap();
    }

}
