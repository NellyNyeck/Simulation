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
    //List<E> route(@NotNull final I p_start, @NotNull final I p_end );

    public void addNode( V v );

    public V getNode( Number n );

    public Collection<V> getNodes();

    public void addEdge( V p_v1, V p_v2, E e );

    public Collection<E> getEdges();

    public int countEdges();

    public int countNodes();

    public List<CEdge> route( final I p_start, final I p_end );
}
