package org.socialcars.sinziana.simulation.environment;


import org.socialcars.sinziana.simulation.data.input.CEdge;

/**
 * the street class
 */
public class CStreet implements IStreet
{

    private CEdge m_edge;

    CStreet( final CEdge p_edge )
    {
        m_edge = p_edge;
    }

    @Override
    public String name()
    {
        return m_edge.getName();
    }

    @Override
    public String from()
    {
        return m_edge.getFrom();
    }

    @Override
    public String to()
    {
        return m_edge.getTo();
    }

    @Override
    public Number weight()
    {
        return m_edge.getWeight();
    }

    @Override
    public Object function()
    {
        return m_edge.getFunction();
    }
}
