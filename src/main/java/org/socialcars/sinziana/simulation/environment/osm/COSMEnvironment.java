package org.socialcars.sinziana.simulation.environment.osm;

import com.codepoetics.protonpack.StreamUtils;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
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
import java.util.stream.Stream;


public class COSMEnvironment
{
    GraphHopper m_hopper;

    final GeoPosition m_topleft;
    final GeoPosition m_bottomright;

    public COSMEnvironment( final String p_file, final Double p_north, final Double p_south, final Double p_east, final Double p_west )
    {
        m_hopper = new GraphHopperOSM().forServer();
        m_hopper.setDataReaderFile( p_file );
        m_hopper.setGraphHopperLocation( String.valueOf(new File( "src/test/graphlocation" ) ) );
        m_hopper.setEncodingManager( new EncodingManager( "car" ) );
        m_hopper.importOrLoad();
        m_topleft = new GeoPosition( p_north, p_west );
        m_bottomright = new GeoPosition( p_south, p_east );
    }


    public void route( final GeoPosition p_start, final GeoPosition p_finish, final GeoPosition... p_via )
    {
        this.route( p_start, p_finish, java.util.Objects.isNull( p_via ) ? Stream.empty() : java.util.Arrays.stream( p_via ) );
    }

    public List<GeoPosition> route( final GeoPosition p_start, final GeoPosition p_finish, final Stream<GeoPosition> p_via )
    {
        List<GeoPosition> l_list = new ArrayList<>();
        StreamUtils.windowed(
            Stream.concat(
                Stream.concat(
                    Stream.of(p_start),
                    p_via
                ),
                Stream.of(p_finish)
            ),
            2
        ).forEach(
            i ->
            {
                GHRequest request = new GHRequest(
                    i.get(0).getLatitude(),
                    i.get(0).getLongitude(),
                    i.get(1).getLatitude(),
                    i.get(1).getLongitude()).
                    setVehicle("car").
                    setLocale(Locale.US);
                GHResponse response = m_hopper.route(request);
                if(response.hasErrors()) {
                    return;
                }
                PathWrapper path = response.getBest();
                path.getInstructions().stream()
                    .forEach(n ->  n.getPoints().forEach( p -> l_list.add( new GeoPosition( p.lat, p.lon ) )) );
            }
        );

        return l_list;

        /*JXMapViewer mapViewer = new JXMapViewer();
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

        RoutePainter routePainter = new RoutePainter( l_list );
        mapViewer.zoomToBestFit(new HashSet<GeoPosition>( l_list ), 0.7);

        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(routePainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);*/

    }

    public void drawRoutes( List<List<GeoPosition>>  l_routes)
    {
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

        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();

        l_routes.stream().forEach( l -> {
            RoutePainter routePainter = new RoutePainter( l );
            painters.add(routePainter);

            }
        );

        mapViewer.zoomToBestFit(new HashSet<GeoPosition>( l_routes.get(0) ), 0.5);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);
    }

    public GeoPosition randomnode()
    {
        Double l_latitude = ThreadLocalRandom.current().nextDouble( m_bottomright.getLatitude(), m_topleft.getLatitude() );
        Double l_longiture = ThreadLocalRandom.current().nextDouble( m_topleft.getLongitude(), m_bottomright.getLongitude() );
        return new GeoPosition(l_latitude, l_longiture);
    }

    public JXMapViewer panel()
    {
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

        return mapViewer;
    }

}
