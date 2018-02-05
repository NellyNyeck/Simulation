package org.socialcars.sinziana.simulation.environment;

import edu.uci.ics.jung.graph.Graph;
import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;
import java.util.stream.Stream;

/**
 * the environment interface
 */
public interface IEnvironment
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

    List<IEdge> route( String p_start, String p_end, String... p_via );

    List<IEdge> route( String p_start, String p_end, Stream<String> p_via );

    String randomnodebyname();

    INode randomnode();


     //@todo methode geändert, bitte nachvollziehen, warum das so wesentlich sinnvoller ist
     //@todo beachte ebenso, dass die DOkumentation in das Interface gehört und nicht in die implementierte Klasse
    /**
     * initialize element
     * @param p_element input element
     * @return an element
     */
    IEnvironment initialize( final IElement... p_element );

    Graph<INode, IEdge> graph();
}
