package org.socialcars.sinziana.simulation.elements;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.units.CUnits;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    String name();

    String location();

    String origin();

    String destination();

    Double position();

    void move( final CUnits p_unit );

    void departed( final IEdge p_edge, final Integer p_timestep );

    void departed( final GeoPosition p_pos, final Integer p_timestep );

    void arrived( final IEdge p_edge, final Integer p_timestep );

    void arrived( final GeoPosition p_pos, final Integer p_timestep );
}
