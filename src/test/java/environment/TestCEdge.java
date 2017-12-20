package environment;

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
 * test class for edge
 */
public class TestCEdge
{
    private CEdge m_edge;

    /**
     * the initialization
     * @throws IOException because file
     * @throws ParseException because json file
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestEdge.json" ) );
        m_edge = new CEdge( l_obj );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_edge != null );
    }

    /**
     * testing the weight
     */
    @Test
    public void weight()
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.weight().equals( 1.00 ) );
    }

}
