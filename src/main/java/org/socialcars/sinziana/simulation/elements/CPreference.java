package org.socialcars.sinziana.simulation.elements;

/**
 * Preference class
 */
public class CPreference implements IPreference
{
    private final Integer m_dest;
    private final Double m_minspeed;
    private final Double m_maxspeed;
    private final Integer m_timelimit;
    private final Double m_lengthlimit;

    /**
     * ctor
     * @param p_dest destination
     * @param p_minspeed minimum speed
     * @param p_maxspeed maximum speed
     * @param p_timelimit time limit
     * @param p_lengthlimit length limit
     */
    public CPreference( final Integer p_dest, final Double p_minspeed, final Double p_maxspeed, final Integer p_timelimit, final Double p_lengthlimit )
    {
        m_dest = p_dest;
        m_minspeed = p_minspeed;
        m_maxspeed = p_maxspeed;
        m_timelimit = p_timelimit;
        m_lengthlimit = p_lengthlimit;
    }

    @Override
    public Integer destination()
    {
        return m_dest;
    }

    @Override
    public Double minSpeed()
    {
        return m_minspeed;
    }

    @Override
    public Double maxSpeed()
    {
        return m_maxspeed;
    }

    @Override
    public Integer timeLimit()
    {
        return m_timelimit;
    }

    @Override
    public Double lengthLimit()
    {
        return m_lengthlimit;
    }
}
