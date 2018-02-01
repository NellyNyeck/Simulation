package org.socialcars.sinziana.simulation.elements;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    Number getSpeed();

    Number getLocation();

    void setSpeed( Number p_speed );

    void setLocation( Number p_location );

    Object getStart();

    Object getFinish();

    Object getMiddle();

    Number getMaxAccel();

    Number getMaxDeccel();

    Number getMaxSpeed();
}
