package org.socialcars.sinziana.simulation.events;

import org.socialcars.sinziana.simulation.elements.IMovable;
import org.socialcars.sinziana.simulation.environment.jung.CNode;

import java.util.Collection;

/**
 * event interface
 */
public interface IEvent
{
    IMovable who();

    EEvenType what();

    CNode where();

    Collection<IMovable> with();

    Number when();

}
