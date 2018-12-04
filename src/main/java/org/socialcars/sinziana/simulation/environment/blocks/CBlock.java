package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.ArrayList;

/**
 * the block class
 */
public class CBlock implements IBlock
{
    private final String m_id;
    private final Double m_coord1;
    private final Double m_coord2;

    private final ArrayList<CBlock> m_up;
    private final ArrayList<CBlock> m_down;
    private final ArrayList<CBlock> m_left;
    private final ArrayList<CBlock> m_right;

    private IElement m_occupiedby;

    /**
     * ctor
     * @param p_id id
     */
    public CBlock( final String p_id, final Double p_coo1, final Double p_coo2 )
    {
        m_id = p_id;
        m_coord1 = p_coo1;
        m_coord2 = p_coo2;
        m_up = new ArrayList<>();
        m_down = new ArrayList<>();
        m_left = new ArrayList<>();
        m_right = new ArrayList<>();
    }

    public void addUp( final CBlock p_up )
    {
        m_up.add( p_up );
    }

    public void addDown( final CBlock p_down )
    {
        m_down.add( p_down );
    }

    public void addRight( final CBlock p_right )
    {
        m_right.add( p_right );
    }

    public void addLeft( final CBlock p_left )
    {
        m_left.add( p_left );
    }

    public Double get1()
    {
        return m_coord1;
    }

    public Double get2()
    {
        return m_coord2;
    }

    @Override
    public String id()
    {
        return m_id;
    }

    @Override
    public ArrayList<CBlock> up()
    {
        return m_up;
    }

    @Override
    public ArrayList<CBlock> down()
    {
        return m_down;
    }

    @Override
    public ArrayList<CBlock> right()
    {
        return m_right;
    }

    @Override
    public ArrayList<CBlock> left()
    {
        return m_left;
    }

    @Override
    public boolean occupied()
    {
        return m_occupiedby != null;
    }

    @Override
    public void occupy( final IElement p_el )
    {
        if ( m_occupiedby == null ) m_occupiedby = p_el;
    }

    @Override
    public void release( final IElement p_el )
    {
        if ( m_occupiedby == p_el ) m_occupiedby = null;
    }

    /**
     * checks if the block is a neighbour
     * @param p_block given block
     * @return boolean
     */
    public boolean isNeighbour( final CBlock p_block )
    {
        if ( m_right.contains( p_block ) ) return true;
        if ( m_left.contains( p_block ) ) return  true;
        if ( m_down.contains( p_block ) ) return  true;
        return m_up.contains( p_block );
    }

}
