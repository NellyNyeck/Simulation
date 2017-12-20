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
 * the test class for CNode
 */
public class TestCNode
{

    private CNode m_testnode;

    /**
     * initialize the node
     * @throws IOException because file
     * @throws ParseException because json simple
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader(  "src/test/resources/Examples/TestNode.json" ) );
        m_testnode = new CNode( l_obj );
    }

    /**
     * testing the constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_testnode != null );
    }

    /**
     * testing the retun id function
     */
    @Test
    public void id()
    {
        assumeNotNull( m_testnode );
        assertTrue( m_testnode.name().contentEquals( "node0" ) );
    }

    /**
     * tests the return function for the first coordinate
     */
    @Test
    public void firstCoord()
    {
        assumeNotNull( m_testnode );
        assertTrue( m_testnode.firstCoord() ==  0 );
    }

    /**
     * tests the return function for the first coordinate
     */
    @Test
    public void secondCoord()
    {
        assumeNotNull( m_testnode );
        assertTrue( m_testnode.secondCoord() == 0 );
    }
}
