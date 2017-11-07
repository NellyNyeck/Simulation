package environment;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;

public final class CGraph<I, V extends INode<I>, E extends IEdge> implements IGraph<I,V,E>
{
    private final Graph<V,E> m_graph = new DirectedSparseGraph<>();

    public CGraph( ) {
    }


    public void addNode(V v){
        m_graph.addVertex(v);
    }

    public V getNode(V comp){
        Collection<V> allN = m_graph.getVertices();
        for(V v : allN){
            if (v.equals(comp)) return v;
        }
        return null;
    }

    @Override
    public Collection<V> getNodes() {
        return m_graph.getVertices();
    }

    @Override
    public void addEdge(V v1, V v2, E e) {
        m_graph.addEdge(e, v1, v2);

    }

    @Override
    public E getEdge(E comp) {
        Collection<E> allE = m_graph.getEdges();
        for(E e : allE){
            if (e.equals(comp)) return e;
        }
        return null;
    }

    @Override
    public Collection<E> getEdges() {
        return m_graph.getEdges();
    }



    /*@Override
    public List<E> route(I p_start, I p_end) {
        return null;
    }*/
}
