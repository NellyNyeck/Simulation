package org.socialcars.sinziana.simulation;

import com.graphhopper.reader.osm.OSMReader;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.GraphExtension;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.storage.RAMDirectory;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.File;
import java.io.IOException;


/**
 * main application
 */
public final class CMain
{
    /**
     * ctor
     */
    private CMain()
    {}

    /**
     * main funtcion
     * @param p_args cli arguments
     */
    public static void main( final String[] p_args ) throws IOException
    {
        GraphHopperStorage m_storage = new GraphHopperStorage( new RAMDirectory(), new EncodingManager("car"), false, new GraphExtension.NoOpExtension() );
        OSMReader m_reader = new OSMReader(m_storage);
        m_reader.setFile( new File( "src/test/resources/new-york-latest.osm.pbf" ));
        m_reader.readGraph();
    }
}
