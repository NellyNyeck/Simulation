package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import org.jxmapviewer.JXMapViewer;
import org.socialcars.sinziana.simulation.elements.IElement;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;


/**
 * test osm graph
 *
 * @see http://download.geofabrik.de/
 */
public class COSMEnvironment implements IEnvironment<JXMapViewer>
{
    /**
     * graphhopper instance
     */
    private final CGraphHopper m_graph;



    public COSMEnvironment( final String p_downloadurl )
    {
        m_graph = new CGraphHopper( p_downloadurl );
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final INode... p_via
    )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final Stream<INode> p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final String... p_via )
    {
        return null;
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final Stream<String> p_via )
    {
        return null;
    }

    @Override
    public String randomnodebyname()
    {
        return null;
    }

    @Override
    public INode randomnode()
    {
        return null;
    }

    @Override
    public IEnvironment initialize( final IElement... p_element )
    {
        return null;
    }

    @Override
    public JXMapViewer panel( final Dimension p_dimension )
    {
        return null;
    }

    /**
     * modified graphhopper structure
     */
    private static final class CGraphHopper extends GraphHopper
    {

        CGraphHopper( final String p_downloadurl )
        {
            final File l_graphlocation = storage( p_downloadurl ).toFile();
            try
            {
                if ( !this.load( l_graphlocation.getAbsolutePath() ) )
                    this.downloadandload( p_downloadurl, l_graphlocation );

            }
            catch ( final Exception l_exception )
            {
                this.downloadandload( p_downloadurl, l_graphlocation );
            }
        }

        /**
         * download graph and run converting
         *
         * @param p_url download URL
         * @param p_directory directory in which the graph data is stored
         */
        private void downloadandload( final String p_url, final File p_directory )
        {
            try
            {
                this.setDataReaderFile( downloadosmpbf(p_url).getAbsolutePath() );
                this.setGraphHopperLocation( p_directory.toString() );
                this.setEncodingManager( new EncodingManager( "car") );

                this.initDataReader( this.importData() );
            }
            catch ( final IOException l_exception )
            {
                throw new RuntimeException( l_exception );
            }
        }

        /**
         * downloads the OSM data
         *
         * @param p_url URL for downloading as string
         * @return download file with full path
         */
        private static File downloadosmpbf( final String p_url ) throws IOException
        {
            final File l_output = File.createTempFile( "openstreetmap", ".osm.pbf" );
            final URL l_url = new URL( p_url );

            final ReadableByteChannel l_channel = Channels.newChannel( l_url.openStream() );
            final FileOutputStream l_stream = new FileOutputStream( l_output );
            l_stream.getChannel().transferFrom( l_channel, 0, Long.MAX_VALUE );

            return l_output;
        }

        /**
         * creates a storage of the graph
         *
         * @param p_downloadurl url
         * @return path
         */
        private static Path storage( final String p_downloadurl )
        {
            try
            {
                final Path l_path = Paths.get(
                        System.getProperty("user.home"),
                        ".osmstorage",

                        getBytes2Hex(
                                MessageDigest.getInstance( "MD5" )
                                        .digest( p_downloadurl.getBytes( "UTF-8" ) )
                        )

                );

                Files.createDirectories(l_path );
                return l_path;
            }
            catch ( final UnsupportedEncodingException | NoSuchAlgorithmException l_exception )
            {
                throw new RuntimeException( l_exception );
            }
            catch ( final IOException l_exception )
            {
                throw new UncheckedIOException( l_exception );
            }
        }

        /**
         * returns a string with hexadecimal bytes
         *
         * @param p_bytes input bytes
         * @return hexadecimal string
         */
        private static String getBytes2Hex( final byte[] p_bytes )
        {
            final StringBuilder l_str = new StringBuilder( 2 * p_bytes.length );
            for ( final byte l_byte : p_bytes )
                l_str.append( String.format( "%02x", l_byte & 0xff ) );

            return l_str.toString();
        }
    }
}
