package org.socialcars.sinziana.simulation.elements;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.events.CEvent;
import org.socialcars.sinziana.simulation.events.EEvenType;
import org.socialcars.sinziana.simulation.events.IEvent;
import org.socialcars.sinziana.simulation.units.CUnits;

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

    private String m_name;
    private Double m_maxaccel;
    private Double m_maxdecel;
    private Double m_maxspeed;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

    private Double m_speed;
    private Double m_acceleration;
    private String m_location;
    private Double m_position;

    private Collection<IEvent> m_events;


    /**
     * constructor
     * @param p_bike bike pojo
     */
    public CBike( final CBicyclepojo p_bike, final Integer p_timestep )
    {
        m_bike = p_bike;
        m_start = new CNode( m_bike.getStart() );
        m_finish = new CNode( m_bike.getFinish() );
        m_bike.getMiddle().forEach( n -> m_middle.add( new CNode( n ) ) );

        m_maxaccel = p_bike.getMaxAccel();
        m_maxdecel = p_bike.getMaxDecel();
        m_maxspeed = p_bike.getMaxSpeed();
        m_name = p_bike.getName();

        final CEvent l_created = new CEvent( this, EEvenType.CREATED, m_start.id(), p_timestep, null );
        m_events.add( l_created );
        LOGGER.log( Level.INFO, l_created.toString() );
        m_position = 0.0;
    }

    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public String location()
    {
        return m_location;
    }

    @Override
    public Double position()
    {
        return m_position;
    }

    @Override
    public String origin()
    {
        return m_start.id();
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
    public IElement call()
    {
        return null;
    }

    @Override
    public void move( final CUnits p_unit )
    {
        if (  m_speed < m_maxspeed )
        {
            m_acceleration = m_maxaccel;
            m_speed = m_speed + p_unit.accelerationToSpeed( m_acceleration ).doubleValue();
            m_position = m_position + p_unit.speedToBlocks( m_speed ).doubleValue();
        }
        else if ( m_speed > m_maxspeed )
        {
            m_acceleration = m_acceleration - m_maxdecel;
            m_speed =  m_speed + p_unit.accelerationToSpeed( m_acceleration ).doubleValue();
            m_position = m_position + p_unit.speedToBlocks( m_speed ).doubleValue();
        }
        else
        {
            m_position = m_position + p_unit.speedToBlocks( m_speed ).doubleValue();
        }
    }

    @Override
    public void departed( final IEdge p_edge, final Integer p_timestep )
    {
        final CEvent l_departed = new CEvent( this, EEvenType.DEPART, p_edge.from().id(), p_timestep, null );
        m_events.add( l_departed );
        LOGGER.log( Level.INFO, l_departed.toString() );
        m_location = p_edge.id();
    }

    @Override
    public void departed( final GeoPosition p_pos, final Integer p_timestep )
    {
        final CEvent l_departed = new CEvent( this, EEvenType.DEPART, p_pos.toString(), p_timestep, null );
        m_events.add( l_departed );
        LOGGER.log( Level.INFO, l_departed.toString() );
        m_location = p_pos.toString();
    }

    @Override
    public void arrived( final IEdge p_edge, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEvenType.ARRIVED, p_edge.to().id(), p_timestep, null );
        m_events.add( l_arrived );
        LOGGER.log( Level.INFO, l_arrived.toString() );
        m_position = 0.0;
    }

    @Override
    public void arrived( final GeoPosition p_pos, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEvenType.ARRIVED, p_pos.toString(), p_timestep, null );
        m_events.add( l_arrived );
        LOGGER.log( Level.INFO, l_arrived.toString() );
        m_position = 0.0;

    }
}
