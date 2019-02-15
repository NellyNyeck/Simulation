package org.socialcars.sinziana.simulation.environment.jung;

import org.socialcars.sinziana.simulation.data.input.CNodepojo;

import java.util.Objects;


/**
 * the class for the node object
 */
public class CNode implements INode
{

    private final String m_name;

    private final CCoordinate m_coordinates;

    /**
     * constructor
     * @param p_node the pojo node given
     */
    public CNode( final CNodepojo p_node )
    {
        m_name = p_node.getName();
        m_coordinates = new CCoordinate( p_node.getCoordinates() );
    }

    @Override
    public String id()
    {
        return m_name;
    }

    @Override
    public CCoordinate coordinate()
    {
        return m_coordinates;
    }

    @Override
    public int hashCode()
    {
        return m_name.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return Objects.nonNull( p_object ) && ( p_object instanceof INode ) && ( p_object.hashCode() == this.hashCode() );
    }

    @Override
    public String toString()
    {
        return m_name;
    }
}
