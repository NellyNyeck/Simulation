package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * blocks for osm
 */
public class COSMBlock implements IBlockEnv
{

    private final Double m_blocksize;
    private TreeSet<CBlock> m_blocks;

    private final COSMEnvironment m_env;

    /**
     * ctor
     * @param p_env evironment
     * @param p_blocksize size of the block
     */
    public COSMBlock( final COSMEnvironment p_env, final Double p_blocksize )
    {
        m_env = p_env;
        m_blocksize = p_blocksize;
        m_blocks = new TreeSet<>();
    }

    @Override
    public void map()
    {
        m_env.getGeopoints().forEach( p ->
        {
            final double l_roundlat = Math.round( p.getLatitude() * 1000000000 ) / 1000000000;
            final double l_roundlong = Math.round( p.getLongitude() * 1000000000 ) / 1000000000;
            m_blocks.add( new CBlock( l_roundlat + "_" + l_roundlong, l_roundlat, l_roundlong ) );
        } );
        connecttheBlocks();
    }

    private void connecttheBlocks()
    {
        final Iterator<CBlock> l_it = m_blocks.iterator();
        final CBlock[] l_prev = {l_it.next()};
        l_it.forEachRemaining( b ->
        {
            final double l_latdiff = b.get1() - l_prev[0].get1();
            final double l_longdiff = b.get2() - l_prev[0].get2();
            if ( l_longdiff == 0 )
            {
                if ( l_latdiff == 0.000009004 )
                {
                    b.addLeft( l_prev[0] );
                    l_prev[0].addRight( b );
                }
                else if ( l_latdiff == -0.000009004 )
                {
                    b.addRight( l_prev[0] );
                    l_prev[0].addLeft( b );
                }
            }
            else if ( l_latdiff == 0 )
            {
                if ( l_longdiff == 0.000009004 )
                {
                    b.addDown( l_prev[0] );
                    l_prev[0].addUp( b );
                }
                else if ( l_longdiff == -0.000009004 )
                {
                    b.addUp( l_prev[0] );
                    l_prev[0].addDown( b );
                }
            }
            l_prev[0] = b;
        } );
    }

    @Override
    public Number getBlockSize()
    {
        return m_blocksize;
    }
}
