package exec;

import environment.CNode;
import environment.CPOI;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

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

    /**
     * testing the get new coordinates horizontal right
     * @throws JSONException working with json
     */
    @Test
    public void getCoordHR() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n1, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n1 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordHori( l_funct, m_n0.xcoord(), m_n0.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "10.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "0.0" ) );
    }

    /**
     * testing the get new coordinates horizontal left
     * @throws JSONException working with json
     */
    @Test
    public void getCoordHL() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n1, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n1, m_n0, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n1, m_n0 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordHori( l_funct, m_n1.xcoord(), m_n1.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "90.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "0.0" ) );
    }

    /**
     * testing the get new coordinates vertical ascending
     * @throws JSONException working with json
     */
    @Test
    public void getCoordVA() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n6 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n6, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n6 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordVert( l_funct, m_n0.xcoord(), m_n0.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "0.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "10.0" ) );
    }

    /**
     * testing the get new coordinates vertical descending
     * @throws JSONException working with json
     */
    @Test
    public void getCoordVD() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n6, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n6, m_n0, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n6, m_n0 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordVert( l_funct, m_n6.xcoord(), m_n6.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "0.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "65.0" ) );
    }

    /**
     * testing the get new coordinates ascending right
     * @throws JSONException working with json
     */
    @Test
    public void getCoordAR() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n7 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n7, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n7 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n0.xcoord(), m_n0.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "8.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "6.0" ) );
    }

    /**
     * testing the get new coordinates descending right
     * @throws JSONException working with json
     */
    @Test
    public void getCoordDR() throws JSONException
    {
        assumeNotNull( m_n6 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n6, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n6, m_n1, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n6, m_n1 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n6.xcoord(), m_n6.ycoord(), l_dist );
        assertTrue( l_res.getDouble( "x" ) == 8 );
        assertTrue( l_res.getDouble( "y" ) == 69 );
    }

    /**
     * testing the get new coordinates ascending left
     * @throws JSONException working with json
     */
    @Test
    public void getCoordAL() throws JSONException
    {
        assumeNotNull( m_n1 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n1, m_n6 ) );
        l_funct.put( "parameters", CMain.getab( m_n1, m_n6, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n1, m_n6 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n1.xcoord(), m_n1.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "92.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "6.0" ) );
    }

    /**
     * testing the get new coordinates descending left
     * @throws JSONException working with json
     */
    @Test
    public void getCoordDL() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n7, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n7, m_n0, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n7, m_n0 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n7.xcoord(), m_n7.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "92.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "69.0" ) );
    }

    /**
     * testing the general get coordinates for vertical
     * @throws JSONException json
     */
    @Test
    public void getCoorV() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n6, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n6, m_n0, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n6, m_n0 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordinates( l_funct, m_n6.xcoord(), m_n6.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "0.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "65.0" ) );
    }

    /**
     * testing the general get coordinates for horizontal
     * @throws JSONException json
     */
    @Test
    public void getCoorH() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n1, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n1 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordinates( l_funct, m_n0.xcoord(), m_n0.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "10.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "0.0" ) );
    }

    /**
     * testing the get coordinates normal
     * @throws JSONException working with json
     */
    @Test
    public void getCoordN() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n7, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n7, m_n0, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n7, m_n0 ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordinates( l_funct, m_n7.xcoord(), m_n7.ycoord(), l_dist );
        assertTrue( l_res.getString( "x" ).contentEquals( "92.0" ) );
        assertTrue( l_res.getString( "y" ).contentEquals( "69.0" ) );
    }

    /**
     * testing with pad 0
     * @throws JSONException working with json
     */
    @Test
    public void pad0() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n1, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n1 ) );
        final Double l_dist = Double.valueOf( 10 );
        CMain.toPad( m_n0, m_n1, 0.0, 9, l_funct, 0.1 );
    }

    /**
     * testing with pad not 0
     * @throws JSONException working with json
     */
    @Test
    public void padn0() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n6 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n6, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n6 ) );
        final Double l_dist = Double.valueOf( 10 );
        CMain.toPad( m_n0, m_n6, 0.0, 7, l_funct, 0.1 );
    }

    /**
     * testing json object creation
     * @throws JSONException json
     */
    @Test
    public void createPOI() throws JSONException
    {
        final Integer l_id = 14;
        final Double l_xc = 8.0;
        final Double l_yc = 14.0;
        final JSONObject l_obj = CMain.createPOI( l_id, l_xc, l_yc );
        assertTrue( l_obj.getInt( "id" ) == 14 );
        assertTrue( l_obj.getString( "x" ).contentEquals( "8.0" ) );
        assertTrue( l_obj.getString( "y" ).contentEquals( "14.0" ) );
    }

    /**
     * testing bind
     * @throws JSONException json
     */
    @Test
    public void bind() throws JSONException
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        CMain.bind( m_n0, m_n1, 100, 1.0 );
    }


    /**
     * testing graph Init
     * @throws JSONException working with json
     * @throws IOException working with file
     */
    @Test
    public void processStreet() throws IOException, JSONException
    {
        CMain.graphInit( "src/test/resources/Scenario2.json" );
    }

    /**
     * testing the poi generation
     * @throws JSONException json
     */
    @Test
    public void randomPoi() throws JSONException
    {

        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.functType( m_n0, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n1, l_funct.getString( "type" ) ) );
        l_funct.put( "direction", CMain.getDirection( m_n0, m_n1 ) );
        final Double l_dist = Double.valueOf( 10 );
        CMain.toPad( m_n0, m_n1, 0.0, 9, l_funct, 0.1 );
        final Collection<CPOI> l_cpois = CMain.randomPOI();
        assertTrue( l_cpois.size() == 6 );
    }

}
