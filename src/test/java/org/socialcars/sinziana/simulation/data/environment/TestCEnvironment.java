package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.CEnvironment;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import javax.swing.*;
import java.awt.*;
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
    private static CInputpojo s_input;

    static
    {
        try
        {
            s_input = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
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
        m_env = new CEnvironment( s_input.getGraph() );
    }

    /**
     * graph output
     */
    @Test
    public void graph()
    {
        System.out.println( m_env );
    }

    /**
     * heatmap
     */
    @Test
    public void heatmap()
    {
        final Map<String, Integer> l_heatmap = new HashMap<>();

        IntStream.range( 0, 1000 )
                 .boxed()
                 .flatMap( i -> m_env.route( m_env.randomnodebyname(), m_env.randomnodebyname() ).stream() )
                 .forEach( i -> l_heatmap.put( i.id(), l_heatmap.getOrDefault( i.id(), 0 ) + 1 ) );

        System.out.println( l_heatmap );
    }

    /**
     * main
     *
     * @param p_args cli arguments
     */
    public static void main( final String[] p_args )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        l_frame.setSize( new Dimension( 900, 900 ) );


        final IEnvironment l_env = new CEnvironment( s_input.getGraph() );

        final FRLayout<INode, IEdge> l_projection = new FRLayout<>( l_env.graph(), l_frame.getSize() );
        l_projection.setInitializer( new RandomLocationTransformer<>( l_frame.getSize(), 1 ) );

        final Function<Object, String> l_labeling = new ToStringLabeller();
        final VisualizationViewer<INode, IEdge> l_view = new VisualizationViewer<>( l_projection );
        l_view.getRenderContext().setVertexLabelTransformer( l_labeling );
        l_view.getRenderContext().setEdgeLabelTransformer( l_labeling );

        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );
    }

}
