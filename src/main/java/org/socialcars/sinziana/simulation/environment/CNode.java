package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CStartpojo;

/**
 * the class for the node object
 */
public class CNode implements INode
{

    private final CStartpojo m_node;

    private final CCoordinate m_coordinates;

    /**
     * constructor
     * @param p_node the pojo node given
     */
    public CNode( final CStartpojo p_node )
    {
        m_node = p_node;
        m_coordinates = new CCoordinate( p_node.getCoordinates() );
    }



    @Override
    public String id()
    {
        return m_node.getName();
    }

    @Override
    public CCoordinate coordinate()
    {
        return m_coordinates;
    }

}
