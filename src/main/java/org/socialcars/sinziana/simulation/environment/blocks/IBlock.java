package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.ArrayList;

/**
 * the block interface
 */
public interface IBlock
{
    String id();

    ArrayList<CBlock> up();

    ArrayList<CBlock> down();

    ArrayList<CBlock> right();

    ArrayList<CBlock> left();

    boolean occupied();

    void occupy( final IElement p_el );

    void release( final IElement p_el );
}
