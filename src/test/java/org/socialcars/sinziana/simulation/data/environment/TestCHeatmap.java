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
import java.util.stream.IntStream;

/**
 * test class for heatmap
 */
public final class TestCHeatmap
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
     * testing the heatmap
     * @param p_args cli arguments
     */
    public static void main( final String[] p_args )
    {
        final JFrame l_frame = new JFrame();
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        l_frame.setSize( new Dimension( 900, 900 ) );


        final IEnvironment l_env = new CEnvironment( s_input.getGraph() );

        final FRLayout<INode, IEdge> l_projection = new FRLayout<>( l_env.graph(), l_frame.getSize() );
        l_projection.setInitializer( new RandomLocationTransformer<>( l_frame.getSize(), 14 ) );

        final Function<Object, String> l_labeling = new ToStringLabeller();
        final VisualizationViewer<INode, IEdge> l_view = new VisualizationViewer<>( l_projection );
        l_view.getRenderContext().setVertexLabelTransformer( l_labeling );
        l_view.getRenderContext().setEdgeLabelTransformer( l_labeling );

        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, 1000 )
            .boxed()
            .flatMap( i -> l_env.route( "node0", l_env.randomnodebyname() ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        System.out.println( l_countingmap );

        final Function<IEdge, Paint> l_coloring = new CHeat( l_countingmap );
        final Function<INode, Paint> l_black = new CBlack();

        l_view.getRenderContext().setEdgeFillPaintTransformer( l_coloring );
        l_view.getRenderContext().setVertexFillPaintTransformer( l_black );
    }

    private static class CHeat implements Function<IEdge, Paint>
    {
        private Map<IEdge, Color> m_coding = new HashMap<>();

        CHeat( final Map<IEdge, Integer> p_countingmap )
        {
            final Integer l_max = p_countingmap.entrySet().stream().max( Map.Entry.comparingByValue() ).get().getValue();
            p_countingmap.entrySet().forEach( p ->
            {
                final float l_number = p.getValue().floatValue() / l_max.floatValue();
                final Color l_color = new Color( l_number, 0, 0 );
                m_coding.put( p.getKey(), l_color );
            } );
        }

        @Nullable
        @Override
        public Color apply( @Nullable final IEdge p_edge )
        {
            return m_coding.get( p_edge );
        }
    }

    private static class CBlack implements Function<INode, Paint>
    {

        @Nullable
        @Override
        public Paint apply( @Nullable final INode p_node )
        {
            return new Color( 0, 0, 0 );
        }
    }
}
