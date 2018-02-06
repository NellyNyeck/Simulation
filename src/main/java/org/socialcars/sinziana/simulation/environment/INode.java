package org.socialcars.sinziana.simulation.environment;

/**
 * the interface for the node object
 */
public interface INode
{
    /**
     * returns the name of the node
     * @return the string id
     */
    String id();

    /**
     * returns the coordinates of the node
     * @return the coordinates
     */
    CCoordinate coordinate();
}
