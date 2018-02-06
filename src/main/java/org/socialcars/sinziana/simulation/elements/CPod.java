package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.environment.CNode;

import java.util.ArrayList;

/**
 * the class for the pod
 */
public class CPod implements IPod
{
    private CPodpojo m_pod;

    private CNode m_start;
    private CNode m_finish;
    private ArrayList<CNode> m_middle = new ArrayList<>();

    private Number m_speed;
    private Number m_location;


    /**
     * constructor
     * @param p_pod pod pojo
     */
    public CPod( final CPodpojo p_pod )
    {
        m_pod = p_pod;
        m_speed = 0;
        m_location = null;
        m_start = new CNode( m_pod.getStart() );
        m_finish = new CNode( m_pod.getFinish() );
        m_pod.getMiddle().stream().forEach( m ->
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
