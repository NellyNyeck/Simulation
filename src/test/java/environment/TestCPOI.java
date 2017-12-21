package environment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * the test class for poi
 */
public class TestCPOI
{
    private CPOI m_poi;

    /**
     * initialize
     * @throws IOException file
     * @throws ParseException json
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestNode.json" ) );
        final CNode l_node = new CNode( l_obj );
        m_poi = new CPOI( l_node );
    }

    /**
     * test constr
     */
    @Test
    public void constr()
    {
        assertTrue( m_poi != null );
    }

    /**
     * test descr
     */
    @Test
    public void descr()
    {
        assumeNotNull( m_poi );
        final CLabel l_lab = new CLabel( "client", 10.00 );
        final Collection<ILabel> l_col = new ArrayList<>(  );
        l_col.add( l_lab );
        m_poi.description( l_col );
        assertTrue( m_poi.labels().size() != 0 );
    }

    /**
     * test labels
     */
    @Test
    public void labels()
    {
        assumeNotNull( m_poi );
        final CLabel l_lab = new CLabel( "client", 10.00 );
        final Collection<ILabel> l_col = new ArrayList<>(  );
        l_col.add( l_lab );
        m_poi.description( l_col );
        assertTrue( m_poi.labels().size() != 0 );
        assertTrue( m_poi.labels().contains( l_lab ) );
    }

    /**
     * test name
     */
    @Test
    public void name()
    {
        assumeNotNull( m_poi );
        assertTrue( m_poi.name().contentEquals( "node0" ) );
    }

    /**
     * test first coord
     */
    @Test
    public void fcoord()
    {
        assumeNotNull( m_poi );
        assertTrue( m_poi.firstCoord().equals( 0.00 ) );
    }

    /**
     * test second coord
     */
    @Test
    public void scoord()
    {
        assumeNotNull( m_poi );
        assertTrue( m_poi.secondCoord().equals( 0.00 ) );
    }

}
