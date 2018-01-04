package exec;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;



/**
 * the test class for main
 */
public class TestCMain
{
    private CMain m_main;

    @Before
    public void init()
    {
        m_main = new CMain();
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
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestGraph.json" ) );
        final JSONArray l_array = (JSONArray) l_obj.get( "nodes" );
        CMain.createNodes( l_array );
        assertTrue( m_main.s_idcounter == 2 );
        assertTrue( m_main.s_GR.countNodes() == 2 );
    }
}
