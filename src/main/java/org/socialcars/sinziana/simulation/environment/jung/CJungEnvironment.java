package org.socialcars.sinziana.simulation.environment.jung;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.base.Function;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.util.RandomLocationTransformer;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.socialcars.sinziana.simulation.data.input.CGraphpojo;
import org.socialcars.sinziana.simulation.elements.IElement;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * the environment class
 */
public class CJungEnvironment implements IEnvironment<VisualizationViewer<INode, IEdge>>
{
    private final Graph<INode, IEdge> m_graph;
    private final DijkstraShortestPath<INode, IEdge> m_pathalgorithm;
    private final Map<String, INode> m_nodes;
    private final INode[] m_nodelist;

    /**
     * construnctor
     * @param p_gr the graph pojo given
     */
    public CJungEnvironment( final CGraphpojo p_gr )
    {
        final DirectedGraph<INode, IEdge> l_graph = new DirectedSparseMultigraph<>();

        m_nodes = p_gr.getNodes()
            .stream()
            .map( CNode::new )
            .peek( l_graph::addVertex )
            .collect( Collectors.toMap( CNode::id, i -> i ) );

        p_gr.getEdges()
            .forEach( e -> l_graph.addEdge(
                new CEdge(
                    e,
                    m_nodes.get( e.getFrom() ),
                    m_nodes.get( e.getTo() )
                ),
                m_nodes.get( e.getFrom() ),
                m_nodes.get( e.getTo() ) )
            );

        m_nodelist = m_nodes.values().toArray( new INode[0] );
        m_graph = Graphs.unmodifiableGraph( l_graph );
        m_pathalgorithm = new DijkstraShortestPath<>( m_graph, IEdge::weight );
    }

    public Collection<IEdge> edges()
    {
        return m_graph.getEdges();
    }

    public int size()
    {
        return m_graph.getVertexCount();
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
    public String randomnodebyname()
    {
        return this.randomnode().id();
    }

    @Override
    public INode randomnode()
    {
        return m_nodelist[ ThreadLocalRandom.current().nextInt( m_nodelist.length ) ];
    }

    @Override
    public IEnvironment<?> initialize( final IElement... p_element )
    {
        return this;
    }

    @Override
    public VisualizationViewer<INode, IEdge> panel( final Dimension p_dimension )
    {
        final FRLayout<INode, IEdge> l_projection = new FRLayout<>( m_graph, p_dimension );
        l_projection.setInitializer( new RandomLocationTransformer<>( p_dimension, 1 ) );

        final Function<Object, String> l_labeling = new ToStringLabeller();
        final VisualizationViewer<INode, IEdge> l_view = new VisualizationViewer<>( l_projection );
        l_view.getRenderContext().setVertexLabelTransformer( l_labeling );
        l_view.getRenderContext().setEdgeLabelTransformer( l_labeling );

        return l_view;
    }

    @Override
    public String toString()
    {
        return m_graph.toString();
    }

}
