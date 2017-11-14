package exec;

import environment.CEdge;
import environment.CGraph;
import environment.CNode;
import org.junit.Before;

/**
 * test class
 */
public final class TestCMain
{
    private CGraph<?, CNode, CEdge> gm_graph;

    @Before
    public void initialize() {
    gm_graph =new CGraph();
    }

}
