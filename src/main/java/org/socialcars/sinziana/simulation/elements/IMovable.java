package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.events.CEvent;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    String position();

    Number speed();

    Number acceleration();

    String destination();

    void move( final String p_newpostion );

    void event( final CEvent p_event );
}
