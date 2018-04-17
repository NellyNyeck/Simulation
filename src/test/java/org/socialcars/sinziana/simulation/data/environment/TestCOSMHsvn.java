package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Before;
import org.socialcars.sinziana.simulation.environment.demand.CInstance;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * test class for hannover suedstadt with real traffic demand
 */
public class TestCOSMHsvn
{
    private ArrayList<CInstance> m_demand;
    private COSMEnvironment m_env;

    /**
     * initialising the class
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/niedersachsen-latest.osm.pbf", "src/test/Hannover", 52.373400, 52.346500,  9.77800, 9.746206 );
        readFile( "src/test/resources/input/filestuff json" );
    }

    private void readFile( final String p_location )
    {

    }

}
