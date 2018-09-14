package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.util.EdgeIteratorState;

import java.util.HashMap;

/**
 * class depincting the traffic demand example as weights on the edges
 */
public class CTrafficDemand implements Weighting
{
    private final HashMap<Integer, Float> m_map;
    private final Integer m_speed;
    private final FlagEncoder m_flagencoder;


    /**
     * ctor
     * @param p_filename the file with the weights
     * @param p_encoder encoder
     * @param p_speed average speed
     */
    public CTrafficDemand( final String p_filename, final FlagEncoder p_encoder, final Integer p_speed )
    {
        m_flagencoder = p_encoder;
        m_speed = p_speed;
        m_map = new HashMap<>();

    }


    @Override
    public double getMinWeight( final double p_weight )
    {
        return 0;
    }

    @Override
    public double calcWeight( final EdgeIteratorState p_edge, final boolean p_reverse, final int p_nextprevedgeid )
    {
        return m_map.get( p_edge.getEdge() );
    }

    @Override
    public long calcMillis( final EdgeIteratorState p_edge, final boolean p_reverse, final int p_nextprevedgeid )
    {
        return (long) ( ( p_edge.getDistance() / m_speed ) / m_map.get( p_edge.getEdge() ) );
    }

    @Override
    public FlagEncoder getFlagEncoder()
    {
        return m_flagencoder;
    }

    @Override
    public String getName()
    {
        return "trafficDemand";
    }

    @Override
    public boolean matches( final HintsMap p_hintsmap )
    {
        return false;
    }
}
