package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;
import java.util.stream.Stream;

/**
 * the environment interface
 */
public interface IEnvironment
{

    List<IEdge> route( INode p_start, INode p_finish, INode... p_middle );

    List<IEdge> route( INode p_start, INode p_finish, Stream<INode> p_middle );

    /**
     * initialize element
     * @param p_element input elements
     * @todo methode geändert, bitte nachvollziehen, warum das so wesentlich sinnvoller ist
     * @todo beachte ebenso, dass die DOkumentation in das Interface gehört und nicht in die implementierte Klasse
     */
    IEnvironment initialize( final IElement... p_element );
}
