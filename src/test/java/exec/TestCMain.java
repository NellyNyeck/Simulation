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
     * testing horizontal function
     */
    @Test
    public void functTypeH()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        assertTrue( CMain.functType( m_n0, m_n1 ).contentEquals( "Horizontal" ) );
    }

    /**
     * testing vertical function
     */
    @Test
    public void functTypeV()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        assertTrue( CMain.functType( m_n0, m_n6 ).contentEquals( "Vertical" ) );
    }

    /**
     * testing normal function
     */
    @Test
    public void functTypeN()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        assertTrue( CMain.functType( m_n0, m_n7 ).contentEquals( "Normal" ) );
    }

    /**
     * testing the get function parameters for horizontal
     * @throws JSONException working with json
     */
    @Test
    public void getabH() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final String l_type = CMain.functType( m_n0, m_n1 );
        final JSONObject l_res = CMain.getab( m_n0, m_n1, l_type );
        assertTrue( l_res.getString( "a" ).contentEquals( "0" ) );
        assertTrue( l_res.getString( "b" ).contentEquals( String.valueOf( m_n0.ycoord() ) ) );
    }

    /**
     * testing the get function parameters for vertical
     * @throws JSONException working with json
     */
    @Test
    public void getabV() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final String l_type = CMain.functType( m_n0, m_n6 );
        final JSONObject l_res = CMain.getab( m_n0, m_n6, l_type );
        assertTrue( l_res.getString( "a" ).contentEquals( "?" ) );
        assertTrue( l_res.getString( "b" ).contentEquals( "?" ) );
    }

    /**
     * testing the get function parameters for normal
     * @throws JSONException working with json
     */
    @Test
    public void getabN() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final String l_type = CMain.functType( m_n0, m_n7 );
        final JSONObject l_res = CMain.getab( m_n0, m_n7, l_type );
        assertTrue( l_res.getString( "a" ).contentEquals( "0.75" ) );
        assertTrue( l_res.getString( "b" ).contentEquals( "0.0" ) );
    }

    /**
     * testing the direction right
     */
    @Test
    public void getDirR()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        assertTrue( CMain.getDirection( m_n0, m_n1 ).contentEquals( "R" ) );
    }

    /**
     * testing the direction left
     */
    @Test
    public void getDirL()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        assertTrue( CMain.getDirection( m_n1, m_n0 ).contentEquals( "L" ) );
    }

    /**
     * testing the direction ascending
     */
    @Test
    public void getDirA()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        assertTrue( CMain.getDirection( m_n0, m_n6 ).contentEquals( "A" ) );
    }

    /**
     * testing the direction descending
     */
    @Test
    public void getDirD()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        assertTrue( CMain.getDirection( m_n6, m_n0 ).contentEquals( "D" ) );
    }

    /**
     * testing the direction ascending right
     */
    @Test
    public void getDirAR()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        assertTrue( CMain.getDirection( m_n0, m_n7 ).contentEquals( "AR" ) );
    }

    /**
     * testing the direction descending left
     **/
    @Test
    public void getDirDL()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        assertTrue( CMain.getDirection( m_n7, m_n0 ).contentEquals( "DL" ) );
    }

    /**
     * testing the direction ascending left
     */
    @Test
    public void getDirAL()
    {
        assumeNotNull( m_n1 );
        assumeNotNull( m_n6 );
        assertTrue( CMain.getDirection( m_n1, m_n6 ).contentEquals( "AL" ) );
    }

    /**
     * testing the direction descending right
     */
    @Test
    public void getDirDR()
    {
        assumeNotNull( m_n1 );
        assumeNotNull( m_n6 );
        assertTrue( CMain.getDirection( m_n6, m_n1 ).contentEquals( "DR" ) );
    }

    /**
     * testing the direction nonexistent
     */
    @Test
    public void getDirNon()
    {
        assumeNotNull( m_n0 );
        assertTrue( CMain.getDirection( m_n0, m_n0 ) == null );
    }
}
