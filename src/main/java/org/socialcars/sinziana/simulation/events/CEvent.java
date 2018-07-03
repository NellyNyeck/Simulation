package org.socialcars.sinziana.simulation.events;

/**
 * event class
 */
public class CEvent implements IEvent
{

    private final String m_type;
    private final Number m_where;
    private final Number m_when;
    private final Number m_who;

    /**
     * ctor
     * @param p_type type
     * @param p_where where
     * @param p_when when
     * @param p_who with whom
     */
    public CEvent( final String p_type, final Number p_where, final Number p_when, final Number p_who )
    {
        m_type = p_type;
        m_where = p_where;
        m_when = p_when;
        m_who = p_who;
    }

    @Override
    public String what()
    {
        return m_type;
    }

    @Override
    public Number where()
    {
        return m_where;
    }

    @Override
    public Number with()
    {
        return m_who;
    }

    @Override
    public Number when()
    {
        return m_when;
    }
}
