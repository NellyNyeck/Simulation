package environment;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

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
        m_graph.addNode( m_node1 );
        m_graph.addNode( m_node2 );
        final Collection<CNode> l_list = m_graph.getNodes();
        if ( ( l_list.size() == 2 ) &&  ( m_graph.countNodes() == 2 ) && ( l_list.contains( m_node1 ) ) && ( l_list.contains( m_node2 ) ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the get node function
     */
    @Test
    public void getNode()
    {
        m_graph.addNode( m_node1 );
        final CNode l_node = m_graph.getNode( 14 );
        if ( l_node != null )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the add edge function
     */
    @Test
    public void addEdge()
    {
        m_graph.addEdge( m_node1, m_node2, m_edge );
        final Collection<CEdge> l_list = m_graph.getEdges();
        if ( ( m_graph.countEdges() == 1 ) && ( l_list.size() == 1 ) && ( l_list.contains( m_edge ) ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the dijkstra routing function
     */
    @Test
    public void route()
    {
        m_graph.addEdge( m_node1, m_node2, m_edge );
        final List<CEdge> l_list = m_graph.route( m_node1, m_node2 );
        if ( ( l_list.size() == 1 ) && ( l_list.contains( m_edge ) ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * testing the reset visited edges function
     */
    @Test
    public void reset()
    {
        m_graph.resetEdges();
        final Collection<CEdge> l_list = m_graph.getEdges();
        Boolean l_check = true;
        for ( final CEdge l_edge : l_list )
        {
            if ( l_edge.visited() != 0 )
            {
                l_check = false;
            }
        }
        System.out.println( l_check );
    }

}
