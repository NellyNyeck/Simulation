package environment;

import java.util.Collection;

public interface IGraph<I, V extends INode<I>, E extends IEdge>
{
    //List<E> route(@NotNull final I p_start, @NotNull final I p_end );

    public void addNode(V v);

    public V getNode(Number n);

    public Collection<V> getNodes();

    public void addEdge(V v1, V v2, E e);

    public E getEdge(E e);

    public Collection<E> getEdges();

    public int countEdges();

    public int countNodes();


}
