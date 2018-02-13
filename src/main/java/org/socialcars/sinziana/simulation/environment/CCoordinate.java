package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinatespojo;

/**
 * coordinates class
 */
// TODO: 06.02.18 delete this
public class CCoordinate implements ICoordinate
{
    private CCoordinatespojo m_coordinate;

    /**
     * constructor
     * @param p_coord coordinate pojo given
     */
    CCoordinate( final CCoordinatespojo p_coord )
    {
        m_coordinate = p_coord;
    }

    public CCoordinate( final Double p_latitude, final Double p_longitude )
    {
        m_coordinate.setType( "GPS" );
        m_coordinate.setFirstCoordinate( p_latitude );
        m_coordinate.setSecondCoordinate( p_longitude );
    }

    @Override
    public String type()
    {
        return m_coordinate.getType();
    }

    @Override
    public Number firstCoordinate()
    {
        return m_coordinate.getFirstCoordinate();
    }

    @Override
    public Number secondCoordinate()
    {
        return m_coordinate.getFirstCoordinate();
    }
}
