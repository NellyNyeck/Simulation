package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Assume;
import org.junit.Before;
import org.socialcars.sinziana.simulation.environment.blocks.COSMBlock;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;

/**
 * test class for osm block environment
 */
public class TestCOMSBlockLarge
{

    private COSMEnvironment m_osmenv;
    private COSMBlock m_env;

    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_osmenv = new COSMEnvironment( "src/test/resources/spain-latest.osm.pbf", "src/test/Barcelona", 41.420472, 41.374398,  2.203925, 2.144553 );
        m_env = new COSMBlock( 1.00, "streets.json" );
    }

    private void testmap()
    {
        Assume.assumeNotNull( m_osmenv );
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
        final TestCOMSBlockLarge l_test = new TestCOMSBlockLarge();
        l_test.init();
        l_test.testmap();
    }
}
