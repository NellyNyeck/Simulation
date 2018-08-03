package org.socialcars.sinziana.simulation.environment.blocks;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.CStreetStructure;

import java.io.IOException;
import java.util.ArrayList;


/**
 * blocks for osm
 */
public class COSMBlock implements IBlockEnv
{
    private static final Double RADIUS = 6371e3;

    private final Double m_blocksize;
    private ArrayList<CBlock> m_blocks;
    private final String m_connector;
    private final String m_filename;



    /**
     * ctor
     * @param p_blocksize size of the block
     */
    public COSMBlock( final Double p_blocksize, final String p_file )
    {
        m_blocksize = p_blocksize;
        m_blocks = new ArrayList<>();
        m_connector = "_";
        m_filename = p_file;
    }

    @Override
    public void map()
    {
        final ArrayList<CStreetStructure> l_streets;
        /*try
        {
            l_streets = readFile();
            l_streets.forEach( s ->
            {
                final Double l_dist = calculateDistance( s.start(), s.end() );
                final Double l_bearingb = calculateBearing( s.start(), s.end() );

                if ( l_dist < m_blocksize )
                {
                    final CBlock l_new = new CBlock( s.start().getLatitude() + m_connector + s.start().getLongitude(),
                        s.start().getLatitude(), s.start().getLongitude()  );
                    m_blocks.add( l_new );
                }
                else if ( l_dist / m_blocksize < 2 )
                {
                    final CBlock l_new1 = new CBlock( s.start().getLatitude() + m_connector + s.start().getLongitude(),
                        s.start().getLatitude(), s.start().getLongitude()  );
                    m_blocks.add( l_new1 );
                    final CBlock l_new2 = new CBlock( s.end().getLatitude() + m_connector + s.end().getLongitude(),
                        s.end().getLatitude(), s.end().getLongitude()  );
                    m_blocks.add( l_new2 );
                    connectwithBearing( l_bearingb, l_new1, l_new2 );

                }
                else if ( l_dist / m_blocksize >= 2 )
                {
                    createLongStreet( l_dist, l_bearingb, s );
                }
            } );
            connecttheBlocks();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
        catch ( final ParseException l_err )
        {
            l_err.printStackTrace();
        }*/

    }

    private void createLongStreet( final Double p_dist, final Double p_bearing, final CStreetStructure p_struct )
    {
       /* final CBlock l_start = new CBlock( p_struct.start().getLatitude() + m_connector + p_struct.start().getLongitude(),
            p_struct.start().getLatitude(), p_struct.start().getLongitude()  );
        m_blocks.add( l_start );
        final CBlock l_end = new CBlock( p_struct.end().getLatitude() + m_connector + p_struct.end().getLongitude(),
            p_struct.end().getLatitude(), p_struct.end().getLongitude()  );
        m_blocks.add( l_end );
        final CBlock[] l_temp = {l_start};
        IntStream.range( 1, (int) ( p_dist / m_blocksize  + 1 ) )
            .boxed()
            .forEach( i ->
            {
                final GeoPosition l_pos = calculateNext( new GeoPosition( l_temp[0].get1(), l_temp[0].get2() ), m_blocksize, p_bearing );
                final CBlock l_new = new CBlock( l_pos.getLatitude() + "_" + l_pos.getLongitude(), l_pos.getLatitude(), l_pos.getLongitude() );
                m_blocks.add( l_new );
                connectwithBearing( p_bearing, l_temp[0], l_new );
                l_temp[0] = l_new;
            } );
        connectwithBearing( p_bearing, l_temp[0], l_end ); */
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
                if ( ( ( calculateDistance( new GeoPosition( b.get1(), b.get2() ), new GeoPosition( bl.get1(), bl.get2() ) ) ) < 0.000009004 )
                    && ( !b.isNeighbour( bl ) ) )
                {
                    connectwithBearing( calculateBearing( new GeoPosition( b.get1(), b.get2() ), new GeoPosition( bl.get1(), bl.get2() ) ), b, bl );
                }
            } );
        } );
    }

    private ArrayList<CStreetStructure> readFile() throws IOException, ParseException
    {
        /*CStreetspojo  l_streetsinput = new ObjectMapper().readValue( new File( m_filename ), CStreetspojo.class );
        final JSONParser l_parser = new JSONParser();
        final Set<CStreetStructure> l_array = l_streetsinput.getAdditionalProperties();

        final ArrayList<CStreetStructure> l_streets = new ArrayList<>();
        l_array.forEach(  o ->
        {
            final JSONObject l_obj = (JSONObject) o;
            final Long l_id = (Long) l_obj.get( "id" );
            final Integer l_ai = l_id.intValue();
            l_streets.add( new CStreetStructure(  ) );
        } );

        return l_streets;*/

        return null;
    }


    private GeoPosition newGeo( final Object p_array )
    {
        final JSONArray l_obj = (JSONArray) p_array;
        final Double l_lat = (Double) l_obj.get( 0 );
        final Double l_long = (Double) l_obj.get( 1 );
        return new GeoPosition( l_lat, l_long );
    }

    /**
     * calculates the distance between two geopoints
     * @param p_one first geopoint
     * @param p_two second geopoint
     * @return distance in meters
     */
    public Double calculateDistance( final GeoPosition p_one, final GeoPosition p_two )
    {

        final Double l_deltalat = Math.toRadians( p_two.getLatitude() ) - Math.toRadians( p_one.getLatitude() );
        final Double l_deltalon = Math.toRadians( p_two.getLongitude() ) - Math.toRadians( p_one.getLongitude() );
        final Double l_alpha = Math.sin( l_deltalat / 2 ) * Math.sin( l_deltalat / 2 )
            + Math.cos( Math.toRadians( p_one.getLatitude() ) ) * Math.cos( Math.toRadians( p_two.getLatitude() ) )
            * Math.sin( l_deltalon / 2 ) * Math.sin( l_deltalon / 2 );
        return RADIUS * ( 2 * Math.atan2( Math.sqrt( l_alpha ), Math.sqrt( 1 - l_alpha ) ) );
    }

    /**
     * calculates the bearing of a street
     * @param p_one biginning of the street
     * @param p_two end of the street
     * @return bearing in degrees ( 0 - 360 )
     */
    public Double calculateBearing( final GeoPosition p_one, final GeoPosition p_two )
    {
        final Double l_yg = Math.sin( p_two.getLongitude() - p_one.getLatitude() ) * Math.cos( p_two.getLatitude() );
        final Double l_xs = Math.cos( p_one.getLatitude() ) * Math.sin( p_two.getLatitude() )
            - Math.sin( p_one.getLatitude() ) * Math.cos( p_two.getLatitude() ) * Math.cos( p_two.getLongitude() - p_one.getLongitude() );
        return Math.toDegrees( Math.atan2( l_yg, l_xs ) );
    }

    /**
     * calculates next Geoposition on said street at the given distance considering the streets bearing
     * @param p_pos first position
     * @param p_dist the distance
     * @param p_brng the bearing
     * @return the new geoposition
     */
    public GeoPosition calculateNext( final GeoPosition p_pos, final Double p_dist, final Double p_brng )
    {
        final Double l_lat2 = Math.asin( Math.sin( p_pos.getLatitude() ) * Math.cos( p_dist / RADIUS )
            + Math.cos( p_pos.getLatitude() ) * Math.sin( p_dist / RADIUS ) * Math.cos( p_brng ) );
        final Double l_long2 = p_pos.getLongitude() + Math.atan2( Math.sin( p_brng ) * Math.sin( p_dist / RADIUS ) * Math.cos( p_pos.getLatitude() ),
            Math.cos( p_dist / RADIUS ) - Math.sin( p_pos.getLatitude() ) * Math.sin( l_lat2 ) );
        return new GeoPosition( l_lat2, l_long2 );
    }

    @Override
    public Number getBlockSize()
    {
        return m_blocksize;
    }

    /**
     * to see the connections of a block
     */
    public void connection( final int p_pos )
    {
        final CBlock l_block = m_blocks.get( p_pos );
        System.out.println( l_block.id() );
        System.out.println();
        l_block.left().forEach( b ->
        {
            System.out.println( b.id() );
        } );
        System.out.println();
        l_block.right().forEach( b ->
        {
            System.out.println( b.id() );
        } );

    }
}
