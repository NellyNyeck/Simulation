package org.socialcars.sinziana.simulation;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * main application
 */
public final class CMain
{
    /**
     * ctor
     */
    private CMain()
    {}

    public static void main( final String[] p_args ) throws IOException {

        /*GraphHopperStorage m_storage = new GraphHopperStorage( new RAMDirectory(), new EncodingManager("car"), false,
            new GraphExtension.NoOpExtension() );

        OSMReader m_reader = new OSMReader(m_storage);
        m_reader.setFile( new File( "src/test/resources/netherlands-latest.osm.pbf" ) );

        m_reader.readGraph();*/

        /*GraphHopper hopper = new GraphHopperOSM().forServer();
        hopper.setDataReaderFile( "src/test/resources/netherlands-latest.osm.pbf" );
        hopper.setGraphHopperLocation( String.valueOf(new File("src/test/graphlocation")) );
        hopper.setEncodingManager(new EncodingManager("car"));

        hopper.importOrLoad();




        GHRequest request = new GHRequest(52.3702, 4.8952, 52.3600, 4.8940).
            setVehicle("car").
            setLocale(Locale.US);

        GHResponse response = hopper.route( request );
        PathWrapper path = response.getBest();

        PointList pointList = path.getPoints();
        double distance = path.getDistance();
        long timeInMs = path.getTime();
        InstructionList howTo = path.getInstructions();

        System.out.println("distance is"+distance);
        System.out.println("duration is:"+timeInMs);
        final ArrayList<GeoPosition> m_list = new ArrayList<>();
        howTo.forEach( i -> {
            i.getPoints().forEach( j -> m_list.add( new GeoPosition( j.lat, j.lon ) ) );

        });

        JXMapViewer mapViewer = new JXMapViewer();
        mapViewer.setZoom( 9 );
        JFrame frame = new JFrame("Routing");
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        RoutePainter routePainter = new RoutePainter(m_list);
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>(m_list), 0.7);

        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);



        /*JXMapKit mapKit = new JXMapKit();
        JFrame frame = new JFrame("Routing");
        GeoPosition m_amsterdam = new GeoPosition(52.3702, 4.8952);


        mapKit.setAddressLocation( m_amsterdam );

        // Display the viewer in a JFrame
        frame.getContentPane().add(mapKit);
        //frame.getContentPane().add(mapViewer);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);*/

        COSMEnvironment m_env = new COSMEnvironment("src/test/resources/netherlands-latest.osm.pbf", 52.430740,52.279503, 5.067963, 4.728887);

        List<List<GeoPosition>> l_routes = new ArrayList<>();
        IntStream.range( 0, 1000 )
            .boxed()
            .flatMap( i -> m_env.route( m_env.randomnode(), m_env.randomnode(), Stream.empty() ).stream() )
            .forEach( p -> // TODO: 15.02.18 paint in in the thing );


        for ( int i = 0; i < 3; i++)
        {
            GeoPosition l_start = m_env.randomnode();
            GeoPosition l_finish = m_env.randomnode();
            if( !l_start.equals( l_finish ) )
            {
                l_routes.add( m_env.route( l_start, l_finish, Stream.empty() ) );
            }
        }
        m_env.drawRoutes( l_routes );


        /*GeoPosition l_start = m_env.randomnode();
        GeoPosition l_finish = m_env.randomnode();
        //GeoPosition l_mid1 = m_env.randomnode();
        //GeoPosition l_mid2 = m_env.randomnode();
        if( l_start != l_finish )  m_env.drawRoutes( m_env.route(l_start, l_finish, Stream.empty() ) );
        // TODO: 15.02.18 paint the nodes black or try to at least*/
    }
}
