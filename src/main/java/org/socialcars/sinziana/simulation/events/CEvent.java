package org.socialcars.sinziana.simulation.events;

import org.socialcars.sinziana.simulation.elements.IMovable;

import java.util.Collection;

/**
 * event class
 */
public class CEvent implements IEvent
{

    private final IMovable m_who;
    private final EEvenType m_type;
    private final String m_where;
    private final Number m_when;
    private final Collection<IMovable> m_with;

    /**
     * ctor
     * @param p_type type
     * @param p_where where
     * @param p_when when
     * @param p_who with whom
     */
    public CEvent( final IMovable p_who, final EEvenType p_type, final String p_where, final Number p_when, final Collection<IMovable> p_with )
    {
        m_who = p_who;
        m_type = p_type;
        m_where = p_where;
        m_when = p_when;
        m_with = p_with;
    }


    @Override
    public IMovable who()
    {
        return m_who;
    }

    @Override
    public EEvenType what()
    {
        return m_type;
    }

    @Override
    public String where()
    {
        return m_where;
    }

    @Override
    public Collection<IMovable> with()
    {
        return m_with;
    }

    @Override
    public Number when()
    {
        return m_when;
    }

}
