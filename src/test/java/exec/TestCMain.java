package exec;

import environment.CNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;


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

}
