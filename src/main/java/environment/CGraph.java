package environment;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

import java.util.List;

public final class CGraph<I, V extends INode<I>, E extends IEdge> implements IGraph<I,V,E>
{
    private final Graph<V,E> m_graph = new DirectedSparseGraph<>();

    public CGraph( ) {

    }

    @Override
    public List<E> route(I p_start, I p_end) {
        return null;
    }
}
