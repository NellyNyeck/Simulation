package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.HashMap;

/**
 * the block class
 */
public class CBlock implements IBlock
{
    private final String m_id;
    private final Double m_coord1;
    private final Double m_coord2;

    private final HashMap<String, CBlock> m_up;
    private final HashMap<String, CBlock> m_down;
    private final HashMap<String, CBlock> m_left;
    private final HashMap<String, CBlock> m_right;

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
        m_up = new HashMap<>();
        m_down = new HashMap<>();
        m_left = new HashMap<>();
        m_right = new HashMap<>();
    }

    public void addUp( final CBlock p_up )
    {
        m_up.put( p_up.id(), p_up );
    }

    public void addDown( final CBlock p_down )
    {
        m_down.put( p_down.id(), p_down );
    }

    public void addRight( final CBlock p_right )
    {
        m_right.put( p_right.id(), p_right );
    }

    public void addLeft( final CBlock p_left )
    {
        m_left.put( p_left.id(), p_left );
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
    public HashMap<String, CBlock> up()
    {
        return m_up;
    }

    @Override
    public HashMap<String, CBlock> down()
    {
        return m_down;
    }

    @Override
    public HashMap<String, CBlock> right()
    {
        return m_right;
    }

    @Override
    public HashMap<String, CBlock> left()
    {
        return m_left;
    }

    @Override
    public boolean occupied()
    {
        if ( m_occupiedby != null ) return true;
        return false;
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
        if ( m_right.containsValue( p_block ) ) return true;
        if ( m_left.containsValue( p_block ) ) return  true;
        if ( m_down.containsValue( p_block ) ) return  true;
        if ( m_up.containsValue( p_block ) ) return true;
        return false;
    }

}
