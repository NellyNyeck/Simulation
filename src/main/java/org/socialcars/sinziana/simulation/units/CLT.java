package org.socialcars.sinziana.simulation.units;


import java.time.Duration;
import java.time.LocalTime;

/**
 * class for time-keeping
 */
public class CLT implements ILT
{
    private final LocalTime m_zdt;

    public CLT( final LocalTime p_time )
    {
        m_zdt = p_time;
    }

    @Override
    public LocalTime timePassed( final LocalTime p_targettime )
    {
        LocalTime l_passed = m_zdt;
        l_passed = l_passed.minusHours( p_targettime.getHour() );
        l_passed = l_passed.minusMinutes( p_targettime.getMinute() );
        l_passed = l_passed.minusSeconds( p_targettime.getSecond() );
        l_passed = l_passed.minusNanos( p_targettime.getNano() );
        return l_passed;
    }

    @Override
    public LocalTime timeUntil( final LocalTime p_targettime )
    {
        LocalTime l_until = m_zdt;
        l_until = l_until.plusHours( p_targettime.getHour() );
        l_until = l_until.plusMinutes( p_targettime.getMinute() );
        l_until = l_until.plusSeconds( p_targettime.getSecond() );
        l_until = l_until.plusNanos( p_targettime.getNano() );
        return l_until;
    }

    @Override
    public LocalTime timeAfter( final Duration p_duration )
    {
        return m_zdt.plus( p_duration );
    }
}
