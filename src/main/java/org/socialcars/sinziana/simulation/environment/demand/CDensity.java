package org.socialcars.sinziana.simulation.environment.demand;

import org.socialcars.sinziana.simulation.data.input.CDensitypojo;

import java.util.HashMap;
import java.util.Set;

/**
 * density class
 */
public class CDensity implements IDensity
{

    private final HashMap<String, Double> m_densities;

    /**
     * ctor
     * @param p_densities densities set
     */
    public CDensity(  final Set<CDensitypojo> p_densities )
    {
        m_densities = new HashMap<>();
        p_densities.forEach( d -> m_densities.put( String.valueOf( d.getId() ), d.getDensity() ) );
    }

    @Override
    public Double getDensity( final String p_edge )
    {
        return m_densities.get( p_edge );
    }

    /**
     * prints all edges
     */
    public void getAll()
    {
        m_densities.keySet().forEach( k -> System.out.println( getDensity( k ) ) );
    }
}
