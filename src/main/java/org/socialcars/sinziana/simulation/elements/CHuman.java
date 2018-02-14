package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CHumanpojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;

import java.util.ArrayList;

/**
 * the human class
 */
public class CHuman implements IHuman
{
    private CHumanpojo m_human;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

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
