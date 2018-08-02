package org.socialcars.sinziana.simulation.environment.osm;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CStreetstructpojo;

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
    public CStreetStructure( final CStreetstructpojo p_pojo )
    {
        m_id = p_pojo.getId();
        m_name = p_pojo.getName();
        m_start = new GeoPosition( p_pojo.getStart().get( 0 ).doubleValue(), p_pojo.getStart().get( 1 ).doubleValue() );
        m_end = new GeoPosition( p_pojo.getEnd().get( 0 ).doubleValue(), p_pojo.getEnd().get( 1 ).doubleValue()  );
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
        l_map.put( "start", m_start );
        l_map.put( "end", m_end );
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
