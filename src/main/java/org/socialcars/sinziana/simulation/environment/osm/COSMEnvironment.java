package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.InstructionList;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;


import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


public class COSMEnvironment
{
    GraphHopper m_hopper;

    public COSMEnvironment( final String p_file )
    {
        m_hopper = new GraphHopperOSM().forServer();
        m_hopper.setDataReaderFile( p_file );
        m_hopper.setGraphHopperLocation( String.valueOf(new File( "src/test/graphlocation" ) ) );
        m_hopper.setEncodingManager( new EncodingManager( "car" ) );
        m_hopper.importOrLoad();
    }


    public List<GeoPosition> route( final GeoPosition p_start, final GeoPosition p_finish, final GeoPosition... p_via )
    {
        return this.route( p_start, p_finish, java.util.Objects.isNull( p_via ) ? Stream.empty() : java.util.Arrays.stream( p_via ) );
    }

    public List<GeoPosition> route( final GeoPosition p_start, final GeoPosition p_finish, final Stream<GeoPosition> p_via )
    {
        final ArrayList<GeoPosition> m_list = new ArrayList<>();
        return com.codepoetics.protonpack.StreamUtils.windowed(
            Stream.concat(
                Stream.concat(
                    Stream.of( p_start ),
                    p_via
                ),
                Stream.of( p_finish )
            ),
        2
        ).flatMap(
            i ->
            {
                GHRequest request = new GHRequest(
                    i.get(0).getLatitude(),
                    i.get(0).getLongitude(),
                    i.get(1).getLatitude(),
                    i.get(1).getLongitude()).
                    setVehicle("car").
                    setLocale(Locale.US);
                GHResponse response = m_hopper.route( request );
                PathWrapper path = response.getBest();
                InstructionList howTo = path.getInstructions();
                howTo.forEach( m -> {
                    m.getPoints().forEach( j -> {
                        m_list.add( new GeoPosition( j.lat, j.lon ) );
                    } );
                } );
            }

        );
        return m_list;

    }

    public GeoPosition randomnode()
    {
        return null;
    }

    public JXMapViewer panel( final Dimension p_dimension )
    {
        return null;
    }

}
