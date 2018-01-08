package objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeNotNull;


/**
 * test class for provider
 */
public class TestCProvider
{
    private CProvider m_prov;

    /**
     * initializing
     * @throws IOException file
     * @throws ParseException json
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestProvider.json" ) );
        m_prov = new CProvider( l_obj );
    }

    /**
     * test constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_prov != null );
    }

    /**
     * test name
     */
    @Test
    public void name()
    {
        assumeNotNull( m_prov );
        assertTrue( m_prov.name().contentEquals( "DHL" ) );
    }

    /**
     * test colour
     */
    @Test
    public void colour()
    {
        assumeNotNull( m_prov );
        assertTrue( m_prov.colour().contentEquals( "yellow" ) );
    }

    /**
     * test depot
     */
    @Test
    public void depot()
    {
        assumeNotNull( m_prov );
        assertTrue( m_prov.depot().contentEquals( "node0" ) );
    }

    /**
     * testing pods
     */
    @Test
    public void pods()
    {
        assumeNotNull( m_prov );
        final HashMap<String, CPOD> l_pods = m_prov.pods();
        assertTrue( l_pods.size() == 1 );
        assertTrue( l_pods.get( "pod1" ) != null );
        final CPOD l_pod = l_pods.get( "pod1" );
        assertTrue( l_pod.id().contentEquals( "pod1" ) );
    }

    /**
     * test maxutgoing
     */
    @Test
    public void maxout()
    {
        assumeNotNull( m_prov );
        assertTrue( m_prov.maxOutgoing().equals( 1.00 ) );
    }

    /**
     * test funct
     */
    @Test
    public void funct()
    {
        assumeNotNull( m_prov );
        assertTrue( m_prov.funct().contentEquals( "normal" ) );
    }

    @Test
    public void params()
    {
        assumeNotNull( m_prov );
        final HashMap<String, Double> l_par = m_prov.params();
        assertTrue( l_par.get( "sigma" ).equals( 1.00 ) );
        assertTrue( l_par.get( "mu" ).equals( 1.00 ) );
    }
}
