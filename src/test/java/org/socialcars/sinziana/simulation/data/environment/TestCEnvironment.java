package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.BasicRenderer;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.CEnvironment;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import javax.swing.JFrame;
import javax.swing.Renderer;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * test class for environment
 */
public final class TestCEnvironment
{
    private static CInputpojo m_input;

    static
    {
        try
        {
            m_input = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    private CEnvironment m_env;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new CEnvironment( m_input.getGraph() );
    }

    @Test
    public void graph()
    {
        System.out.println( m_env );
    }

    @Test
    public void heatmap()
    {
        final Map<String, Integer> l_heatmap = new HashMap<>();

        IntStream.range( 0, 1000 )
                 .boxed()
                 .flatMap( i -> m_env.route( m_env.randomnode(), m_env.randomnode() ).stream() )
                 .forEach( i -> l_heatmap.put( i.id(), l_heatmap.getOrDefault( i.id(), 0 ) + 1 ) );

        System.out.println( l_heatmap );
    }

    public static void main( final String[] p_args ) throws IOException
    {
        final JFrame l_frame = new JFrame();
        l_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        l_frame.setSize( new Dimension( 1200, 1200 ) );


        final IEnvironment l_env = new CEnvironment( m_input.getGraph() );

        final FRLayout<INode, IEdge> l_projection = new FRLayout<>( l_env.getGraph(), l_frame.getSize() );
        l_projection.setInitializer( new RandomLocationTransformer<>( l_frame.getSize(), 1 ) );

        l_frame.getContentPane().add ( new VisualizationViewer<>( l_projection ) );
        l_frame.setVisible( true );
    }

}
