package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.storage.Graph;

public class CMyGraphHopper extends GraphHopper
{
    private final String m_demandfile;
    private final Integer m_speed;

    public CMyGraphHopper( final String p_demandfile, final Integer p_speed)
    {

        m_demandfile = p_demandfile;
        m_speed = p_speed;
    }

    @Override
    public Weighting createWeighting( HintsMap p_hintsMap, FlagEncoder p_encoder, Graph p_graph )
    {
        String weightingStr = p_hintsMap.getWeighting().toLowerCase();
        if ("my_custom_weighting".equals( weightingStr ) )
        {
            return new CTrafficDemand( m_demandfile, p_encoder, m_speed );
        } else
            {
            return super.createWeighting( p_hintsMap, p_encoder, p_graph);
        }
    }
}
