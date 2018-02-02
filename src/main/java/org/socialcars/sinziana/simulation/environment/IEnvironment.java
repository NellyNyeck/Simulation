package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;

/**
 * the environment interface
 */
public interface IEnvironment
{

    List<? extends IEdge> findBestRoute( INode p_start, INode p_finish,  List<? extends INode> p_middle );

    void initSet( IElement p_el );
}
