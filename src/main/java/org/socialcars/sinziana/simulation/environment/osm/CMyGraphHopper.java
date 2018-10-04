package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.Graph;

/**
 * class for weighted option of Graph
 */
public class CMyGraphHopper extends GraphHopperOSM
{
    private final String m_demandfile;
    private final Integer m_speed;

    /**
     * ctor
     * @param p_demandfile file with the demands
     * @param p_speed average considered speed
     */
    public CMyGraphHopper( final String p_demandfile, final Integer p_speed )
    {

        m_demandfile = p_demandfile;
        m_speed = p_speed;
    }

    @Override
    public Weighting createWeighting( final HintsMap p_hintsmap, final FlagEncoder p_encoder, final Graph p_graph )
    {
        final String l_weighting = p_hintsmap.getWeighting().toLowerCase();
        if ( "my_custom_weighting".equals( l_weighting ) )
        {
            return new CTrafficDemand( m_demandfile, p_encoder, m_speed );
        }
        else
        {
            return super.createWeighting( p_hintsmap, p_encoder, p_graph );
        }
    }
}
