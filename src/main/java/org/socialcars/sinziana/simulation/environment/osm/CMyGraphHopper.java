package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;

import java.io.IOException;

/**
 * class for weighted option of Graph
 */
public class CMyGraphHopper extends GraphHopperOSM
{
    private final String m_demandfile;
    private final Double m_speed;

    /**
     * ctor
     * @param p_demandfile file with the demands
     * @param p_speed average considered speed
     */
    public CMyGraphHopper( final String p_demandfile, final Double p_speed )
    {

        m_demandfile = p_demandfile;
        m_speed = p_speed;
    }

    @Override
    public Weighting createWeighting( final HintsMap p_hintsmap, final FlagEncoder p_encoder, final Graph p_graph )
    {
        try
        {
            return new CTrafficDemand( m_demandfile, p_encoder, m_speed );
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
        return null;
    }

}
