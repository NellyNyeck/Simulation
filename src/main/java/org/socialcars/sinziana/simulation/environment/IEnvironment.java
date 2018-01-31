package org.socialcars.sinziana.simulation.environment;

import org.socialcars.sinziana.simulation.elements.IElement;

import java.util.List;

/**
 * the environment interface
 * @param <I> generic for node identifier
 * @param <V> generic for node
 * @param <E> generic for edge
 */
public interface IEnvironment<I, V extends IIntersection<I>, E extends IStreet>
{

    List<E> findBestRoute( V p_start, V p_finish,  List<V> p_middle );

    void initSet( IElement p_el );
}
