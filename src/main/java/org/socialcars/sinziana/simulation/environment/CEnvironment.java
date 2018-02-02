package org.socialcars.sinziana.simulation.environment;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;


import org.socialcars.sinziana.simulation.data.input.CEdgepojo;
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
            m_graph.addVertex( new CNode( n ) );
        } );
        final Set<CEdgepojo> l_edges = p_gr.getEdges();
        l_edges.stream().forEach( e ->
        {
        } );
    }

    /**
     * gets the node
     * @param p_name the name of the node
     * @return the node found
     */
    public CNode getNode( final String p_name )
    {
        final CNode l_found = null;
        return l_found;
    }

    /**
     * finds the best route
     * @param p_start start node
     * @param p_finish end node
     * @param p_middle middle list of nodes, not relevant does not apply
     * @return list of edges
     */
    @Override
    public List<? extends IEdge> findBestRoute( final INode p_start, final INode p_finish, final List<? extends INode> p_middle )
    {
        final DijkstraShortestPath<CNode, CEdge> l_dijkstra = new DijkstraShortestPath<>( m_graph );
        //final List<CEdge> l_route = l_dijkstra.getPath( p_start, p_finish );
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
