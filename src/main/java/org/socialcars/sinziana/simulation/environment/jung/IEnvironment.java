package org.socialcars.sinziana.simulation.environment.jung;

import org.socialcars.sinziana.simulation.elements.IElement;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * the environment interface
 */
public interface IEnvironment<V extends JPanel>
{
    /**
     * finding a route with more middle points
     * @param p_start begining
     * @param p_finish end
     * @param p_via middle goals
     * @return list of edges
     */
    List<IEdge> route( INode p_start, INode p_finish, INode... p_via );

    /**
     * same as above but fixed input
     * @param p_start begining
     * @param p_finish end
     * @param p_via middle
     * @return list of edges
     */
    List<IEdge> route( INode p_start, INode p_finish, Stream<INode> p_via );

    /**
     * finding a route with given strings, so node object needs to be found first
     * @param p_start start node
     * @param p_end end node
     * @param p_via middle destination points
     * @return the list of edges
     */
    List<IEdge> route( String p_start, String p_end, String... p_via );

    /**
     * finding a route with given strings, fixed input, node object needs to be found first
     * @param p_start start node
     * @param p_end end node
     * @param p_via middle destination points
     * @return the list of edges
     */
    List<IEdge> route( String p_start, String p_end, Stream<String> p_via );

    /**
     * returns the id of a random node
     * @return the name
     */
    String randomnodebyname();

    /**
     * retunrs a random node
     * @return node
     */
    INode randomnode();

    /**
     * returns the zones
     * @return zones
     */
    HashMap<String, List<INode>> getZones();

    /**
     * gets a random node by zone
     * @param p_zone the zone
     * @return the node
     */
    INode randomnodebyzone( final String p_zone );

    /**
     * calculates the edges length
     * @param p_edge edge
     * @return length
     */
    Number edgeLength( final IEdge p_edge);

     //@todo methode geändert, bitte nachvollziehen, warum das so wesentlich sinnvoller ist
    /**
     * initialize element
     * @param p_element input element
     * @return an element
     */
    IEnvironment<?> initialize( final IElement... p_element );

    /**
     * returns a panel with graph visualization
     *
     * @param p_dimension dimension
     * @return panel
     */
    V panel( final Dimension p_dimension );
}
