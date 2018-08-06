package org.socialcars.sinziana.simulation.environment.osm;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CStreetpojo;

import java.util.HashMap;
import java.util.Map;

/**
 * class for keeping streets
 */
public class CStreetStructure
{
    private final Integer m_id;
    private final String m_name;
    private final GeoPosition m_start;
    private final GeoPosition m_end;

    /**
     * ctor
     * @param p_pojo street structure plain java object
     */
    public CStreetStructure( final CStreetpojo p_pojo )
    {
        m_id = p_pojo.getId();
        m_name = p_pojo.getName();
        m_start = new GeoPosition( p_pojo.getStart().getLat(), p_pojo.getStart().getLon() );
        m_end = new GeoPosition( p_pojo.getEnd().getLat(), p_pojo.getEnd().getLon() );
    }

    /**
     * ctor
     * @param p_id id
     * @param p_name name
     * @param p_start start
     * @param p_end end
     */
    public CStreetStructure( final Integer p_id, final String p_name, final GeoPosition p_start, final GeoPosition p_end )
    {
        m_id = p_id;
        m_name = p_name;
        m_start = p_start;
        m_end = p_end;
    }

    /**
     * maps the street to a json object
     * @return a map as json
     */
    public Map<String, Object> toMap()
    {
        final HashMap<String, Object> l_map = new HashMap<>();
        l_map.put( "id", m_id );
        l_map.put( "name", m_name );
        HashMap<String, Double> l_coord = new HashMap<String, Double>();
        l_coord.put( "lat", m_start.getLatitude() );
        l_coord.put( "lon", m_start.getLongitude() );
        l_map.put( "start", l_coord );
        l_coord = new HashMap<>();
        l_coord.put( "lat", m_end.getLatitude() );
        l_coord.put( "lon", m_end.getLongitude() );
        l_map.put( "end", l_coord );
        return l_map;
    }

    public GeoPosition start()
    {
        return m_start;
    }

    public GeoPosition end()
    {
        return m_end;
    }

}
