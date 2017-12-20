package environment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

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
     * initialize
     * @throws IOException file
     * @throws ParseException json
     */
    @Before
    public void init() throws IOException, ParseException
    {
        m_graph = new CGraph<>();
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestGraph.json" ) );
        JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        m_n1 = new CNode( (JSONObject) l_array.get( 0 ) );
        m_n2 = new CNode( (JSONObject) l_array.get( 1 ) );
        l_array = (JSONArray) l_obj.get( "edges" );
        m_ed = new CEdge( (JSONObject) l_array.get( 0 ) );
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
        final CNode l_test = m_graph.getNode( m_n2.name() );
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
        final CNode l_test = m_graph.getNode( m_n2.name() );
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
}
