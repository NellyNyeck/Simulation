package org.socialcars.sinziana.simulation.environment.osm;

import org.jxmapviewer.viewer.GeoPosition;

/**
 * Interface for a street in OSM, holds id, name, beginning and end geopoint
 */
public interface IEdgeStructure
{
    int id();

    String name();

    GeoPosition start();

    GeoPosition end();
}
