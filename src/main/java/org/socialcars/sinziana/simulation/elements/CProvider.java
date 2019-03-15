package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CProviderpojo;
import org.socialcars.sinziana.simulation.environment.jung.CNode;

import java.util.HashSet;
import java.util.logging.Logger;


/**
 * the provider lightjason class
 */
public class CProvider implements IProvider
{

    private static final Logger LOGGER = Logger.getLogger( CProvider.class.getName() );

    private final CProviderpojo m_provider;

    private HashSet<CNode> m_depots;

    private HashSet<CPod> m_pods;



    /**
     * constructor
     * @param p_provider provider pojo
     */
    public CProvider( final CProviderpojo p_provider )
    {
        m_provider = p_provider;
        m_depots = new HashSet<>();
        m_pods = new HashSet<>();
        m_provider.getDepots().forEach( d ->
        {
            final CNode l_dep = new CNode( d );
            m_depots.add( l_dep );
        } );
        m_provider.getPods().forEach( p ->
        {
            final CPod l_pod = new CPod( p, 0 );
            m_pods.add( l_pod );
        } );
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
}
