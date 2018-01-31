package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinates;
import org.socialcars.sinziana.simulation.data.input.CStart;

/**
 * the class for the node object
 */
public class CIntersection implements IIntersection<String>
{

    private final CStart m_node;

    /**
     * constructor
     * @param p_node the pojo node given
     */
    CIntersection( final CStart p_node )
    {
        m_node = p_node;
    }

    @Override
    public String name()
    {
        return m_node.getName();
    }

    @Override
    public CCoordinates coord()
    {
        return m_node.getCoordinates();
    }
}
