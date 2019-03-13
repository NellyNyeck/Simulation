package org.socialcars.sinziana.simulation.events;

import org.socialcars.sinziana.simulation.elements.IMovable;

import java.util.Collection;

/**
 * event interface
 */
public interface IEvent
{
    IMovable who();

    EEvenType what();

    String where();

    Collection<IMovable> with();

    Number when();

}
