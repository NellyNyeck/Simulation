package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CBicyclepojo;
import org.socialcars.sinziana.simulation.environment.CNode;

import java.util.ArrayList;

/**
 * the bike class
 */
public class CBike implements IBike
{
    private CBicyclepojo m_bike;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

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
