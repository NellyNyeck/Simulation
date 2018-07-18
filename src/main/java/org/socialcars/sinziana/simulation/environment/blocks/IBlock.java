package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.HashMap;

/**
 * the block interface
 */
public interface IBlock
{
    String id();

    HashMap<String, CBlock> up();

    HashMap<String, CBlock> down();

    HashMap<String, CBlock> right();

    HashMap<String, CBlock> left();

    boolean occupied();

    void occupy( final IElement p_el );

    void release( final IElement p_el );
}
