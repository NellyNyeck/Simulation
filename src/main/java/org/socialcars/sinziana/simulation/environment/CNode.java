package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinatespojo;
import org.socialcars.sinziana.simulation.data.input.CStartpojo;

/**
 * the class for the node object
 */
public class CNode implements INode
{

    private final CStartpojo m_node;

    /**
     * constructor
     * @param p_node the pojo node given
     */
    public CNode( final CStartpojo p_node )
    {
        m_node = p_node;
    }

    @Override
    public String name()
    {
        return m_node.getName();
    }

    @Override
    public CCoordinatespojo coord()
    {
        return m_node.getCoordinates();
    }
}
