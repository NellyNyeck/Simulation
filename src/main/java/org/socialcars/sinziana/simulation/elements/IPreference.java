package org.socialcars.sinziana.simulation.elements;

/**
 * preference interface
 */
public interface IPreference
{
    Integer destination();

    Double minSpeed();

    Double maxSpeed();

    Integer timeLimit();

    Double lengthLimit();
}
