package org.socialcars.sinziana.simulation.environment.blocks;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;
import org.socialcars.sinziana.simulation.environment.osm.CStreetStructure;

import java.util.ArrayList;
import java.util.stream.IntStream;


/**
 * blocks for osm
 */
public class COSMBlock implements IBlockEnv
{

    private final Double m_blocksize;
    private ArrayList<CBlock> m_blocks;
    private final String m_connector;
    private final String m_filename;

    private final COSMEnvironment m_env;

    /**
     * ctor
     * @param p_env evironment
     * @param p_blocksize size of the block
     */
    public COSMBlock( final COSMEnvironment p_env, final Double p_blocksize, final String p_file )
    {
        m_env = p_env;
        m_blocksize = p_blocksize;
        m_blocks = new ArrayList<>();
        m_connector = "_";
        m_filename = p_file;
    }

    @Override
    public void map()
    {


        connecttheBlocks();
    }

    private void createLongStreet( final Double p_dist, final Double p_bearing, final CStreetStructure p_struct )
    {
        final CBlock l_start = new CBlock( p_struct.start().getLatitude() + m_connector + p_struct.start().getLongitude(),
            p_struct.start().getLatitude(), p_struct.start().getLongitude()  );
        final CBlock l_end = new CBlock( p_struct.end().getLatitude() + m_connector + p_struct.end().getLongitude(),
            p_struct.end().getLatitude(), p_struct.end().getLongitude()  );
        final CBlock[] l_temp = {l_start};
        IntStream.range( 1, (int) ( p_dist / m_blocksize  + 1 ) )
            .boxed()
            .forEach( i ->
            {
                final GeoPosition l_pos = m_env.calculateNext( new GeoPosition( l_temp[0].get1(), l_temp[0].get2() ), m_blocksize, p_bearing );
                final CBlock l_new = new CBlock( l_pos.getLatitude() + "_" + l_pos.getLongitude(), l_pos.getLatitude(), l_pos.getLongitude() );
                connectwithBearing( p_bearing, l_temp[0], l_new );
                l_temp[0] = l_new;
            } );
        connectwithBearing( p_bearing, l_temp[0], l_end );
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
