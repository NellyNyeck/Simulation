package org.socialcars.sinziana.simulation.units;

/**
 * the units interface
 *
 * @todo Dokumentation fehlt!
 * @todo Benennung ist mal wieder schlecht, was ist "dist", was ist "accel", was macht "newPosition"?
 */
public interface IUnits
{
    Number distToBlock( Number p_dist );

    Number blockToDist( Number p_blocks );

    Number speedToDist( Number p_speed );

    Number accelToSpeed( Number p_acceleration );

    Number distanceToSpeed( Number p_distance );

    Number speedToBlocks( Number p_speed );

    Number stepsToTimesteps( Number p_steps );

    Number newPosition( Number p_oldposition, Number p_speed );
}
