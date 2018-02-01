package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CPod;
import org.socialcars.sinziana.simulation.environment.CIntersection;

import java.util.List;

/**
 * the class for the pod
 */
public class CPOD implements IPOD
{
    private CPod m_pod;

    private CIntersection m_start;
    private CIntersection m_finish;
    private List<CIntersection> m_middle;

    private Number m_speed;
    private Number m_location;


    /**
     * constructor
     * @param p_pod pod pojo
     */
    public CPOD( final CPod p_pod )
    {
        m_pod = p_pod;
        m_speed = 0;
        m_location = null;
        m_start = new CIntersection( m_pod.getStart() );
        m_finish = new CIntersection( m_pod.getFinish() );
        m_pod.getMiddle().stream().forEach( m ->
        {
            final CIntersection l_mid = new CIntersection( m );
            m_middle.add( l_mid );
        } );
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
    public CIntersection getStart()
    {
        return m_start;
    }

    @Override
    public CIntersection getFinish()
    {
        return m_finish;
    }

    @Override
    public List<CIntersection> getMiddle()
    {
        return m_middle;
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

    @Override
    public Number getCapacity()
    {
        return m_pod.getCapacity();
    }

    @Override
    public String getProvider()
    {
        return m_pod.getProvider();
    }
}
