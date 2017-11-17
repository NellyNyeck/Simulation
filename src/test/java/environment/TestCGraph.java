package environment;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the test class for CGraph
 */
public class TestCGraph
{
    private CGraph<?, CNode, CEdge> m_graph;
    private CNode m_node1;
    private CNode m_node2;
    private CEdge m_edge;



    /**
     * Initialization of graph
     */
    @Before
    public void initialize()
    {
        m_graph = new CGraph<>();
        m_edge = new CEdge( "14 8" );
        m_node1 = new CNode( 14 );
        m_node2 = new CNode( 8 );
    }

    /**
     * testing the add node function
     */
    @Test
    public void addNode()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_node1 );
        assumeNotNull( m_node2 );
        m_graph.addNode( m_node1 );
        m_graph.addNode( m_node2 );
        final Collection<CNode> l_list = m_graph.getNodes();
        assertTrue( l_list.size() == 2 );
        assertTrue( m_graph.countNodes() == 2 );
        assertTrue( l_list.contains( m_node1 ) );
        assertTrue( l_list.contains( m_node2 ) );
    }

    /**
     * testing the get node function
     */
    @Test
    public void getNode()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_node1 );
        m_graph.addNode( m_node1 );
        final CNode l_node = m_graph.getNode( 14 );
        assertTrue( l_node != null );
    }

    /**
     * testing the add edge function
     */
    @Test
    public void addEdge()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_node1 );
        assumeNotNull( m_node2 );
        m_graph.addEdge( m_node1, m_node2, m_edge );
        final Collection<CEdge> l_list = m_graph.getEdges();
        assertTrue( m_graph.countEdges() == 1 );
        assertTrue( l_list.size() == 1 );
        assertTrue( l_list.contains( m_edge ) );
    }

    /**
     * testing the dijkstra routing function
     */
    @Test
    public void route()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_node1 );
        assumeNotNull( m_node2 );
        m_graph.addEdge( m_node1, m_node2, m_edge );
        final List<CEdge> l_list = m_graph.route( m_node1, m_node2 );
        assertTrue( l_list.size() == 1 );
        assertTrue( l_list.contains( m_edge ) );
    }

    /**
     * testing the reset visited edges function
     */
    @Test
    public void reset()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_node1 );
        assumeNotNull( m_node2 );
        m_graph.addEdge( m_node1, m_node2, m_edge );
        m_edge.add();
        m_graph.resetEdges();
        final Collection<CEdge> l_list = m_graph.getEdges();
        for ( final CEdge l_edge : l_list )
        {
            assertTrue( l_edge.visited() ==  0 );
        }
    }

}
