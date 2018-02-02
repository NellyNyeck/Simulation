package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.CIntersection;

/**
 * the interface for static agents like client and provider
 */
public interface IStatic extends IElement
{
    CIntersection location();
}
