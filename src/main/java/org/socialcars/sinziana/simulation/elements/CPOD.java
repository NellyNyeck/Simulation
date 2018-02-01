package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CPod;

/**
 * the class for the pod
 */
public class CPOD implements IMovable
{
    private Number m_speed;
    private Number m_location;

    private CPod m_pod;

    public CPOD( final CPod p_pod )
    {
        m_pod = p_pod;
    }

    @Override
    public Number getSpeed()
    {
        return m_speed;
    }

    @Override
    public Number getLocation()
    {
        return m_location;
    }

    @Override
    public void setSpeed( final Number p_speed )
    {
        m_speed = p_speed;
    }

    @Override
    public void setLocation( final Number p_location )
    {
        m_location = p_location;
    }

    @Override
    public Object getStart()
    {
        return m_pod.getStart();
    }

    @Override
    public Object getFinish()
    {
        return m_pod.getFinish();
    }

    @Override
    public Object getMiddle()
    {
        return m_pod.getMiddle();
    }

    @Override
    public Number getMaxAccel()
    {
        return m_pod.getMaxAccel();
    }

    @Override
    public Number getMaxDeccel()
    {
        return m_pod.getMaxDecel();
    }

    @Override
    public Number getMaxSpeed()
    {
        return m_pod.getMaxSpeed();
    }

    @Override
    public String getName()
    {
        return m_pod.getName();
    }

    @Override
    public String getFilename()
    {
        return m_pod.getFilename();
    }

    @Override
    public String getAgentType()
    {
        return m_pod.getAgentType();
    }

    @Override
    public boolean terminate()
    {
        return false;
    }

    @Override
    public IElement call() throws Exception
    {
        return null;
    }
}
