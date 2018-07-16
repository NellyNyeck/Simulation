package org.socialcars.sinziana.simulation.environment.jung;

import org.socialcars.sinziana.simulation.data.input.CCoordinatespojo;

/**
 * coordinates class
 */
public class CCoordinate implements ICoordinate
{


    private CCoordinatespojo m_coordinate;

    /**
     * constructor
     * @param p_coord coordinate pojo given
     */
    public CCoordinate( final CCoordinatespojo p_coord )
    {
        m_coordinate = p_coord;
    }

    @Override
    public Double latitude()
    {
        return m_coordinate.getLat();
    }

    @Override
    public Double longitude()
    {
        return m_coordinate.getLon();
    }

}
