package org.socialcars.sinziana.simulation.environment.blocks;

/**
 * block environment interface
 */
public interface IBlockEnv
{
    void map( final Object p_obj );

    /**
     * gets blocksize
     * @return blocksize
     */
    Number getBlockSize();

}
