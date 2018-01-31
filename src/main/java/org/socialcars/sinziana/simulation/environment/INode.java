package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.data.input.CCoordinates;

/**
 * the interface for the node object
 * @param <T> generic type
 */
public interface INode<T>
{

    T id();

    CCoordinates coord();

}
