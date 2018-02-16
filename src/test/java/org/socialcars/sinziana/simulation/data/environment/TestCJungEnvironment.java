package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.visualization.EColorMap;

import javax.annotation.Nullable;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInputpojo.class );
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
        l_frame.setSize( new Dimension( 900, 900 ) );
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
        l_frame.setSize( new Dimension( 900, 900 ) );
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

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeat( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    /**
     * heatmap class
     */
    private final static class CHeat implements Function<IEdge, Paint>
    {
        private Map<IEdge, Color> m_coding;

        CHeat( final Map<IEdge, Integer> p_countingmap )
        {
            final Integer l_max = p_countingmap.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
            m_coding = p_countingmap.entrySet().stream().collect( Collectors.toMap( Map.Entry::getKey, i -> EColorMap.MAGMA.apply( i.getValue(), l_max ) ) );
        }

        @Nullable
        @Override
        public Color apply( @Nullable final IEdge p_edge )
        {
            return m_coding.get( p_edge );
        }
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
        l_test.graph();
        l_test.heatmap();
    }

}
