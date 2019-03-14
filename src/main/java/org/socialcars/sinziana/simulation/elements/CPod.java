package org.socialcars.sinziana.simulation.elements;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
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
 * the class for the pod
 */
public class CPod implements IPod
{

    // TODO: 2019-03-14 add costs and preferences to json schema
    private static final Logger LOGGER = Logger.getLogger( CPod.class.getName() );

    private CPodpojo m_pod;

    private String m_name;
    private String m_provider;
    private Integer m_capacity;

    private String m_start;
    private String m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

    private Double m_speed;
    private Double m_acceleration;
    private String m_location;
    private Double m_position;

    private Double m_maxaccel;
    private Double m_maxdecel;
    private Double m_maxspeed;

    private CPreference m_preference;



    private Collection<IEvent> m_events = new ArrayList<>();


    /**
     * constructor
     * @param p_pod pod pojo
     */
    public CPod( final CPodpojo p_pod, final Integer p_timestep )
    {
        m_pod = p_pod;
        m_speed = 0.00;
        m_location = p_pod.getStart().getName();
        m_start = new CNode( m_pod.getStart() ).id();
        m_finish = new CNode( m_pod.getFinish() ).id();
        m_pod.getMiddle().forEach( m -> m_middle.add( new CNode( m ) ) );
        final CEvent l_created = new CEvent( this, EEvenType.CREATED, m_start, p_timestep, null );
        m_events.add( l_created );
        LOGGER.log( Level.INFO, l_created.toString() );
        m_position = 0.0;
        m_maxaccel = p_pod.getMaxAccel();
        m_maxdecel = p_pod.getMaxDecel();
        m_maxspeed = p_pod.getMaxSpeed();
        m_name = p_pod.getName();
        m_provider = p_pod.getProvider();
        m_capacity = p_pod.getCapacity();

        m_preference = new CPreference( Integer.valueOf( m_finish ), 0.01, m_maxspeed, 1000, 100000.0 );
    }

    @Override
    public String name()
    {
        return m_name;
    }

    @Override
    public  String location()
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
        return m_start;
    }

    @Override
    public String destination()
    {
        return m_finish;
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
        //System.out.println( "Pod " + m_name + "  is one edge " + m_location + " at " + m_position );
    }

    @Override
    public void departed( final IEdge p_edge, final Integer p_timestep )
    {
        final CEvent l_departed = new CEvent( this, EEvenType.DEPART, p_edge.from().id(), p_timestep, null );
        m_events.add( l_departed );
        System.out.println( "Pod " + m_name + " departed node " + p_edge.from().id() + " at timestep " + p_timestep );
        LOGGER.log( Level.INFO, l_departed.toString() );
        m_location = p_edge.id();
    }

    @Override
    public void departed( final GeoPosition p_pos, final Integer p_timestep )
    {
        final CEvent l_departed = new CEvent( this, EEvenType.DEPART, p_pos.toString(), p_timestep, null );
        m_events.add( l_departed );
        System.out.println( "Pod " + m_name + " departed " + p_pos.toString() + " at timestep " + p_timestep );
        LOGGER.log( Level.INFO, l_departed.toString() );
        m_location = p_pos.toString();
    }

    @Override
    public void arrived( final IEdge p_edge, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEvenType.ARRIVED, p_edge.to().id(), p_timestep, null );
        m_events.add( l_arrived );
        System.out.println( "Pod " + m_name + " arrived at node " + p_edge.to().id() + " at timestep " + p_timestep );
        m_location = p_edge.to().id();
        LOGGER.log( Level.INFO, l_arrived.toString() );
        m_position = 0.0;
    }

    @Override
    public void arrived( final GeoPosition p_pos, final Integer p_timestep )
    {
        final CEvent l_arrived = new CEvent( this, EEvenType.ARRIVED, p_pos.toString(), p_timestep, null );
        m_events.add( l_arrived );
        System.out.println( "Pod " + m_name + " arrived at" + p_pos.toString() + " at timestep " + p_timestep );
        m_location = p_pos.toString();
        LOGGER.log( Level.INFO, l_arrived.toString() );
        m_position = 0.0;

    }

    @Override
    public IPreference preferences()
    {
        return m_preference;
    }


    public void seeEvent()
    {
        m_events.forEach( e -> System.out.println( e.what() ) );
    }
}
