package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.data.input.CStart;

import java.util.List;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    Number getSpeed();

    Number getLocation();

    void setSpeed( Number p_speed );

    void setLocation( Number p_location );

    CStart getStart();

    CStart getFinish();

    List<CStart> getMiddle();

    Number getMaxAccel();

    Number getMaxDeccel();

    Number getMaxSpeed();
}
