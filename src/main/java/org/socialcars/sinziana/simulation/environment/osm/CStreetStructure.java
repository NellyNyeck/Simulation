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


    /**
     * ctor
     * @param p_pojo street structure plain java object
     */
    public CStreetStructure( final CStreetpojo p_pojo )
    {
        m_id = p_pojo.getId();
        m_name = p_pojo.getName();
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
        return l_map;
    }

    /*public GeoPosition start()
    {
        return new GeoPosition( m_startlat, m_startlong );
    }

    public GeoPosition end()
    {
        return new GeoPosition( m_endlat, m_endlong );
    }**/

}
