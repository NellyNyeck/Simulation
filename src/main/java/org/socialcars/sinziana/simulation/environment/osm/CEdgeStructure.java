package org.socialcars.sinziana.simulation.environment.osm;

import org.jxmapviewer.viewer.GeoPosition;

/**
 * class for edge structures in OSM
 */
public class CEdgeStructure implements IEdgeStructure
{
    private final Integer m_id;
    private final String m_name;
    private final GeoPosition m_start;
    private final GeoPosition m_end;

    /**
     * ctor
     * @param p_id id
     * @param p_name name
     * @param p_start start geoposition
     * @param p_end end geoposition
     */
    public CEdgeStructure( final int p_id, final String p_name, final GeoPosition p_start, final GeoPosition p_end )
    {
        m_id = p_id;
        m_name = p_name;
        m_start = p_start;
        m_end = p_end;
    }

    @Override
    public int id()
    {
        return m_id;
    }

    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public GeoPosition start()
    {
        return m_start;
    }

    @Override
    public GeoPosition end()
    {
        return m_end;
    }
}
