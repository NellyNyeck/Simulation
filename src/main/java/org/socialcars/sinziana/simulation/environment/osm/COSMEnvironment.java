package org.socialcars.sinziana.simulation.environment.osm;

import com.graphhopper.GraphHopper;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.util.EncodingManager;
import org.jxmapviewer.JXMapViewer;
import org.socialcars.sinziana.simulation.elements.IElement;
import org.socialcars.sinziana.simulation.environment.IEdge;
import org.socialcars.sinziana.simulation.environment.IEnvironment;
import org.socialcars.sinziana.simulation.environment.INode;

import javax.annotation.Nonnull;
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
import java.util.Objects;
import java.util.stream.Stream;


/**
 * test osm graph.
 * With the enviornment variable OSMPATH a graph location directory
 * can be set
 *
 * @see http://download.geofabrik.de/
 */
public class COSMEnvironment implements IEnvironment<JXMapViewer>
{
    /**
     * graphhopper instance
     */
    private final GraphHopper m_graph = new GraphHopperOSM().forServer();


    public COSMEnvironment( @Nonnull final String p_downloadurl )
    {
        this( p_downloadurl, "car" );
    }

    /**
     * ctor
     *
     * @param p_downloadurl download url
     */
    public COSMEnvironment( @Nonnull final String p_downloadurl, @Nonnull String p_encoding ) throws IOException
    {
        final File l_graphlocation = storage( p_downloadurl ).toFile();

        m_graph.setEncodingManager( new EncodingManager( p_encoding ) );
        m_graph.setGraphHopperLocation( l_graphlocation.toString() );

        try
        {
            m_graph.importOrLoad();
        }
        catch ( final Exception l_exception )
        {
            m_graph.setDataReaderFile( downloadosmpbf( p_downloadurl ).toString() );
            m_graph.importOrLoad();
        }
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
            final String l_graphlocation = getBytes2Hex( MessageDigest.getInstance( "MD5" ).digest( p_downloadurl.getBytes( "UTF-8" ) ) );
            final Path l_path = Objects.nonNull( System.getProperty("OSMPATH") ) && ( System.getProperty("OSMPATH").length() > 0 )
                    ? Paths.get( System.getProperty("user.home"), ".osmstorage", l_graphlocation )
                    : Paths.get( System.getProperty("OSMPATH"), l_graphlocation );

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
