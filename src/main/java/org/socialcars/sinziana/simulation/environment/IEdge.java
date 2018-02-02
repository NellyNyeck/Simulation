package org.socialcars.sinziana.simulation.environment;

/**
 * the street interface
 */
public interface IEdge
{

    String name();

    String from();

    String to();

    Number weight();

    //TODO: 02.02.18 do the function
    Object function();

}
