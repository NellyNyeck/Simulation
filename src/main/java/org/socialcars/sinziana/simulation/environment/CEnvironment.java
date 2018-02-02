package org.socialcars.sinziana.simulation.environment;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;


import org.socialcars.sinziana.simulation.data.input.CGraphpojo;
import org.socialcars.sinziana.simulation.data.input.CStartpojo;
import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;
import java.util.Set;

/**
 * the environment class
 */
public class CEnvironment implements IEnvironment
{

    private final Graph<CNode, CEdge> m_graph = new DirectedSparseGraph<>();

    /**
     * construnctor
     * @param p_gr the graph pojo given
     */
    public CEnvironment( final CGraphpojo p_gr )
    {
        final Set<CStartpojo> l_nodes = p_gr.getNodes();
        l_nodes.stream().forEach( n ->
        {
            final CNode l_int = new CNode( n );
            //m_graph.addVertex( l_int ); U WOT MATE?!?!?!?!!
        } );
    }

    /**
     * finds the best route
     * @param p_start start node
     * @param p_finish end node
     * @param p_middle middle list of nodes, not relevant does not apply
     * @return list of edges
     */

    @Override
    public List<IEdge> findBestRoute( final INode p_start, final INode p_finish, final List<INode> p_middle )
    {
        return null;
    }

    /**
     * puts all agents in the environment
     * @param p_el the set of elements
     */
    @Override
    public void initSet( final IElement p_el )
    {

    }
}
