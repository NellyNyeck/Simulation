package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Before;
import org.junit.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

/**
 * testing the osm environment
 */
public class TestCOSMmap
{

    private COSMEnvironment m_env;

    @Before
    public void init()
    {
        m_env = new COSMEnvironment( "src/test/resources/netherlands-latest.osm.pbf",  52.378023, 52.358227,  4.926724,  4.874718 );
    }

    /**
     * testing the random position
     */
    @Test
    public void randomPosition()
    {
        final GeoPosition l_newnode = m_env.randomnode();
        System.out.println( l_newnode.getLatitude() );
        System.out.println( l_newnode.getLongitude() );
    }

}
