package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;
import org.socialcars.sinziana.simulation.events.CEvent;
import org.socialcars.sinziana.simulation.events.EEvenType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * the class for the pod
 */
public class CPod implements IPod
{

    private static final Logger LOGGER = Logger.getLogger( CPod.class.getName() );

    private CPodpojo m_pod;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

    private Double m_speed;
    private Double m_acceleration;
    private String m_location;

    private Collection<CEvent> m_events;


    /**
     * constructor
     * @param p_pod pod pojo
     */
    public CPod( final CPodpojo p_pod )
    {
        m_pod = p_pod;
        m_speed = 0.00;
        m_location = null;
        m_start = new CNode( m_pod.getStart() );
        m_finish = new CNode( m_pod.getFinish() );
        m_pod.getMiddle().stream().forEach( m ->
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
    public void move( final String p_newposition )
    {
        m_location = p_newposition;
    }

    @Override
    public void event( final CEvent p_event )
    {
        m_events.add( p_event );
        LOGGER.log( Level.INFO, p_event.toString() );
    }
}
