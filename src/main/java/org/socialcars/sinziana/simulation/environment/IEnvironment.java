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

    void initSet( IElement p_el );
}
