package environment;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

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
        final HashMap l_map = new HashMap<>(  );
        l_map.put( "name", "node0" );
        final HashMap<String, Double> l_coord = new HashMap<>(  );
        l_coord.put( "first coordinates", 0.00 );
        l_coord.put( "second coordinates", 0.00 );
        l_map.put( "coordinates", l_coord );
        final JSONObject l_obj = new JSONObject( l_map );
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
        assertTrue( m_testnode.id().contentEquals( "node0" ) );
    }

    /**
     * tests the return function for the first coordinate
     */
    @Test
    public void firstCoord()
    {
        assumeNotNull( m_testnode );
        assertTrue( m_testnode.firstCoord().equals( 0 ) );
    }

    /**
     * tests the return function for the first coordinate
     */
    @Test
    public void secondCoord()
    {
        assumeNotNull( m_testnode );
        assertTrue( m_testnode.secondCoord().equals( 0 ) );
    }


}
