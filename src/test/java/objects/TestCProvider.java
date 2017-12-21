package objects;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;

import java.io.FileReader;
import java.io.IOException;


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
}
