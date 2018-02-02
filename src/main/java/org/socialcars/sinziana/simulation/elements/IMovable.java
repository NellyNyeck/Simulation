package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.INode;

import java.util.Collection;

/**
 * the interface for movable agents
 */
public interface IMovable extends IElement
{
    Number getSpeed();

    Number getLocation();

    void setSpeed( Number p_speed );

    void setLocation( Number p_location );

    INode getStart();

    INode getFinish();

    Collection<? extends INode> getMiddle();

    Number getMaxAccel();

    Number getMaxDeccel();

    Number getMaxSpeed();
}
