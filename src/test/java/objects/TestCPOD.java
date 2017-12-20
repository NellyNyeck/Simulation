package objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;


/**
 * the test class for pods
 */
public class TestCPOD
{
    private CPOD m_pod;

    /**
     * initializing
     * @throws IOException file
     * @throws ParseException json
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestPod.json" ) );
        m_pod = new CPOD( l_obj );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_pod != null );
    }



}
