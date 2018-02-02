package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CProvider;
import org.socialcars.sinziana.simulation.environment.CIntersection;


import java.util.ArrayList;
import java.util.List;


/**
 * the provider lightjason class
 */
public class CDeliverer implements IProvider
{

    private final CProvider m_provider;

    private ArrayList<CIntersection> m_depots;

    private ArrayList<CPOD> m_pods;

    /**
     * constructor
     * @param p_provider provider pojo
     */
    public CDeliverer( final CProvider p_provider )
    {
        m_provider = p_provider;
        m_depots = new ArrayList<>();
        m_pods = new ArrayList<>();
        m_provider.getDepots().stream().forEach( d ->
        {
            final CIntersection l_dep = new CIntersection( d );
            m_depots.add( l_dep );
        } );
        m_provider.getPods().stream().forEach( p ->
        {
            final CPOD l_pod = new CPOD( p );
            m_pods.add( l_pod );
        } );
    }

    @Override
    public String colour()
    {
        return m_provider.getColour();
    }

    @Override
    public List<CIntersection> depots()
    {
        return m_depots;
    }

    @Override
    public List<CPOD> pods()
    {
        return m_pods;
    }

    @Override
    public Number maxClients()
    {
        return m_provider.getMaximumCustomers();
    }

    @Override
    public Number maxPodsTime()
    {
        return m_provider.getMaxOutPodsTime();
    }

    @Override
    public CIntersection location()
    {
        return m_depots.get( 0 );
    }

    @Override
    public String getName()
    {
        return m_provider.getName();
    }

    @Override
    public String getFilename()
    {
        return m_provider.getFilename();
    }

    @Override
    public String getAgentType()
    {
        return m_provider.getAgentType();
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
