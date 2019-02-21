package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.events.CEvent;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    String position();

    Double speed();

    Double acceleration();

    String destination();

    void accelshift( final Double p_accel );

    void move( final String p_newpostion );

    void event( final CEvent p_event );

}
