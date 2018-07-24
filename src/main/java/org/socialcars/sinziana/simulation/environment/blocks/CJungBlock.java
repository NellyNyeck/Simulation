package org.socialcars.sinziana.simulation.environment.blocks;

import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.environment.jung.INode;

import java.util.Collection;
import java.util.HashMap;
import java.util.stream.IntStream;

/**
 * lock environment class
 */
public class CJungBlock implements IBlockEnv
{
    private final Double m_blocksize;
    private HashMap<String, CBlock> m_intersections;
    private HashMap<String, CBlock> m_streets;

    private final CJungEnvironment m_env;

    /**
     * ctor
     * @param p_blocksize the blcoksize
     */
    public CJungBlock( final CJungEnvironment p_env, final Double p_blocksize )
    {
        m_env = p_env;
        m_blocksize = p_blocksize;
        m_intersections = new HashMap<>();
        m_streets = new HashMap<>();
    }

    @Override
    public Number getBlockSize()
    {
        return m_blocksize;
    }

    /**
     * maps the jung environemnt to blocks
     */
    public void map()
    {
        createJungNodes( m_env.nodes() );
        createJungEdges( m_env.edges() );
    }

    private void createJungEdges( final Collection<IEdge> p_edges )
    {
        p_edges.forEach( e ->
        {
            switch ( e.orientation() )
            {
                case "right" :
                    addBlocksright( e );
                    break;
                case "left" :
                    addBlocksleft( e );
                    break;
                case "up":
                    addBlocksup( e );
                    break;
                case "down" :
                    addBlocksdown( e );
                    break;
                default:
                    break;
            }
        } );
    }

    private void addBlocksdown( final IEdge p_edg )
    {
        final String l_ul = "ul";
        final String l_dl = "dl";
        IntStream.range( 1,  p_edg.length() )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + i, p_edg.from().coordinate().latitude(), p_edg.from().coordinate().longitude() - i );
                m_streets.put( l_new.id(), l_new );
                if ( i == 1 )
                {
                    l_new.addUp( m_intersections.get( p_edg.from().id() + l_dl ) );
                    m_intersections.get( p_edg.from().id() + l_dl ).addDown( l_new );
                }
                else if ( i ==  Integer.valueOf( String.valueOf( p_edg.length() ) ) - 1 )
                {
                    l_new.addUp( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    l_new.addDown( m_intersections.get( p_edg.to().id() + l_ul ) );
                    m_intersections.get( p_edg.to().id() + l_ul ).addUp( l_new );
                }
                else
                {
                    l_new.addUp( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ).addDown( l_new );
                }

            } );

    }

    private void addBlocksup( final IEdge p_edg )
    {
        final String l_ur = "ur";
        final String l_dr = "dr";
        IntStream.range( 1, p_edg.length() )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + i, p_edg.from().coordinate().latitude(), p_edg.from().coordinate().longitude() + i );
                m_streets.put( l_new.id(), l_new );
                if ( i == 1 )
                {
                    l_new.addDown( m_intersections.get( p_edg.from().id() + l_ur ) );
                    m_intersections.get( p_edg.from().id() + l_ur ).addUp( l_new );
                }
                else if ( i ==  Integer.valueOf( String.valueOf( p_edg.length() ) ) - 1 )
                {
                    l_new.addDown( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    l_new.addUp( m_intersections.get( p_edg.to().id() + l_dr ) );
                    m_intersections.get( p_edg.to().id() + l_dr ).addDown( l_new );
                }
                else
                {
                    l_new.addDown( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ).addUp( l_new );
                }

            } );

    }

    private void addBlocksleft( final IEdge p_edg )
    {
        final String l_dl = "dl";
        final String l_dr = "dr";
        IntStream.range( 1, p_edg.length() )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + i, p_edg.from().coordinate().latitude() - i, p_edg.from().coordinate().longitude() );
                m_streets.put( l_new.id(), l_new );
                if ( i == 1 )
                {
                    l_new.addRight( m_intersections.get( p_edg.from().id() + l_dl ) );
                    m_intersections.get( p_edg.from().id() + l_dl ).addLeft( l_new );
                }
                else if ( i ==  Integer.valueOf( String.valueOf( p_edg.length() ) ) - 1 )
                {
                    l_new.addRight( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    l_new.addLeft( m_intersections.get( p_edg.to().id() + l_dr ) );
                    m_intersections.get( p_edg.to().id() + l_dr ).addRight( l_new );
                }
                else
                {
                    l_new.addRight( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ).addLeft( l_new );
                }
            } );

    }

    private void addBlocksright( final IEdge p_edg )
    {
        final String l_dr = "dr";
        final String l_dl = "dl";
        IntStream.range( 1, p_edg.length()  )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + String.valueOf( i ), p_edg.from().coordinate().latitude() + i, p_edg.from().coordinate().longitude() );
                m_streets.put( l_new.id(), l_new );
                if ( i == 1 )
                {
                    l_new.addLeft( m_intersections.get( p_edg.from().id() + l_dr ) );
                    m_intersections.get( p_edg.from().id() + l_dr ).addRight( l_new );
                }
                else if ( i ==  Integer.valueOf( String.valueOf( p_edg.length() ) ) - 1 )
                {
                    l_new.addLeft( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    l_new.addRight( m_intersections.get( p_edg.to().id() + l_dl ) );
                    m_intersections.get( p_edg.to().id() + l_dl ).addLeft( l_new );
                }
                else
                {
                    l_new.addLeft( m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ) );
                    m_streets.get( p_edg.id() + String.valueOf( i - 1 ) ).addRight( l_new );
                }
            } );
    }

    private void createJungNodes( final Collection<INode> p_nodes )
    {
        p_nodes.forEach( n ->
        {
            final CBlock l_bl1 = new CBlock( n.id() + "dr", n.coordinate().latitude(), n.coordinate().longitude() );
            final CBlock l_bl2 = new CBlock( n.id() + "dl", n.coordinate().latitude(), n.coordinate().longitude() );
            final CBlock l_bl3 = new CBlock( n.id() + "ul", n.coordinate().latitude(), n.coordinate().longitude() );
            final CBlock l_bl4 = new CBlock( n.id() + "ur", n.coordinate().latitude(), n.coordinate().longitude() );
            m_intersections.put( l_bl1.id(), l_bl1 );
            m_intersections.put( l_bl2.id(), l_bl2 );
            m_intersections.put( l_bl3.id(), l_bl3 );
            m_intersections.put( l_bl4.id(), l_bl4 );
            addNodeConnections( l_bl1, l_bl2, l_bl3, l_bl4 );
        } );
    }

    private void addNodeConnections( final CBlock p_dr, final CBlock p_dl, final CBlock p_ul, final CBlock p_ur )
    {
        p_dr.addUp( p_ur );
        p_dr.addLeft( p_dl );
        p_dl.addUp( p_ul );
        p_dl.addRight( p_dr );
        p_ul.addRight( p_ur );
        p_ul.addDown( p_dl );
        p_ur.addLeft( p_ul );
        p_ur.addDown( p_dr );
    }

    /**
     * shows the sizes of the network, for testing purposes
     */
    public void returnsizes()
    {
        System.out.println( m_intersections.size() );
        System.out.println( m_streets.size() );
    }

}
