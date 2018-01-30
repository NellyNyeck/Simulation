package org.socialcars.sinziana.simulation.units;

/**
 * the units class
 */
public class CUnits
{

    private final double m_timestep;

    private final double m_stepstep;

    CUnits( final Double p_time, final Double p_space )
    {
        m_timestep = p_time;
        m_stepstep = p_space;
    }

    public double accel2speed( final double p_accel )
    {
        return p_accel * m_timestep;
    }

    public double getDist( final double p_dist )
    {
        return p_dist * m_stepstep;
    }

    public double getTimestep()
    {
        return m_stepstep;
    }

    public double getStepstep()
    {
        return m_stepstep;
    }

}
