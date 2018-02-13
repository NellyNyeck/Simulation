package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.PathWrapper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.elements.IElement;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


public class COSMEnvironment implements IEnvironment<JXMapViewer>
{
    GraphHopper m_hopper;

    public COSMEnvironment( String p_file )
    {
        m_hopper = new GraphHopperOSM().forServer();
        m_hopper.setDataReaderFile( p_file );
        m_hopper.setGraphHopperLocation( String.valueOf(new File("src/test/graphlocation")) );
        m_hopper.setEncodingManager(new EncodingManager("car"));
        m_hopper.importOrLoad();
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final INode... p_via )
    {
        GHRequest request = new GHRequest(
            p_start.coordinate().firstCoordinate().doubleValue(),
            p_start.coordinate().secondCoordinate().doubleValue(),
            p_finish.coordinate().firstCoordinate().doubleValue(),
            p_finish.coordinate().secondCoordinate().doubleValue() ).
            setVehicle("car").
            setLocale(Locale.US);
        GHResponse response = m_hopper.route( request );
        PathWrapper path = response.getBest();
        InstructionList howTo = path.getInstructions();
        final ArrayList<GeoPosition> m_list = new ArrayList<>();
        howTo.forEach( i -> {
            i.getPoints().forEach( j -> {
                m_list.add( new GeoPosition( j.lat, j.lon ) );
            });

        });
        return null;
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final Stream<INode> p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final String... p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final Stream<String> p_via )
    {
        return null;
    }

    @Override
    public String randomnodebyname()
    {
        return null;
    }

    @Override
    public INode randomnode()
    {
        return null;
    }

    @Override
    public IEnvironment initialize( final IElement... p_element )
    {
        return null;
    }

    @Override
    public JXMapViewer panel( final Dimension p_dimension )
    {
        return null;
    }

}
