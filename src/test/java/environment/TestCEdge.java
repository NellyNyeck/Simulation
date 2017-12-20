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

    /**
     * testing visited
     */
    @Test
    public void visited()
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.visited() == 0 );
    }

    /**
     * testing name
     */
    @Test
    public void name()
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.name().contentEquals( "edge0-1" ) );
    }

    /**
     * testing from
     */
    @Test
    public void from()
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.from().contentEquals( "node0" ) );
    }

    /**
     * testing to
     */
    @Test
    public void to()
    {
        assumeNotNull( m_edge );
        assertTrue( m_edge.to().contentEquals( "node1" ) );
    }

    /**
     * testing  add
     */
    @Test
    public void add()
    {
        assumeNotNull( m_edge );
        m_edge.add();
        assertTrue( m_edge.visited() == 1 );
    }

    /**
     * testing reset
     */
    @Test
    public void reset()
    {
        assumeNotNull( m_edge );
        m_edge.add();
        m_edge.reset();
        assertTrue( m_edge.visited() == 0 );
    }

}
