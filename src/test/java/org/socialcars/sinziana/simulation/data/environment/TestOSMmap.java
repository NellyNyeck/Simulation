package org.socialcars.sinziana.simulation.data.environment;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.socialcars.sinziana.simulation.RoutePainter;

import javax.swing.JFrame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class TestOSMmap {

    @Test
    public void amsterdamRoute()
    {
        GraphHopper hopper = new GraphHopperOSM().forServer();
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
            i.getPoints().forEach( j -> {
                m_list.add( new GeoPosition( j.lat, j.lon ) );
            });

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
    }

}
