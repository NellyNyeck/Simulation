package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.environment.blocks.COSMBlock;

import java.io.IOException;

/**
 * test class for osm block environment
 */
public class TestCOSMBlockSmall
{
    private COSMBlock m_env;

    /**
     * initialisation
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_env = new COSMBlock( 1.00, "BarcaSmallStreets.json" );
    }

    /**
     * testin mapping
     */
    @Test
    public void testMap()
    {
        Assume.assumeNotNull( m_env );
        m_env.map();
    }

    /**
     * testing the connections of a random block
     */
    @Test
    public void testConnection()
    {
        Assume.assumeNotNull( m_env );
        m_env.connection( 14 );
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
        l_test.testConnection();
    }

}
