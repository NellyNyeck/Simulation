package environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the node test class
 */
public class TestCNode
{
    private CNode m_node;

    /**
     * intialization
     * @throws JSONException working with jsonobject
     */
    @Before
    public void init() throws JSONException
    {
        final JSONObject l_put = new JSONObject();
        l_put.put( "id", "S" );
        l_put.put( "x", 8 );
        l_put.put( "y", 14 );
        m_node = new CNode( l_put );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_node != null );
    }

    /**
     * testing the exception
     */
    @Test
    public void jackedConstr()
    {
        assumeNotNull( m_node );
        final JSONObject l_put = new JSONObject();
        m_node = new CNode( l_put );
    }

    /**
     * testing the id
     */
    @Test
    public void id()
    {
        assertTrue( m_node.id().contentEquals( "S" ) );
    }

    /**
     * tsting the x coordinates
     */
    @Test
    public void xcoord()
    {
        assertTrue( m_node.xcoord() == 8 );
    }

    /**
     * testing the y coordinates
     */
    @Test
    public void ycoord()
    {
        assertTrue( m_node.ycoord() == 14 );
    }

}
