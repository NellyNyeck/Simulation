package org.socialcars.sinziana.simulation.environment.demand.osm;

import org.socialcars.sinziana.simulation.environment.jung.CCoordinate;

/**
 * interface for demand class
 */
public interface IInstanceOSM
{
    public CCoordinate from();

    public CCoordinate to();

    public int howMany();

}
