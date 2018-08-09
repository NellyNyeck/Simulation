package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;
import org.socialcars.sinziana.simulation.events.CEvent;
import org.socialcars.sinziana.simulation.events.EEvenType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the bike class
 */
public class CBike implements IBike
{
    private static final Logger LOGGER = Logger.getLogger( CBike.class.getName() );

    private CBicyclepojo m_bike;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

    private Double m_speed;
    private Double m_acceleration;
    private String m_location;

    private Collection<CEvent> m_events;


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
    public void accelshift( final Double p_accel )
    {
        m_acceleration = p_accel;
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
