package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CHumanpojo;
import org.socialcars.sinziana.simulation.environment.CNode;
import org.socialcars.sinziana.simulation.environment.INode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * the human class
 */
public class CHuman implements IHuman
{
    private CHumanpojo m_human;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle;

    private Number m_speed;
    private Number m_location;

    /**
     * constructor
     * @param p_human human pojo
     */
    public CHuman( final CHumanpojo p_human )
    {
        m_human = p_human;
        m_start = new CNode( p_human.getStart() );
        m_finish = new CNode( p_human.getFinish() );
        m_human.getMiddle().stream().forEach( m ->
        {
            m_middle.add( new CNode( m ) );
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
        return m_human.getMaxAccel();
    }

    @Override
    public Number getMaxDeccel()
    {
        return m_human.getMaxDecel();
    }

    @Override
    public Number getMaxSpeed()
    {
        return m_human.getMaxSpeed();
    }

    @Override
    public String getName()
    {
        return m_human.getName();
    }

    @Override
    public String getFilename()
    {
        return m_human.getFilename();
    }

    @Override
    public String getAgentType()
    {
        return m_human.getAgentType();
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
