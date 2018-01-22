package environment;

import java.util.Collection;
import java.util.List;

/**
 * The Interface for any implementation of a graph
 * @param <I> dunno what this is
 * @param <V> the generic for Node/Vertex
 * @param <E> the generic for Edge
 */

public interface IGraph<I, V extends INode<I>, E extends IEdge>
{
    void addNode( V v );

    V getNode( String n );

    Collection<V> getNodes();

    void addEdge( V p_v1, V p_v2, E e );

    Collection<E> getEdges();

    int countEdges();

    int countNodes();

    List<E> route( final V p_start, final V p_end );

    void resetEdges();
}
