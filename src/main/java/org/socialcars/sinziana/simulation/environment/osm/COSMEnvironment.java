package org.socialcars.sinziana.simulation.environment.osm;

import com.codepoetics.protonpack.StreamUtils;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.reader.osm.OSMReader;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.RAMDirectory;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.Instruction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.socialcars.sinziana.simulation.visualization.CHeatPainter;
import org.socialcars.sinziana.simulation.visualization.CRoutePainter;

import javax.swing.JFrame;
import javax.swing.event.MouseInputListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * the environment based on Open Street Map
 */
public class COSMEnvironment
{
    private GraphHopper m_hopper;
    private final GeoPosition m_topleft;
    private final GeoPosition m_bottomright;

    /**
     * ctor
     * @param p_file the osm file
     * @param p_north northern most point
     * @param p_south southern most point
     * @param p_east eastern most point
     * @param p_west western most point
     * @throws IOException file
     */
    public COSMEnvironment( final String p_file, final String p_graphlocation, final Double p_north, final Double p_south, final Double p_east, final Double p_west ) throws IOException
    {
        /*GraphHopperStorage m_storage = new GraphHopperStorage( new RAMDirectory(), new EncodingManager("car"), false, new GraphExtension.NoOpExtension() );
        OSMReader m_reader = new OSMReader( m_storage );
        m_reader.setFile( new File( p_file ));
        m_reader.readGraph();*/
        m_hopper = new GraphHopperOSM().forServer();
        m_hopper.setDataReaderFile( p_file );
        m_hopper.setGraphHopperLocation( String.valueOf( new File( p_graphlocation ) ) );
        m_hopper.setEncodingManager( new EncodingManager( "car" ) );
        m_hopper.importOrLoad();

        m_topleft = new GeoPosition( p_north, p_west );
        m_bottomright = new GeoPosition( p_south, p_east );
    }


    public void route( final GeoPosition p_start, final GeoPosition p_finish, final GeoPosition... p_via )
    {
        this.route( p_start, p_finish, java.util.Objects.isNull( p_via ) ? Stream.empty() : java.util.Arrays.stream( p_via ) );
    }

    /**
     * the routing program
     * @param p_start the start point
     * @param p_finish the finish point
     * @param p_via the list of middle
     * @return a list of geographical points with the route
     */
    public List<GeoPosition> route( final GeoPosition p_start, final GeoPosition p_finish, final Stream<GeoPosition> p_via )
    {
        return StreamUtils.windowed(
            Stream.concat(
                Stream.concat(
                    Stream.of( p_start ),
                    p_via
                ),
                Stream.of( p_finish )
            ),
            2
        ).map( i -> new GHRequest(
            i.get( 0 ).getLatitude(),
            i.get( 0 ).getLongitude(),
            i.get( 1 ).getLatitude(),
            i.get( 1 ).getLongitude() )
            .setVehicle( "car" )
            .setLocale( Locale.US )
        )
        .map( i -> m_hopper.route( i ) )
        .filter( i -> !i.hasErrors() )
        .flatMap( i -> i.getBest().getInstructions().stream() )
        .map( Instruction::getPoints )
        .flatMap( i -> IntStream.range( 0, i.size() )
                                .boxed()
                                .map( j -> new GeoPosition( i.getLatitude( j ), i.getLongitude( j ) ) )
        )
        .collect( Collectors.toList() );

    }

    /**
     * draws the routes
     * @param p_routes the list of routes
     */
    public void drawRoutes( final List<List<GeoPosition>> p_routes )
    {
        final JXMapViewer l_mapviewer = new JXMapViewer();
        l_mapviewer.setZoom( 9 );
        final JFrame l_frame = new JFrame( "Routing" );
        l_frame.getContentPane().add( l_mapviewer );
        l_frame.setSize( 800,  600 );
        l_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        l_frame.setVisible( true );

        // Create a TileFactoryInfo for OpenStreetMap
        final TileFactoryInfo l_info = new OSMTileFactoryInfo();
        final DefaultTileFactory l_tilefactory = new DefaultTileFactory( l_info );
        l_mapviewer.setTileFactory( l_tilefactory );

        final List<Painter<JXMapViewer>> l_painters = new ArrayList<Painter<JXMapViewer>>();

        p_routes.stream().forEach( l ->
            {
                final CRoutePainter l_routepainter = new CRoutePainter( l );
                l_painters.add( l_routepainter );
            }
        );

        l_mapviewer.zoomToBestFit( Set.of( m_bottomright, m_topleft ),  0.8 );

        final CompoundPainter<JXMapViewer> l_painter = new CompoundPainter<JXMapViewer>( l_painters );
        l_mapviewer.setOverlayPainter( l_painter );

        /**
         * the zoom
         */
        final MouseInputListener l_mia = new PanMouseInputListener( l_mapviewer );
        l_mapviewer.addMouseListener( l_mia );
        l_mapviewer.addMouseMotionListener( l_mia );
        l_mapviewer.addMouseListener( new CenterMapListener( l_mapviewer ) );
        l_mapviewer.addMouseWheelListener( new ZoomMouseWheelListenerCursor( l_mapviewer ) );
        l_mapviewer.addKeyListener( new PanKeyListener( l_mapviewer ) );
    }

    public void routeOne( GeoPosition p_start, GeoPosition p_finish )
    {
        System.out.println( m_hopper.getLocationIndex().findClosest( p_start.getLatitude(), p_start.getLongitude(), EdgeFilter.ALL_EDGES).getClosestEdge().getName() );
        System.out.println( m_hopper.getLocationIndex().findClosest( p_finish.getLatitude(), p_finish.getLongitude(), EdgeFilter.ALL_EDGES ).getClosestEdge().getName() );
        GHRequest l_req = new GHRequest(p_start.getLatitude(),p_start.getLongitude(), p_finish.getLatitude(), p_finish.getLongitude() );
        GHResponse l_resp = m_hopper.route( l_req );
        if ( l_resp.hasErrors() ) System.out.println( "no bueno" );
        else {
            try {
                FileWriter fileWriter = new FileWriter("info.json");
                fileWriter.write( l_resp.getBest().getInstructions().createJson().toString() );
                l_resp.getBest().getInstructions().stream().forEach( i -> {
                    try {
                        fileWriter.write( i.getExtraInfoJSON().toString() );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fileWriter.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * draws a heatmap
     * @param p_routes the list of routes
     */
    public void drawHeat( final List<List<GeoPosition>> p_routes ) throws IOException
    {
        final JXMapViewer l_mapviewer = new JXMapViewer();
        l_mapviewer.setZoom( 9 );
        final JFrame l_frame = new JFrame( "Heatmap" );
        l_frame.getContentPane().add( l_mapviewer );
        l_frame.setSize( 800,  600 );
        l_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        l_frame.setVisible( true );

        // Create a TileFactoryInfo for OpenStreetMap
        final TileFactoryInfo l_info = new OSMTileFactoryInfo();
        final DefaultTileFactory l_tilefactory = new DefaultTileFactory( l_info );
        l_mapviewer.setTileFactory( l_tilefactory );

        final List<Painter<JXMapViewer>> l_painters = new ArrayList<Painter<JXMapViewer>>();

        final CHeatPainter l_heatpainter = new CHeatPainter( p_routes );
        l_painters.add( l_heatpainter );

        l_mapviewer.zoomToBestFit( Set.of( m_bottomright, m_topleft ),  0.8 );

        final CompoundPainter<JXMapViewer> l_painter = new CompoundPainter<JXMapViewer>( l_painters );
        l_mapviewer.setOverlayPainter( l_painter );

        /**
         * the zoom
         */
        final MouseInputListener l_mia = new PanMouseInputListener( l_mapviewer );
        l_mapviewer.addMouseListener( l_mia );
        l_mapviewer.addMouseMotionListener( l_mia );
        l_mapviewer.addMouseListener( new CenterMapListener( l_mapviewer ) );
        l_mapviewer.addMouseWheelListener( new ZoomMouseWheelListenerCursor( l_mapviewer ) );
        l_mapviewer.addKeyListener( new PanKeyListener( l_mapviewer ) );

        writeHeat( l_heatpainter.getValues() );

    }

    private void writeHeat( HashMap<GeoPosition, Integer> p_values ) throws IOException
    {
        FileWriter writer = new FileWriter("heatmap.json");
        HashMap<Integer, CStructure> l_heats = new HashMap<>();
        final Set<GeoPosition> l_keys = p_values.keySet();
        l_keys.forEach( p-> {
            int l_id = m_hopper.getLocationIndex().findClosest( p.getLatitude(), p.getLongitude(), EdgeFilter.ALL_EDGES).getClosestEdge().getEdge();
            CStructure l_new = l_heats.get( l_id );
            if ( l_new != null)
            {
                l_new = new CStructure( m_hopper.getLocationIndex().findClosest( p.getLatitude(), p.getLongitude(), EdgeFilter.ALL_EDGES ).getClosestEdge().getDistance(), m_hopper.getLocationIndex().findClosest( p.getLatitude(), p.getLongitude(), EdgeFilter.ALL_EDGES ).getClosestEdge().getName() );
                l_heats.put( l_id, l_new );
            }
            else
            {
                l_new.add( p_values.get( p ) );
            }
        } );
        JSONObject l_json = new JSONObject();
        l_heats.keySet().forEach( s -> l_json.put( s, l_heats.get( s ) ) );
        writer.write( l_json.toJSONString() );
        writer.flush();
        writer.close();
}

    /**
     * generates a radom point
     * @return the random node generated
     */
    public GeoPosition randomnode()
    {
        final Double l_latitude = ThreadLocalRandom.current().nextDouble( m_bottomright.getLatitude(), m_topleft.getLatitude() );
        final Double l_longiture = ThreadLocalRandom.current().nextDouble( m_topleft.getLongitude(), m_bottomright.getLongitude() );
        return new GeoPosition( l_latitude, l_longiture );
    }

    /**
     * creating the panel
     * @return the map viewer
     */
    public JXMapViewer panel()
    {
        final JXMapViewer l_mapviewer = new JXMapViewer();
        l_mapviewer.setZoom( 9 );
        final JFrame l_frame = new JFrame( "Routing" );
        l_frame.getContentPane().add( l_mapviewer );
        l_frame.setSize( 800,  600 );
        l_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        l_frame.setVisible( true );

        // Create a TileFactoryInfo for OpenStreetMap
        final TileFactoryInfo l_info = new OSMTileFactoryInfo();
        final DefaultTileFactory l_tilefactory = new DefaultTileFactory( l_info );
        l_mapviewer.setTileFactory( l_tilefactory );

        return l_mapviewer;
    }

    private class CStructure
    {
        String m_name;
        Integer m_visited;
        Double m_distance;

        CStructure( Double p_distance, String p_name )
        {
            m_distance = p_distance;
            m_name = p_name;
            m_visited = 0;
        }

        public void add( Integer p_new )
        {
            m_visited = m_visited + p_new;
        }
    }

}
