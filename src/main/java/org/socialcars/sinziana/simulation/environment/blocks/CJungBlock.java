package org.socialcars.sinziana.simulation.environment.blocks;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import org.socialcars.sinziana.simulation.elements.IMovable;
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
    private static final String CONNECTOR = "-";
    private static final String SEPARATOR = ".";

    private final Double m_blocksize;
    private HashMap<String, CBlock> m_intersections;
    private HashMap<String, CBlock> m_streets;

    private final DirectedGraph<IBlock, String> m_graph;



    /**
     * ctor
     * @param p_blocksize the blcoksize
     */
    public CJungBlock( final CJungEnvironment p_env, final Double p_blocksize )
    {
        m_blocksize = p_blocksize;
        m_intersections = new HashMap<>();
        m_streets = new HashMap<>();
        m_graph = new DirectedSparseMultigraph<>();
        map( p_env );
    }

    @Override
    public Number getBlockSize()
    {
        return m_blocksize;
    }

    @Override
    public void move( final IMovable p_agent )
    {

    }

    /**
     * maps the jung environemnt to blocks
     */
    private void map( final CJungEnvironment p_env )
    {
        createJungNodes( p_env.nodes() );
        createJungEdges( p_env.edges() );
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
        IntStream.range( 1, (int) ( p_edg.length() / m_blocksize ) )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + SEPARATOR + i, p_edg.from().coordinate().latitude(), p_edg.from().coordinate().longitude() - m_blocksize );
                m_streets.put( l_new.id(), l_new );
                m_graph.addVertex( l_new );
                if ( i == 1 )
                {
                    m_graph.addEdge( l_new.id(), m_intersections.get( p_edg.from().id() + l_dl ), l_new );
                }
                else if ( i ==  p_edg.length() - 1 )
                {
                    m_graph.addEdge( l_new.id(), l_new, m_intersections.get( p_edg.to().id() + l_ul ) );
                }
                else
                {
                    m_graph.addEdge( l_new.id(), m_streets.get( p_edg.id() + SEPARATOR + String.valueOf( i - 1 ) ), l_new );
                }

            } );

    }

    private void addBlocksup( final IEdge p_edg )
    {
        final String l_ur = "ur";
        final String l_dr = "dr";
        IntStream.range( 1, (int) ( p_edg.length() / m_blocksize ) )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + SEPARATOR + i, p_edg.from().coordinate().latitude(), p_edg.from().coordinate().longitude() + m_blocksize );
                m_streets.put( l_new.id(), l_new );
                m_graph.addVertex( l_new );
                if ( i == 1 )
                {
                    m_graph.addEdge( l_new.id(), m_intersections.get( p_edg.from().id() + l_ur ), l_new );
                }
                else if ( i ==   p_edg.length() - 1 )
                {
                    m_graph.addEdge( l_new.id(), l_new, m_intersections.get( p_edg.to() + l_dr ) );
                }
                else
                {
                    m_graph.addEdge( l_new.id(), m_streets.get( p_edg.id() + SEPARATOR + String.valueOf( i - 1 ) ), l_new );
                }

            } );

    }

    private void addBlocksleft( final IEdge p_edg )
    {
        final String l_dl = "dl";
        final String l_dr = "dr";
        IntStream.range( 1, (int) ( p_edg.length() / m_blocksize ) )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + SEPARATOR + i, p_edg.from().coordinate().latitude() - m_blocksize, p_edg.from().coordinate().longitude() );
                m_streets.put( l_new.id(), l_new );
                m_graph.addVertex( l_new );
                if ( i == 1 )
                {
                    m_graph.addEdge( l_new.id(), m_intersections.get( p_edg.from().id() + l_dl ), l_new );
                }
                else if ( i ==   p_edg.length() - 1 )
                {
                    m_graph.addEdge( l_new.id(), l_new, m_intersections.get( p_edg.to() + l_dr ) );
                }
                else
                {
                    m_graph.addEdge( l_new.id(), m_streets.get( p_edg.id() + SEPARATOR + String.valueOf( i - 1 ) ), l_new );
                }
            } );

    }

    private void addBlocksright( final IEdge p_edg )
    {
        final String l_dr = "dr";
        final String l_dl = "dl";
        IntStream.range( 1, (int) ( p_edg.length() / m_blocksize ) )
            .boxed()
            .forEach( i ->
            {
                final CBlock l_new = new CBlock( p_edg.id() + SEPARATOR + i, p_edg.from().coordinate().latitude() + m_blocksize, p_edg.from().coordinate().longitude() );
                m_streets.put( l_new.id(), l_new );
                m_graph.addVertex( l_new );
                if ( i == 1 )
                {
                    m_graph.addEdge( l_new.id(), m_intersections.get( p_edg.from().id() + l_dr ), l_new );
                }
                else if ( i ==  p_edg.length() - 1 )
                {
                    m_graph.addEdge( l_new.id(), l_new, m_intersections.get( p_edg.to() + l_dl ) );
                }
                else
                {
                    m_graph.addEdge( l_new.id(), m_streets.get( p_edg.id() + SEPARATOR + String.valueOf( i - 1 ) ), l_new );
                }
            } );
    }

    private void createJungNodes( final Collection<INode> p_nodes )
    {
        p_nodes.forEach( n ->
        {
            final CBlock l_bl1 = new CBlock( n.id() + "dr", n.coordinate().latitude(), n.coordinate().longitude() );
            m_intersections.put( l_bl1.id(), l_bl1 );
            m_graph.addVertex( l_bl1 );
            final CBlock l_bl2 = new CBlock( n.id() + "dl", n.coordinate().latitude(), n.coordinate().longitude() );
            m_intersections.put( l_bl2.id(), l_bl2 );
            m_graph.addVertex( l_bl2 );
            final CBlock l_bl3 = new CBlock( n.id() + "ul", n.coordinate().latitude(), n.coordinate().longitude() );
            m_intersections.put( l_bl3.id(), l_bl3 );
            m_graph.addVertex( l_bl3 );
            final CBlock l_bl4 = new CBlock( n.id() + "ur", n.coordinate().latitude(), n.coordinate().longitude() );
            m_intersections.put( l_bl4.id(), l_bl4 );
            m_graph.addVertex( l_bl4 );
            addNodeConnections( l_bl1, l_bl2, l_bl3, l_bl4 );
        } );
    }

    private void addNodeConnections( final CBlock p_dr, final CBlock p_dl, final CBlock p_ul, final CBlock p_ur )
    {
        m_graph.addEdge( p_dr.id() + CONNECTOR + p_dl.id(), p_dr, p_dl );
        m_graph.addEdge( p_dr.id() + CONNECTOR + p_ur.id(), p_dr, p_ur );
        m_graph.addEdge( p_dl.id() + CONNECTOR + p_dr.id(), p_dl, p_dr );
        m_graph.addEdge( p_dl.id() + CONNECTOR + p_ul.id(), p_dl, p_ul );
        m_graph.addEdge( p_ur.id() + CONNECTOR + p_dr.id(), p_ur, p_dr );
        m_graph.addEdge( p_ur.id() + CONNECTOR + p_ul.id(), p_ur, p_ul );
        m_graph.addEdge( p_ul.id() + CONNECTOR + p_dl.id(), p_ul, p_dl );
        m_graph.addEdge( p_ul.id() + CONNECTOR + p_ur.id(), p_ul, p_ur );
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
