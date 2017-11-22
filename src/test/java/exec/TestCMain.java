package exec;

import environment.CNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * testing the main class
 */
public class TestCMain
{
    private CMain m_main;
    private CNode m_n0;
    private CNode m_n1;
    private CNode m_n6;
    private CNode m_n7;


    /**
     * initializing;
     * @throws JSONException working with json
     */
    @Before
    public void init() throws JSONException
    {
        m_main = new CMain();
        JSONObject l_put = new JSONObject();
        l_put.put( "id", 0 );
        l_put.put( "x", 0 );
        l_put.put( "y", 0 );
        m_n0 = new CNode( l_put );
        l_put = new JSONObject();
        l_put.put( "id", 1 );
        l_put.put( "x", 100 );
        l_put.put( "y", 0 );
        m_n1 = new CNode( l_put );
        l_put = new JSONObject();
        l_put.put( "id", 6 );
        l_put.put( "x", 0 );
        l_put.put( "y", 75 );
        m_n6 = new CNode( l_put );
        l_put = new JSONObject();
        l_put.put( "id", 7 );
        l_put.put( "x", 100 );
        l_put.put( "y", 75 );
        m_n7 = new CNode( l_put );
    }

    /**
     * testing bing
     * @throws JSONException working with json object
     */
    @Test
    public void bind() throws JSONException
    {
        assumeNotNull( m_n1 );
        assumeNotNull( m_n0 );
        m_main.bind( m_n0, m_n1, 1, 100 );
        assertTrue( m_main.s_GR.countEdges() == 1 );
    }

    /**
     * testing vertical function
     * @throws JSONException working with json object
     */
    @Test
    public void vert() throws JSONException
    {
        final JSONObject l_object = new JSONObject();
        l_object.put( "from", 0 );
        l_object.put( "to", 6 );
        l_object.put( "length", 75 );
        l_object.put( "weighth", 1 );

        m_main.vertical( l_object, "max" );

    }
}
