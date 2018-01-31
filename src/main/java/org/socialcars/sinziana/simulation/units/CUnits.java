package org.socialcars.sinziana.simulation.units;

/**
 * the units class
 *
 * @todo hierzu ein Interface bauen
 */
public class CUnits
{

    private final double m_timestep;

    private final double m_stepstep;

    /**
     * ctor
     *
     * @param p_time time given by runtime
     * @param p_space space given by runtime
     *
     */
    CUnits( final Number p_time, final Number p_space )
    {
        m_timestep = p_time.doubleValue();
        m_stepstep = p_space.doubleValue();
    }


    /**
     * transforming acceleration to speed
     * @param p_accel the acceleration given
     * @return the resulting speed
     */
    public final Number accelToSpeed( final Number p_accel )
    {
        return p_accel.doubleValue() * m_timestep;
    }

    /**
     * transforming the distance
     * @param p_dist the given distance
     * @return the resulting distance
     */
    public final Number getDist( final Number p_dist )
    {
        return p_dist.doubleValue() * m_stepstep;
    }

    /**
     * returns the internal tact
     * @return the internal tact
     */
    public final Number getTimestep()
    {
        return m_stepstep;
    }

    /**
     * returns the internal distance step
     * @return the internal distance step
     */
    public final Number getStepstep()
    {
        return m_stepstep;
    }

}
