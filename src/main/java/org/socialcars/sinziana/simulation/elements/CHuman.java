package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CHumanpojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;
import org.socialcars.sinziana.simulation.events.CEvent;
import org.socialcars.sinziana.simulation.events.EEvenType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the human class
 */
public class CHuman implements IHuman
{
    private static final Logger LOGGER = Logger.getLogger( CHuman.class.getName() );


    private CHumanpojo m_human;

    private CNode m_start;
    private CNode m_finish;
    private final ArrayList<CNode> m_middle = new ArrayList<>();

    private Double m_speed;
    private Double m_acceleration;
    private String m_location;

    private Collection<CEvent> m_events;


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
        event( new CEvent( this, EEvenType.CREATED, m_start, System.currentTimeMillis(), null ) );
    }

    @Override
    public String position()
    {
        return m_location;
    }

    @Override
    public Double speed()
    {
        return m_speed;
    }

    @Override
    public Double acceleration()
    {
        return m_acceleration;
    }

    @Override
    public String destination()
    {
        return m_finish.id();
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
    public void move( final String p_newpostion )
    {
        m_location = p_newpostion;
    }

    @Override
    public void event( final CEvent p_event )
    {
        m_events.add( p_event );
        LOGGER.log( Level.INFO, p_event.toString() );
    }
}
