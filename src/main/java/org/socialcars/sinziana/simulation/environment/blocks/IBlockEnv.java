package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.elements.IMovable;

/**
 * block environment interface
 */
public interface IBlockEnv
{

    /**
     * gets blocksize
     * @return blocksize
     */
    Number getBlockSize();

    void move( IMovable p_agent );



}
