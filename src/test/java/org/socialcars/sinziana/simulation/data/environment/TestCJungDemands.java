package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CDemandsjungpojo;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.environment.demand.jung.CInstanceJung;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.IEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.INode;
import org.socialcars.sinziana.simulation.visualization.CHeatFunction;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * testing jung environment with demands
 */
public class TestCJungDemands
{
    private static final CDemandsjungpojo INPUTD;
    private static final CInputpojo INPUTG;
    private static final CInputpojo INPUTGD;

    private ArrayList<CInstanceJung> m_demand;
    private CJungEnvironment m_env;

    static
    {
        try
        {
            INPUTG = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten.json" ), CInputpojo.class );
            INPUTD = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_demand.json" ), CDemandsjungpojo.class );
            INPUTGD = new ObjectMapper().readValue( new File( "src/test/resources/tiergarten_weights.json" ), CInputpojo.class );
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
        m_env = new CJungEnvironment( INPUTG.getGraph() );
        m_demand = new ArrayList<>();
        INPUTD.getDemand().forEach( j ->
        {
            final CInstanceJung l_new = new CInstanceJung( j );
            m_demand.add( l_new );
        } );
    }

    /**
     * heatmap
     * @throws IOException file
     */
    @Test
    public final void heatmap() throws IOException
    {
        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 1000, 1000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUTG.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final HashMap<IEdge, Integer> l_countingmap = new HashMap<>();
        m_demand.forEach( i -> IntStream.range( 0, Math.round( i.howMany() ) ).boxed()
                .flatMap( j -> l_env.route( l_env.randomnodebyzone( i.from() ), l_env.randomnodebyzone( i.to() ) ).stream() )
                .forEach( j -> l_countingmap.put( j, l_countingmap.getOrDefault( j, 0 ) + 1 ) ) );

        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        writeHeat( l_countingmap );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    private void writeHeat( final HashMap<IEdge, Integer> p_values ) throws IOException
    {
        final File l_filedir = new File( "nellys_heatmap.json" );

        final Writer l_out = new BufferedWriter( new OutputStreamWriter(
            new FileOutputStream( l_filedir ), StandardCharsets.UTF_8 ) );

        final HashMap<String, Object> l_result = new HashMap<>();
        p_values.keySet().forEach( s -> l_result.put( s.id(), p_values.get( s ) ) );
        final JSONObject l_json =  new JSONObject( l_result );
        l_out.write( l_json.toJSONString() );
        l_out.flush();
        l_out.close();
        System.out.println( "done" );
    }

    /**
     * testing the zones
     */
    @Test
    public void testZones()
    {
        final HashMap<String, List<INode>> l_zones = m_env.getZones();
        Assert.assertEquals( 26, l_zones.size() );
        IntStream.range( 1, l_zones.size() + 1 ).forEach( i ->
        {
            final List<INode> l_test = l_zones.get( String.valueOf( i ) );
            l_test.forEach( k -> System.out.println( "node " + k.id() ) );
        } );
        System.out.println();

        System.out.println( m_env.randomnodebyzone( String.valueOf( 5 ) ).id() );
    }

    /**
     * tests densities
     */
    @Test
    public void testDensity()
    {
        m_env = new CJungEnvironment( INPUTGD.getGraph() );

        final JFrame l_frame = new JFrame();
        l_frame.setSize( new Dimension( 2000, 2000 ) );
        l_frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        final IEnvironment<VisualizationViewer<INode, IEdge>> l_env = new CJungEnvironment( INPUTGD.getGraph() );
        final VisualizationViewer<INode, IEdge> l_view = l_env.panel( l_frame.getSize() );
        l_frame.getContentPane().add( l_view );
        l_frame.setVisible( true );

        final Map<IEdge, Integer> l_countingmap = new HashMap<>();
        IntStream.range( 0, 1 )
            .boxed()
            .flatMap( i -> l_env.route( l_env.randomnodebyname(), l_env.randomnodebyname() ).stream() )
            .forEach( i -> l_countingmap.put( i, l_countingmap.getOrDefault( i, 0 ) + 1 ) );

        final DefaultModalGraphMouse l_gm = new DefaultModalGraphMouse();
        l_gm.setMode( ModalGraphMouse.Mode.TRANSFORMING );
        l_view.setGraphMouse( l_gm );

        l_view.getRenderContext().setEdgeFillPaintTransformer( new CHeatFunction( l_countingmap ) );
        l_view.getRenderContext().setVertexFillPaintTransformer( i -> new Color( 0, 0, 0 ) );
    }

    /**
     * main
     * @param p_args cli arguments
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCJungDemands l_test = new TestCJungDemands();
        l_test.init();
        l_test.heatmap();
        l_test.testDensity();
    }


}
