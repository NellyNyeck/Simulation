package exec;

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
 * test class for sim specs
 */
public class TestCSpecs
{
    private CSpecs m_specs;

    /**
     * initializing
     * @throws IOException file
     * @throws ParseException json
     */
    @Before
    public void init() throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final JSONObject l_obj = (JSONObject) l_parser.parse( new FileReader( "src/test/resources/Examples/TestSpecs.json" ) );
        m_specs = new CSpecs( l_obj );
    }

    /**
     * testing constructor
     */
    @Test
    public void constr()
    {
        assertTrue( m_specs != null );
    }

    /**
     * testing coordinates
     */
    @Test
    public void coord()
    {
        assumeNotNull( m_specs );
        assertTrue( m_specs.coordType().contentEquals( "synthetic" ) );
    }

    /**
     * testing client nbb
     */
    @Test
    public void clientNb()
    {
        assumeNotNull( m_specs );
        assertTrue( m_specs.clientNb() == 3 );
    }

    /**
     * testing time unit
     */
    @Test
    public void timeUnit()
    {
        assumeNotNull( m_specs );
        assertTrue( m_specs.timeUnit().contentEquals( "second" ) );
    }

    /**
     * testing speed unit
     */
    @Test
    public void speedUnit()
    {
        assumeNotNull( m_specs );
        assertTrue( m_specs.speedUnit().contentEquals( "m/s" ) );
    }

    /**
     * testing length unit
     */
    @Test
    public void lengthUnit()
    {
        assumeNotNull( m_specs );
        assertTrue( m_specs.lengthUnit().contentEquals( "meter" ) );
    }


}
