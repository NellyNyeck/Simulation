package org.socialcars.sinziana.simulation.environment.jung;

import org.socialcars.sinziana.simulation.data.input.CEdgepojo;

import java.text.MessageFormat;
import java.util.Objects;


/**
 * the street class
 */
public class CEdge implements IEdge
{

    private final String m_name;

    private INode m_from;

    private INode m_to;

    private final Double m_weight;

    /**
     * constructor
     * @param p_edge the edge pojo
     * @param p_from origin node
     * @param p_to destination node
     */
    public CEdge( final CEdgepojo p_edge, final INode p_from,  final INode p_to )
    {
        m_name = p_edge.getName();
        m_from = p_from;
        m_to = p_to;
        m_weight = p_edge.getWeight();
    }

    @Override
    public String id()
    {
        return m_name;
    }

    @Override
    public INode from()
    {
        return m_from;
    }

    @Override
    public INode to()
    {
        return m_to;
    }

    @Override
    public Number weight()
    {
        return m_weight;
    }

    @Override
    public int hashCode()
    {
        return m_name.hashCode();
    }

    @Override
    public boolean equals( final Object p_object )
    {
        return Objects.nonNull( p_object ) && ( p_object instanceof IEdge ) && ( p_object.hashCode() == this.hashCode() );
    }

    @Override
    public String toString()
    {
        return MessageFormat.format( "{0}({1})", m_name, m_weight );
    }

    @Override
    public Double length()
    {
        return Math.sqrt( Math.pow( m_from.coordinate().latitude() - m_to.coordinate().latitude(), 2 )
            + Math.pow( m_from.coordinate().longitude() - m_to.coordinate().longitude(), 2 ) );
    }

    @Override
    public String orientation()
    {
        if ( m_from.coordinate().latitude() > m_to.coordinate().latitude() ) return "left";
        else if ( m_from.coordinate().latitude() < m_to.coordinate().latitude() ) return "right";
        else
        {
            if ( m_from.coordinate().longitude() < m_to.coordinate().longitude() ) return "up";
            else return "down";
        }
    }
}
