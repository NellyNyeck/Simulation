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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * the environment class
 */
public class CJungEnvironment implements IEnvironment<VisualizationViewer<INode, IEdge>>
{
    private static final Logger LOGGER = Logger.getLogger( CJungEnvironment.class.getName() );

    private final Graph<INode, IEdge> m_graph;
    private final DijkstraShortestPath<INode, IEdge> m_pathalgorithm;
    private final Map<String, INode> m_nodes;
    private final INode[] m_nodelist;
    private final HashMap<String, List<INode>> m_zones;


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

        m_zones = new HashMap<>();
        if ( p_gr.getZones() != 0 )
        {
            final AtomicInteger l_count = new AtomicInteger();
            l_count.set( 1 );
            final int l_npz = m_nodes.size() / p_gr.getZones();
            IntStream.range( 1, p_gr.getZones() + 1 ).boxed().forEach( i ->
            {
                final ArrayList<INode> l_mappy = new ArrayList<>();
                IntStream.range( l_count.get(), l_count.get() + l_npz ).boxed().forEach( j ->
                {
                    l_mappy.add( m_nodes.get( j.toString() ) );
                } );
                l_count.addAndGet( l_npz );
                m_zones.put( String.valueOf( i ), l_mappy );
            } );
            if ( l_count.intValue() < m_nodes.size() )
            {
                final List<INode> l_local = m_zones.get( String.valueOf( p_gr.getZones() ) );
                IntStream.range( l_count.intValue(), m_nodes.size() ).boxed().forEach( i -> l_local.add( m_nodes.get( i.toString() ) ) );
            }
        }
    }

    public Collection<IEdge> edges()
    {
        return m_graph.getEdges();
    }

    public Collection<INode> nodes()
    {
        return m_graph.getVertices();
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
    public HashMap<String, List<INode>> getZones()
    {
        return m_zones;
    }

    @Override
    public INode randomnodebyzone( final String p_zone )
    {
        return m_zones.get( p_zone ).get( ThreadLocalRandom.current().nextInt( m_zones.get( p_zone ).size() ) );
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

    private static class CVisitedStructure
    {
        private final String m_id;
        private Integer m_visited;

        CVisitedStructure( final String p_id, final Integer p_visited )
        {
            m_id = p_id;
            m_visited = p_visited;
        }

        private void add( final Integer p_new )
        {
            m_visited = m_visited + p_new;
        }

        private Map<String, Object> toMap()
        {
            final HashMap<String, Object> l_map = new HashMap<>();
            l_map.put( "id", m_id );
            l_map.put( "visited", m_visited );
            return l_map;
        }
    }

}
