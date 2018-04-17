package org.socialcars.sinziana.simulation.environment.demand;

import org.socialcars.sinziana.simulation.environment.jung.CCoordinate;

/**
 * interface for demand class
 */
public interface IInstance
{
    public CCoordinate from();

    public CCoordinate to();

    public Number howMany();

}
