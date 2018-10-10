package org.socialcars.sinziana.simulation.environment.demand.jung;

import org.socialcars.sinziana.simulation.data.input.CDemandjungpojo;

import java.util.logging.Logger;

/**
 * class for jung demands
 */
public class CInstanceJung implements IInstanceJung
{

    private static final Logger LOGGER = Logger.getLogger( CInstanceJung.class.getName() );

    private final String m_from;
    private final String m_to;
    private final Integer m_howmany;

    /**
     * ctor
     * @param p_pojo pojo object
     */
    public CInstanceJung( final CDemandjungpojo p_pojo )
    {
        m_from = p_pojo.getFrom();
        m_to = p_pojo.getTo();
        m_howmany = p_pojo.getNb();
    }

    @Override
    public String from()
    {
        return m_from;
    }

    @Override
    public String to()
    {
        return m_to;
    }

    @Override
    public int howMany()
    {
        return m_howmany;
    }
}
