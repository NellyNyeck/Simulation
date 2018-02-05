package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.function.CFunction;
import org.socialcars.sinziana.simulation.function.IFunction;
import org.socialcars.sinziana.simulation.data.input.CEdgepojo;

/**
 * the street class
 */
public class CEdge implements IEdge
{

    private CEdgepojo m_edge;

    private CNode m_from;

    private CNode m_to;

    private CFunction m_function;

    /**
     * constructor
     * @param p_edge edge pojo
     * @param p_from from node
     * @param p_to to node
     */
    public CEdge( final CEdgepojo p_edge, final INode p_from,  final INode p_to )
    {
        m_edge = p_edge;
        //m_from = p_from;
        //m_to = p_to;
        // TODO: 05.02.18 figure this out
        //m_function = p_edge.getFunction();
    }


    @Override
    public String id()
    {
        return m_edge.getName();
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
        return m_edge.getWeight();
    }

    @Override
    public IFunction<Number> function()
    {
        return m_function;
    }

}
