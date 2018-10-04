package org.socialcars.sinziana.simulation.environment.demand;

import org.socialcars.sinziana.simulation.data.input.CDensitypojo;

import java.util.HashMap;
import java.util.Set;

/**
 * density class
 */
public class CDensity implements IDensity
{

    private final Double m_speed;
    private final HashMap<Integer, Double> m_densities;

    /**
     * ctor
     * @param p_speed speed
     * @param p_densities densities set
     */
    public CDensity( final Double p_speed, final Set<CDensitypojo> p_densities )
    {
        m_speed = p_speed;
        m_densities = new HashMap<>();
        p_densities.forEach( d ->
        {
            m_densities.put( d.getId(), d.getDensity() );
        } );
    }

    @Override
    public Double getDensity( final int p_edge )
    {
        return m_densities.get( p_edge );
    }

    /**
     * prints all edges
     */
    public void getAll()
    {
        m_densities.keySet().forEach( k ->
        {
            System.out.println( getDensity( k ) );
        } );
    }
}
