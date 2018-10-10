package org.socialcars.sinziana.simulation.environment.osm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.util.EdgeIteratorState;
import org.socialcars.sinziana.simulation.data.input.CDensitiespojo;
import org.socialcars.sinziana.simulation.environment.demand.CDensity;

import java.io.File;
import java.io.IOException;

/**
 * class depincting the traffic demand example as weights on the edges
 */
public class CTrafficDemand implements Weighting
{
    private final CDensity m_map;
    private final Double m_speed;
    private final FlagEncoder m_flagencoder;


    /**
     * ctor
     * @param p_filename the file with the weights
     * @param p_encoder encoder
     * @param p_speed average speed
     * @throws IOException file
     */
    public CTrafficDemand( final String p_filename, final FlagEncoder p_encoder, final Double p_speed ) throws IOException
    {
        m_flagencoder = p_encoder;
        m_speed = p_speed;
        final CDensitiespojo l_input;
        l_input = new ObjectMapper().readValue( new File( p_filename ), CDensitiespojo.class );
        m_map = new CDensity( l_input.getDensity() );
    }


    @Override
    public double getMinWeight( final double p_weight )
    {
        return 0;
    }

    @Override
    public double calcWeight( final EdgeIteratorState p_edge, final boolean p_reverse, final int p_nextprevedgeid )
    {
        if ( m_map.getDensity( String.valueOf( p_edge.getEdge() ) ) != null )
        {
            return m_map.getDensity( String.valueOf( p_edge.getEdge() ) );
        }
        else return Integer.MAX_VALUE;
    }

    @Override
    public long calcMillis( final EdgeIteratorState p_edge, final boolean p_reverse, final int p_nextprevedgeid )
    {
        if ( m_map.getDensity( String.valueOf( p_edge.getEdge() ) ) != null )
        {
            return (long) ( ( p_edge.getDistance() / m_speed ) / m_map.getDensity( String.valueOf( p_edge.getEdge() ) ) );
        }
        return 100;
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
