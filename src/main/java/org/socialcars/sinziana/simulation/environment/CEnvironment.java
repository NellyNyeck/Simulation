package org.socialcars.sinziana.simulation.environment;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import org.socialcars.sinziana.simulation.data.input.CGraph;
import org.socialcars.sinziana.simulation.data.input.CStart;
import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;
import java.util.Set;

/**
 * the environment class
 * @param <I> generic for intersection identifier
 * @param <V> generic for intersection
 * @param <E> generic for street
 */
public class CEnvironment<I, V extends INode<I>, E extends IStreet> implements IEnvironment<I, V, E>
{

    private final Graph<V, E> m_graph = new DirectedSparseGraph<>();

    /**
     * construnctor
     * @param p_gr the graph pojo given
     */
    public CEnvironment( final CGraph p_gr )
    {
        final Set<CStart> l_nodes = p_gr.getNodes();
        l_nodes.stream().forEach( n ->
        {
            final CIntersection l_int = new CIntersection( n );
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
    public List<E> findBestRoute( final V p_start, final V p_finish, final List<V> p_middle )
    {
        final DijkstraShortestPath<V, E> l_alg = new DijkstraShortestPath<V, E>( m_graph );
        final List<E> l_rou = l_alg.getPath( p_start, p_finish );
        return l_rou;
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
