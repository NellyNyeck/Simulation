package org.socialcars.sinziana.simulation.elements;

/**
 * interface for preference structure
 */
public interface IPreference
{
    Integer destination();

    Double minSpeed();

    Double maxSpeed();

    Integer timeLimit();

    Double lengthLimit();

}
