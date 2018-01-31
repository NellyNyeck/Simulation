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
     * @param p_time time step
     * @param p_space space?
     *
     * @todo bitte mal überlegen was der UNterschied zwischen double und Double ist, Du mischst hier
     */
    CUnits( final Double p_time, final Double p_space )
    {
        m_timestep = p_time;
        m_stepstep = p_space;
    }

    /**
     * ???
     * @param p_accel was ist das?
     * @return ???
     * @todo bitte mal auf ordentliche Benennung der Methoden und Parameter achten!
     * Was heisst "p_accel" ? und was heisst "accel2speed"?
     * @toto bitte mal lesen: http://www.programmierenlernenhq.de/java-programmieren-lernen-vergabe-von-namen-fur-klassen-methoden-und-variablen-in-java/
     */
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
