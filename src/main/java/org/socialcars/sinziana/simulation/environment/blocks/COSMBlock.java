package org.socialcars.sinziana.simulation.environment.blocks;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.CEdgeStructure;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.IntStream;


/**
 * blocks for osm
 */
public class COSMBlock implements IBlockEnv
{

    private final Double m_blocksize;
    private ArrayList<CBlock> m_blocks;
    private final String m_connector;

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
        m_blocks = new ArrayList<>();
        m_connector = "_";
    }

    @Override
    public void map()
    {
        final HashMap<Integer, CEdgeStructure> l_edges = m_env.getEdges();
        final Set<Integer> l_keys = l_edges.keySet();
        l_keys.forEach( k ->
        {
            final Double l_dist = m_env.calculateDistance( l_edges.get( k ).start(), l_edges.get( k ).end() );
            final Double l_bearingb = m_env.calculateBearing( l_edges.get( k ).start(), l_edges.get( k ).end() );

            if ( l_dist < m_blocksize )
            {
                final CBlock l_new = new CBlock( l_edges.get( k ).start().getLatitude() + m_connector + l_edges.get( k ).start().getLongitude(),
                    l_edges.get( k ).start().getLatitude(), l_edges.get( k ).start().getLongitude()  );
                m_blocks.add( l_new );
            }
            else if ( l_dist / m_blocksize < 2 )
            {
                final CBlock l_new1 = new CBlock( l_edges.get( k ).start().getLatitude() + m_connector + l_edges.get( k ).start().getLongitude(),
                    l_edges.get( k ).start().getLatitude(), l_edges.get( k ).start().getLongitude()  );
                final CBlock l_new2 = new CBlock( l_edges.get( k ).end().getLatitude() + m_connector + l_edges.get( k ).end().getLongitude(),
                    l_edges.get( k ).end().getLatitude(), l_edges.get( k ).end().getLongitude()  );
                connectwithBearing( l_bearingb, l_new1, l_new2 );
            }
            else if ( l_dist / m_blocksize >= 2 )
            {
                createLongStreet( l_dist, l_bearingb, l_edges.get( k ) );
            }
        } );
        connecttheBlocks();
    }

    private void createLongStreet( final Double p_dist, final Double p_bearing, final CEdgeStructure p_struct )
    {
        final CBlock l_start = new CBlock( p_struct.start().getLatitude() + m_connector + p_struct.start().getLongitude(),
            p_struct.start().getLatitude(), p_struct.start().getLongitude()  );
        final CBlock l_end = new CBlock( p_struct.end().getLatitude() + m_connector + p_struct.end().getLongitude(),
            p_struct.end().getLatitude(), p_struct.end().getLongitude()  );
        final CBlock[] l_temp = {l_start};
        final int l_nb = (int) ( p_dist / m_blocksize );
        IntStream.range( 1, l_nb + 1 )
            .boxed()
            .forEach( i ->
            {
                final GeoPosition l_pos = m_env.calculateNext( new GeoPosition( l_temp[0].get1(), l_temp[0].get2() ), m_blocksize, p_bearing );
                final CBlock l_new = new CBlock( l_pos.getLatitude() + "_" + l_pos.getLongitude(), l_pos.getLatitude(), l_pos.getLongitude() );
                connectwithBearing( p_bearing, l_temp[0], l_new );
                l_temp[0] = l_new;
            } );
    }

    private void connectwithBearing( final Double p_bearing, final CBlock p_one, final CBlock p_two )
    {
        if ( ( p_bearing >= 45 ) && ( p_bearing <= 135 ) )
        {
            p_one.addRight( p_two );
            p_two.addLeft( p_one );
        }
        else if ( ( p_bearing >= 135 ) && ( p_bearing <= 225 ) )
        {
            p_one.addDown( p_two );
            p_two.addUp( p_one );
        }
        else if ( ( p_bearing >= 225 ) && ( p_bearing <= 315 ) )
        {
            p_one.addLeft( p_two );
            p_two.addRight( p_one );
        }
        else
        {
            p_one.addUp( p_two );
            p_two.addDown( p_one );
        }
    }

    private void connecttheBlocks()
    {
        m_blocks.forEach( b ->
        {
            m_blocks.forEach( bl ->
            {
                if ( ( ( m_env.calculateDistance( new GeoPosition( b.get1(), b.get2() ), new GeoPosition( bl.get1(), bl.get2() ) ) ) < m_blocksize )
                    && ( !b.isNeighbour( bl ) ) )
                {
                    connectwithBearing( m_env.calculateBearing( new GeoPosition( b.get1(), b.get2() ), new GeoPosition( bl.get1(), bl.get2() ) ), b, bl );
                }
            } );
        } );
    }

    @Override
    public Number getBlockSize()
    {
        return m_blocksize;
    }
}
