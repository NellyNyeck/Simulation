package environment;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;
import java.util.List;

/**
 *defining the class graph
 * @param <I> dunno what this is
 * @param <V> Node/Vertex generic
 * @param <E> Edge generic
 * Graph class
 */
public final class CGraph<I, V extends INode<I>, E extends IEdge> implements IGraph<I, V, E>
{
    private final Graph<V, E> m_graph = new DirectedSparseGraph<>();

    public CGraph( )
    {
    }


    public void addNode( final V v )
    {
        m_graph.addVertex( v );
    }

    /**
     * fetching the node with the desired id
     * @param n the id of the node that needs to be returned
     * @return the node with the given n id
     */
    public V getNode( final Number n )
    {
        final Collection<V> l_all = m_graph.getVertices();
        for ( final V l_node : l_all )
        {
            if ( l_node.id() == n ) return l_node;
        }
        return null;
    }

    @Override
    public Collection<V> getNodes()
    {
        return m_graph.getVertices();
    }

    @Override
    public void addEdge( final V p_v1, final V p_v2, final E e )
    {
        m_graph.addEdge( e, p_v1, p_v2 );
    }

    @Override
    public Collection<E> getEdges()
    {
        return m_graph.getEdges();
    }

    @Override
    public int countEdges()
    {
        return m_graph.getEdgeCount();
    }

    @Override
    public int countNodes()
    {
        return m_graph.getVertexCount();
    }

    @Override
    public List<CEdge> route(final I p_start, final I p_end )
    {
        final DijkstraShortestPath l_alg = new DijkstraShortestPath( m_graph );
        final List<CEdge> l_rou = l_alg.getPath( p_start, p_end );
        for ( final CEdge l_edg : l_rou)
        {
            l_edg.add();
        }
        return l_rou;
    }

    public void resetEgdes(){
        Collection<CEdge> l_edges = (Collection<CEdge>) m_graph.getEdges();
        for ( final CEdge l_edg : l_edges ){
            l_edg.reset();
        }
    }
}
