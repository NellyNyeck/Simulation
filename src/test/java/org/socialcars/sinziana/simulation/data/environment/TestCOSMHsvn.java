package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Before;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;
import java.util.ArrayList;

/**
 * test class for hannover suedstadt with real traffic demand
 */
public class TestCOSMHsvn
{
    private ArrayList<CDemand> m_demands;
    private COSMEnvironment m_env;

    /**
     * initialising the class
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMEnvironment( "src/test/resources/niedersachsen-latest.osm.pbf", "src/test/Hannover", 52.373400, 52.346500,  9.77800, 9.746206 );

        m_demands = readFile( "src/test/resources/input/HSVN.csv" );
    }

    /**
     * reading the traffic demand file
     * @param p_location parth to file
     * @return list of demands
     */
    private ArrayList<CDemand> readFile( final String p_location )
    {
        final ArrayList<CDemand> l_demands = new ArrayList<>();



        return l_demands;
    }

    private final class CDemand
    {
        private final String m_origin;
        private final String m_destination;
        private final int m_howmany;

        private CDemand( final String p_or, final String p_de, final int p_nb )
        {
            m_origin = p_or;
            m_destination = p_de;
            m_howmany = p_nb;
        }
    }
}
