package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.CIntersection;
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

    CIntersection getStart();

    CIntersection getFinish();

    List<CIntersection> getMiddle();

    Number getMaxAccel();

    Number getMaxDeccel();

    Number getMaxSpeed();
}
