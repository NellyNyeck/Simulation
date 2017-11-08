package environment;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.Collection;
import java.util.List;

public final class CGraph<I, V extends INode<I>, E extends IEdge> implements IGraph<I,V,E> {
    private final Graph<V,E> m_graph = new DirectedSparseGraph<>();

    public CGraph( ) {
    }


    public void addNode(V v){
        m_graph.addVertex(v);
    }

    public V getNode(Number n){
        Collection<V> allN = m_graph.getVertices();
        for(V v : allN){
            if (v.id()==n) return v;
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

    @Override
    public int countEdges() {
        return m_graph.getEdgeCount();
    }

    @Override
    public int countNodes() {
        int v=m_graph.getVertexCount();
        return m_graph.getVertexCount();
    }

    @Override
    public List<E> route(I p_start, I p_end) {
        DijkstraShortestPath alg = new DijkstraShortestPath(m_graph);
        List<E> r= alg.getPath(p_start, p_end);
        return r;
    }

    public void update(List<E> l){
        for(E e : l){
            CEdge ex= (CEdge) getEdge(e);
            ex.update();
        }
    }
}
