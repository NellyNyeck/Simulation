package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.environment.CNode;
import org.socialcars.sinziana.simulation.environment.INode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * the bike class
 */
public class CBike implements IBike
{
    private CBicyclepojo m_bike;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle;

    private Number m_speed;
    private Number m_location;

    /**
     * constructor
     * @param p_bike bike pojo
     */
    public CBike( final CBicyclepojo p_bike )
    {
        m_bike = p_bike;
        m_start = new CNode( m_bike.getStart() );
        m_finish = new CNode( m_bike.getFinish() );
        m_bike.getMiddle().stream().forEach( n -> m_middle.add( new CNode( n ) ) );
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
    public INode getStart()
    {
        return m_start;
    }

    @Override
    public INode getFinish()
    {
        return m_finish;
    }

    @Override
    public Collection<? extends INode> getMiddle()
    {
        return m_middle;
    }

    @Override
    public Number getMaxAccel()
    {
        return m_bike.getMaxAccel();
    }

    @Override
    public Number getMaxDeccel()
    {
        return m_bike.getMaxDecel();
    }

    @Override
    public Number getMaxSpeed()
    {
        return m_bike.getMaxSpeed();
    }

    @Override
    public String getName()
    {
        return m_bike.getName();
    }

    @Override
    public String getFilename()
    {
        return m_bike.getFilename();
    }

    @Override
    public String getAgentType()
    {
        return m_bike.getAgentType();
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
