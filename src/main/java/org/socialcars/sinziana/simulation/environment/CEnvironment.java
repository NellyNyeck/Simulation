package org.socialcars.sinziana.simulation.environment;

import com.codepoetics.protonpack.StreamUtils;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import org.socialcars.sinziana.simulation.data.input.CGraphpojo;
import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * the environment class
 */
public class CEnvironment implements IEnvironment
{
    private final Graph<INode, IEdge> m_graph = new DirectedSparseGraph<>();
    private final DijkstraShortestPath<INode, IEdge> m_pathalgorithm = new DijkstraShortestPath<>( m_graph );
    private final Map<String, INode> m_nodes;
    private final Random m_random = new Random();

    /**
     * construnctor
     * @param p_gr the graph pojo given
     */
    public CEnvironment( final CGraphpojo p_gr )
    {
        m_nodes = p_gr.getNodes()
            .stream()
            .map( CNode::new )
            .peek( m_graph::addVertex )
            .collect( Collectors.toMap( CNode::id, i -> i ) );
        p_gr.getEdges()
            .forEach( e -> m_graph.addEdge(
                new CEdge(
                    e,
                    m_nodes.get( e.getFrom() ),
                    m_nodes.get( e.getTo() )
                ),
                m_nodes.get( e.getFrom() ),
                m_nodes.get( e.getTo() ) )
            );
    }


    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final INode... p_via )
    {
        return this.route( p_start, p_finish, Objects.isNull( p_via ) ? Stream.empty() : Arrays.stream( p_via ) );
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final Stream<INode> p_via )
    {
        return StreamUtils.windowed(
            Stream.concat(
                Stream.concat(
                    Stream.of( p_start ),
                    p_via
                ),
                Stream.of( p_finish )
            ),
        2
        ).flatMap(
            i -> m_pathalgorithm.getPath( i.get( 0 ), i.get( 1 ) ).stream()
        ).collect( Collectors.toList() );
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final String... p_via )
    {
        return this.route( p_start, p_end, Objects.isNull( p_via ) ? Stream.empty() : Arrays.stream( p_via ) );
    }

    @Override
    public List<IEdge> route( final String p_start, final String p_end, final Stream<String> p_via )
    {
        return this.route( m_nodes.get(  p_start ), m_nodes.get( p_end ), p_via.map( m_nodes::get ) );
    }

    @Override
    public String randomnode()
    {
        final String[] l_keys = m_nodes.keySet().toArray( new String[0] );
        return l_keys[m_random.nextInt( l_keys.length )];
    }

    @Override
    public IEnvironment initialize( final IElement... p_element )
    {
        return this;
    }

    @Override
    public String toString()
    {
        return m_graph.toString();
    }

    public Graph<INode, IEdge> getGraph()
    {
        return m_graph;
    }
}
