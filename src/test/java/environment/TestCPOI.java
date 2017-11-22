package environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the test class for poi
 */
public class TestCPOI
{
    private CPOI m_cpoi;
    private CNode m_node;

    /**
     * initialises
     * @throws JSONException because working with json object
     */
    @Before
    public void init() throws JSONException
    {
        final JSONObject l_put = new JSONObject();
        l_put.put( "id", "S" );
        l_put.put( "x", 8 );
        l_put.put( "y", 14 );
        m_node = new CNode( l_put );
        m_cpoi = new CPOI( m_node );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_cpoi != null );
    }

    /**
     * testing the exception
     */
    @Test
    public void jackedConstr()
    {
        assumeNotNull( m_cpoi );
        final JSONObject l_put = new JSONObject();
        final CNode l_node = new CNode( l_put );
        m_cpoi = new CPOI( l_node );
    }

    /**
     * testing the id
     */
    @Test
    public void id()
    {
        assumeNotNull( m_cpoi );
        assertTrue( m_cpoi.id().equals( m_node ) );
    }

    /**
     * testing the x coordinates
     */
    @Test
    public void xcoord()
    {
        assumeNotNull( m_cpoi );
        assertTrue( m_cpoi.xcoord() == m_node.xcoord() );
    }

    /**
     * testing the y coordinates
     */
    @Test
    public void ycoord()
    {
        assumeNotNull( m_cpoi );
        assertTrue( m_cpoi.ycoord() == m_node.ycoord() );
    }

    /**
     * testing the labels
     */
    @Test
    public void label()
    {
        assumeNotNull( m_cpoi );
        assertTrue( m_cpoi.labels().isEmpty() );
    }
}
