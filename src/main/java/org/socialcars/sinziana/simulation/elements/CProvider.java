package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CProviderpojo;
import org.socialcars.sinziana.simulation.environment.CNode;
import org.socialcars.sinziana.simulation.environment.INode;


import java.util.Collection;
import java.util.HashSet;


/**
 * the provider lightjason class
 */
public class CProvider implements IProvider
{

    private final CProviderpojo m_provider;

    private final CNode m_location;

    private HashSet<CNode> m_depots;

    private HashSet<CPod> m_pods;

    /**
     * constructor
     * @param p_provider provider pojo
     */
    public CProvider( final CProviderpojo p_provider )
    {
        m_provider = p_provider;
        m_location = new CNode( p_provider.getLocation() );
        m_depots = new HashSet<>();
        m_pods = new HashSet<>();
        m_provider.getDepots().stream().forEach( d ->
        {
            final CNode l_dep = new CNode( d );
            m_depots.add( l_dep );
        } );
        m_provider.getPods().stream().forEach( p ->
        {
            final CPod l_pod = new CPod( p );
            m_pods.add( l_pod );
        } );
    }

    @Override
    public String colour()
    {
        return m_provider.getColour();
    }

    @Override
    public Collection<? extends INode> depots()
    {
        return m_depots;
    }

    @Override
    public Collection<? extends IPod> pods()
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
    public CNode location()
    {
        return m_location;
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
