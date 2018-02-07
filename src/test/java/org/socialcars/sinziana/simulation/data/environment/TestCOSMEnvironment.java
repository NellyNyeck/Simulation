package org.socialcars.sinziana.simulation.data.environment;

import org.junit.Test;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;


/**
 * test osm graph
 */
public final class TestCOSMEnvironment
{
    /**
     * test graph
     */
    @Test
    public void graph()
    {
        new COSMEnvironment( "http://download.geofabrik.de/europe/netherlands-latest.osm.pbf" );
    }

}
