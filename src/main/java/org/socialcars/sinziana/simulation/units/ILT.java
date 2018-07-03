package org.socialcars.sinziana.simulation.units;

import java.time.Duration;
import java.time.LocalTime;

/**
 * interface for time-keeping
 */
public interface ILT
{
    LocalTime timePassed( LocalTime p_targettime );

    LocalTime timeUntil( LocalTime p_targettime );

    LocalTime timeAfter( Duration p_duration );
}
