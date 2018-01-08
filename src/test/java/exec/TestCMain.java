package exec;

import environment.CNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;



/**
 * the test class for main
 */
public class TestCMain
{
    private CMain m_main;
    private CNode m_n0;
    private CNode m_n1;
    private CNode m_n6;
    private CNode m_n7;

    /**
     * initializing te thing
     * @throws IOException file
     * @throws ParseException parser
     */
    @Before
    public void init() throws IOException, ParseException
    {
        m_main = new CMain();
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestMain.json" ) );
        final JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        CNode l_node = new CNode( (JSONObject) l_array.get( 0 ) );
        m_n0 = l_node;
        l_node = new CNode( (JSONObject) l_array.get( 1 ) );
        m_n1 = l_node;
        l_node = new CNode( (JSONObject) l_array.get( 2 ) );
        m_n6 = l_node;
        l_node = new CNode( (JSONObject) l_array.get( 3 ) );
        m_n7 = l_node;
    }

    /**
     * testing the create nodes function
     * @throws IOException dealing with files
     * @throws ParseException parser
     */
    @Test
    public void nodes() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestMain.json" ) );
        final JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        CMain.createNodes( l_array );
        assertTrue( m_main.s_idcounter == 4 );
        assertTrue( m_main.s_GR.countNodes() == 4 );
    }

    /**
     * testing the add edge branch 1
     * @throws IOException file
     * @throws ParseException parser
     */
    @Test
    public void edges1() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestGraph.json" ) );
        JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        CMain.createNodes( l_array );
        l_array = (JSONArray) l_obj.get( "edges" );
        m_main.createEdges( l_array );
        assertTrue( m_main.s_GR.countEdges() == 1 );
    }

    /**
     * testing the add edge branch 2
     * @throws IOException file
     * @throws ParseException parser
     */
    @Test
    public void edges2() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestMain.json" ) );
        JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        CMain.createNodes( l_array );
        final JSONObject l_spec = (JSONObject) l_obj.get( "simulation specification" );
        CMain.setSpecs( l_spec );
        l_array = (JSONArray) l_obj.get( "edges" );
        CMain.createEdges( l_array );
        assertTrue( m_main.s_GR.countEdges() > 7 );
        assertTrue( m_main.s_GR.countNodes() > 4 );
        assertTrue( m_main.s_GR.getPois().size() > 50 );

    }

    /**
     * testing the get new coordinates horizontal right
     */
    @Test
    public void getCoordHR()
    {
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n0, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n1, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordHori( l_funct, m_n0.firstCoord(), m_n0.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 10.0 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 0.0 );
    }

    /**
     * testing the get new coordinates horizontal left
     */
    @Test
    public void getCoordHL()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n1, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n1, m_n0, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordHori( l_funct, m_n1.firstCoord(), m_n1.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 90.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 0.00 );
    }

    /**
     * testing the get new coordinates vertical ascending
     */
    @Test
    public void getCoordVA()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n0, m_n6 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n6, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordVert( l_funct, m_n0.firstCoord(), m_n0.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 0.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 10.00 );
    }

    /**
     * testing the get new coordinates vertical descending
     */
    @Test
    public void getCoordVD()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n6, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n6, m_n0, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordVert( l_funct, m_n6.firstCoord(), m_n6.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 0.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 65.0 );
    }

    /**
     * testing the get new coordinates ascending right
     */
    @Test
    public void getCoordOAR()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n0, m_n7 ) );
        l_funct.put( "parameters", CMain.getab( m_n0, m_n7, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n0.firstCoord(), m_n0.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 8.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 6.00 );
    }

    /**
     * testing the get new coordinates descending right
     */
    @Test
    public void getCoordODR()
    {
        assumeNotNull( m_n6 );
        assumeNotNull( m_n1 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n6, m_n1 ) );
        l_funct.put( "parameters", CMain.getab( m_n6, m_n1, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n6.firstCoord(), m_n6.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 8.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 69.00 );
    }

    /**
     * testing the get new coordinates ascending left
     */
    @Test
    public void getCoordOAL()
    {
        assumeNotNull( m_n1 );
        assumeNotNull( m_n6 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n1, m_n6 ) );
        l_funct.put( "parameters", CMain.getab( m_n1, m_n6, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n1.firstCoord(), m_n1.secondCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 92.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 6.00 );
    }

    /**
     * testing the get new coordinates descending left
     */
    @Test
    public void getCoordODL()
    {
        assumeNotNull( m_n0 );
        assumeNotNull( m_n7 );
        final JSONObject l_funct = new JSONObject();
        l_funct.put( "type", CMain.calculateOrientation( m_n7, m_n0 ) );
        l_funct.put( "parameters", CMain.getab( m_n7, m_n0, (String) l_funct.get( "type" ) ) );
        final Double l_dist = Double.valueOf( 10 );
        final JSONObject l_res = CMain.getCoordLinear( l_funct, m_n7.firstCoord(), m_n7.firstCoord(), l_dist );
        assertTrue( (double) l_res.get( "first coordinate" ) == 92.00 );
        assertTrue( (double) l_res.get( "second coordinate" ) == 69.0 );
    }

    /**
     * testing specs
     * @throws IOException file
     * @throws ParseException parser
     */
    @Test
    public void specs() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestMain.json" ) );
        final JSONObject l_spec = (JSONObject) l_obj.get( "simulation specification" );
        m_main.setSpecs( l_spec );
        assertTrue( m_main.s_specs != null );
        assertTrue( m_main.s_specs.coordType().contentEquals( "synthetic" ) );
    }
}
