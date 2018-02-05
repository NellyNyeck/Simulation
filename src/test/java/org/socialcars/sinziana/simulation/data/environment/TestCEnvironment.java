package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * test class for environment
 */
public final class TestCEnvironment
{
    private CEnvironment m_env;

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new CEnvironment( new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class ).getGraph() );
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

    @Test
    public static void main( final String[] p_args ) throws IOException
    {
        final IEnvironment l_env = new CEnvironment( new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class ).getGraph() );

        final JFrame l_frame = new JFrame();
        l_frame.getContentPane().add ( new VisualizationViewer<>( new FRLayout<>( l_env.getGraph() ) ) ); ;
        l_frame.setVisible( true );
        l_frame.pack();
    }
}
