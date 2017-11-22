package environment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the test class for edge
 */
public class TestCEdge
{
    private CEdge m_cedge;

    /**
     * initialization of the edge
     * @throws JSONException working with jsonobject
     */
    @Before
    public void init() throws JSONException
    {
        final JSONObject l_put = new JSONObject();
        l_put.put( "from", 8 );
        l_put.put( "to", 14 );
        l_put.put( "weight", 8 );
        l_put.put( "length", 14 );
        m_cedge = new CEdge( l_put );
    }

    /**
     * testing the costructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_cedge != null );
    }

    /**
     * testing contructor with empty object
     */
    @Test
    public void jackedcontr()
    {
        assumeNotNull( m_cedge );
        final JSONObject l_put = new JSONObject();
        m_cedge = new CEdge( l_put );
    }

    /**
     * testing the return weight function
     */
    @Test
    public void weight()
    {
        assumeNotNull( m_cedge );
        assertTrue( m_cedge.weight() == 8 );
    }

    /**
     * test the return visited
     */
    @Test
    public void visited()
    {
        assertTrue( m_cedge.visited() == 0 );
    }

    /**
     * testing the add function
     */
    @Test
    public void add()
    {
        m_cedge.add();
        m_cedge.add();
        assertTrue( m_cedge.visited() == 2 );
    }

    /**
     * testing about function
     */
    @Test
    public void about()
    {
        assertTrue( m_cedge.about().contentEquals( "8 14" ) );
    }


    /**
     * testing the return length
     */
    @Test
    public void length()
    {
        assertTrue( m_cedge.length() == 14 );
    }

    /**
     * testing the reset
     */
    @Test
    public void reset()
    {
        m_cedge.add();
        m_cedge.reset();
        assertTrue( m_cedge.visited() == 0 );
        assertTrue( m_cedge.weight() == 1 );
    }

}
