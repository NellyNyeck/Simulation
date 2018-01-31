package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinates;

/**
 * the interface for the node object
 * @param <T> generic type
 */
public interface IIntersection<T>
{

    T name();

    CCoordinates coord();

}
