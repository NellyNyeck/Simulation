package org.socialcars.sinziana.simulation.environment;

/**
 * the coordinates interface
 */
public interface ICoordinate
{
    /**
     * gets coordinate type
     * @return coordinate type
     */
    String type();

    /**
     * gets first coordinate
     * @return first coordinate
     */
    Number firstCoordinate();

    /**
     * gets second coordinate
     * @return second coordinate
     */
    Number secondCoordinate();

}
