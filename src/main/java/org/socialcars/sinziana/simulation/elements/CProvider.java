package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CProviderpojo;
import org.socialcars.sinziana.simulation.environment.CNode;

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
