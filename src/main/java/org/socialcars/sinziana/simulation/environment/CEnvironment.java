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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * the environment class
 */
public class CEnvironment implements IEnvironment
{
    private final Graph<INode, IEdge> m_graph = new DirectedSparseGraph<>();
    private final DijkstraShortestPath<INode, IEdge> m_pathalgorithm = new DijkstraShortestPath<>( m_graph );

    /**
     * construnctor
     * @param p_gr the graph pojo given
     */
    public CEnvironment( final CGraphpojo p_gr )
    {
        final Map<String, INode> l_nodes = p_gr.getNodes()
            .stream()
            .map( CNode::new )
            .peek( m_graph::addVertex )
            .collect( Collectors.toMap( CNode::id, i -> i ) );
        p_gr.getEdges().forEach( e ->
        {
            m_graph.addEdge( new CEdge( e, l_nodes.get( e.getFrom() ), l_nodes.get( e.getTo() ) ), l_nodes.get( e.getFrom() ), l_nodes.get( e.getTo() ) );
        } );
    }


    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final INode... p_middle )
    {
        return this.route( p_start, p_finish, Objects.isNull( p_middle ) ? Stream.empty() : Arrays.stream( p_middle ) );
    }

    @Override
    public List<IEdge> route( final INode p_start, final INode p_finish, final Stream<INode> p_middle )
    {
        return StreamUtils.windowed(
            Stream.concat(
                Stream.concat(
                    Stream.of( p_start ),
                    p_middle
                ),
                Stream.of( p_finish )
            ),
        2
        ).flatMap(
            i -> m_pathalgorithm.getPath( i.get( 0 ), i.get( 1 ) ).stream()
        ).collect( Collectors.toList() );
    }

    @Override
    public IEnvironment initialize( final IElement... p_element )
    {
        return this;
    }

}
