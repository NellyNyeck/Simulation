package objects;

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

    /**
     * test capacity
     */
    @Test
    public void cap()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod.capacity() == 1 );

    }

    /**
     * test id
     */
    @Test
    public void id()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod.id().contentEquals( "pod1" ) );
    }

    /**
     * test provider
     */
    @Test
    public void prov()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod.provider().contentEquals( "DHL" ) );
    }

    /**
     * test strategy
     */
    @Test
    public void strat()
    {
        assumeNotNull( m_pod );
        assertTrue( m_pod.strategy().contentEquals( "cooperative" ) );

    }


}
