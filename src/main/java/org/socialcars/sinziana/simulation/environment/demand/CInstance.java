package org.socialcars.sinziana.simulation.environment.demand;

import org.socialcars.sinziana.simulation.data.input.COdpojo;
import org.socialcars.sinziana.simulation.environment.jung.CCoordinate;

/**
 * class for one instance of demand
 */
public class CInstance implements IInstance
{

    private final CCoordinate m_from;

    private final CCoordinate m_to;

    private final double m_howmany;

    /**
     * ctor
     * @param p_pojo the pojo instance given
     */
    public CInstance( final COdpojo p_pojo )
    {
        m_from = new CCoordinate( p_pojo.getFrom() );
        m_to = new CCoordinate( p_pojo.getTo() );
        m_howmany = p_pojo.getNb();
    }


    @Override
    public CCoordinate from()
    {
        return m_from;
    }

    @Override
    public CCoordinate to()
    {
        return m_to;
    }

    @Override
    public Number howMany()
    {
        return m_howmany;
    }
}
