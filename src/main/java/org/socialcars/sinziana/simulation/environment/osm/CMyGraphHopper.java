package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.storage.Graph;

public class CMyGraphHopper extends GraphHopperOSM
{
    private final String m_demandfile;
    private final Integer m_speed;

    public CMyGraphHopper( final String p_demandfile, final Integer p_speed)
    {

        m_demandfile = p_demandfile;
        m_speed = p_speed;
    }

    @Override
    public Weighting createWeighting(HintsMap hintsMap, FlagEncoder encoder, Graph graph)
    {
        String l_weightingStr = hintsMap.getWeighting().toLowerCase();
        if ( "my_custom_weighting".equals( l_weightingStr ) )
        {
            return new CTrafficDemand( m_demandfile, encoder, m_speed );
        }
        else
            {
            return super.createWeighting( hintsMap, encoder, graph );
        }
    }
}
