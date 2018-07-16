package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.events.CEvent;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    Number position();

    void move( final Number p_newpostion );

    void event( final CEvent p_event );
}
