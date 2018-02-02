package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;

/**
 * the environment interface
 */
public interface IEnvironment
{

    List<IEdge> findBestRoute( INode p_start, INode p_finish,  List<INode> p_middle );

    void initSet( IElement p_el );
}
