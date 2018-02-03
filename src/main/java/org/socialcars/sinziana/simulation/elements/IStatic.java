package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.INode;

/**
 * the interface for static agents like client and provider
 */
public interface IStatic extends IElement
{
    /**
     * location
     * @return location
     *
     * @todo wieso muss die Location von außen sichtbar sein?
     */
    INode location();
}
