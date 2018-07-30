package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.environment.blocks.COSMBlock;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;

/**
 * test class for osm block environment
 */
public class TestCOSMBlockSmall
{
    private COSMEnvironment m_osm;
    private COSMBlock m_env;

    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_osm = new COSMEnvironment( "src/test/resources/spain-latest.osm.pbf", "src/test/Barcelona", 41.412895, 41.409428,  2.184461, 2.174316 );
        m_env = new COSMBlock( m_osm, 0.000009004, "streets.json" );
    }

    /**
     * testin mapping
     */
    @Test
    public void testMap()
    {
        Assume.assumeNotNull( m_osm );
        Assume.assumeNotNull( m_env );
        m_env.map();
    }

    /**
     * main
     * @param p_args cli
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {
        final TestCOSMBlockSmall l_test = new TestCOSMBlockSmall();
        l_test.init();
        l_test.testMap();
    }

}
