package environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * testng the graph class
 */
public class TestCGraph
{
    private CGraph<?, CNode, CEdge> m_graph;
    private CNode m_n1;
    private CNode m_n2;
    private CEdge m_ed;

    /**
     * initialising
     * @throws JSONException working with json object
     */
    @Before
    public void init() throws JSONException
    {
        m_graph = new CGraph<>();
        JSONObject l_put = new JSONObject(  );
        l_put.put( "id", 8 );
        l_put.put( "x", 200 );
        l_put.put( "y", 100 );
        m_n1 = new CNode( l_put );
        l_put = new JSONObject(  );
        l_put.put( "id", 14 );
        l_put.put( "x", 200 );
        l_put.put( "y", 200 );
        m_n2 = new CNode( l_put );
        l_put = new JSONObject();
        l_put.put( "from", 8 );
        l_put.put( "to", 14 );
        l_put.put( "weight", 1 );
        l_put.put( "length", 100 );
        m_ed = new CEdge( l_put );
    }

    /**
     * testing add node
     */
    @Test
    public void addNode()
    {
        assumeNotNull( m_graph );
        m_graph.addNode( m_n1 );
        assertTrue( m_graph.countNodes() == 1 );
    }

    /**
     * testing get node
     */
    @Test
    public void getNode()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        final CNode l_test = m_graph.getNode( m_n2.id() );
        assertTrue( l_test != null );
        assertTrue( l_test.equals( m_n2 ) );
    }

    /**
     * testing get node with non existand one
     */
    @Test
    public void getnoNode()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        final CNode l_test = m_graph.getNode( m_n2.id() );
        assertTrue( l_test == null );
    }

    /**
     * testing get nodes
     */
    @Test
    public void getNodes()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        final Collection<CNode> l_list = m_graph.getNodes();
        assertTrue( l_list.size() == 2 );
        assertTrue( l_list.contains( m_n1 ) );
        assertTrue( l_list.contains( m_n2 ) );
    }

    /**
     * testing add edge
     */
    @Test
    public void addEdge()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        assumeNotNull( m_ed );
        m_graph.addEdge( m_n1, m_n2, m_ed );
        assertTrue( m_graph.countEdges() == 1 );
    }

    /**
     * testing get edges
     */
    @Test
    public void getEdges()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        assumeNotNull( m_ed );
        m_graph.addEdge( m_n1, m_n2, m_ed );
        assertTrue( m_graph.getEdges().size() == 1 );
        assertTrue( m_graph.getEdges().contains( m_ed ) );
    }

    /**
     * testing routing
     */
    @Test
    public void route()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        assumeNotNull( m_ed );
        m_graph.addEdge( m_n1, m_n2, m_ed );
        assertTrue( m_graph.route( m_n1, m_n2 ).size() == 1 );
    }

    /**
     * testing the edges reset
     */
    @Test
    public void reset()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        assumeNotNull( m_ed );
        m_ed.add();
        m_ed.add();
        m_graph.addEdge( m_n1, m_n2, m_ed );
        m_graph.resetEdges();
        final Collection<CEdge> l_edges = m_graph.getEdges();
        assertTrue( l_edges.contains( m_ed ) );
        assertTrue( m_ed.visited() == 0 );
    }

    /**
     * testing the add poi
     */
    @Test
    public void addpoi()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        final CPOI l_cpoi = new CPOI( m_n1 );
        m_graph.addPoi( l_cpoi );
        assertTrue( m_graph.getPois().size() == 1 );
    }

    /**
     * testing the get poi
     */
    @Test
    public void getpoi()
    {
        assumeNotNull( m_graph );
        assumeNotNull( m_n1 );
        assumeNotNull( m_n2 );
        m_graph.addNode( m_n1 );
        m_graph.addNode( m_n2 );
        final CPOI l_cpoi = new CPOI( m_n1 );
        m_graph.addPoi( l_cpoi );
        final HashMap<Integer, CPOI> l_collection = m_graph.getPois();
        assertTrue( l_collection.containsValue( l_cpoi ) );
    }
}
