package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CEdgepojo;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;


/**
 * the street class
 */
public class CEdge implements IEdge
{

    private final String m_name;

    private INode m_from;

    private INode m_to;

    private final AtomicReference<Number> m_weight = new AtomicReference<>();

    /**
     * constructor
     * @param p_edge edge pojo
     * @param p_from from node
     * @param p_to to node
     */
    public CEdge( final CEdgepojo p_edge, final INode p_from,  final INode p_to )
    {
        m_name = p_edge.getName();
        m_from = p_from;
        m_to = p_to;
        m_weight.set( p_edge.getWeight() );
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
        return m_weight.get();
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
        return MessageFormat.format( "{0}({1})", m_name, m_weight.get() );
    }

}
