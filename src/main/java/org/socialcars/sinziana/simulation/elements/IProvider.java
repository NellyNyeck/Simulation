package org.socialcars.sinziana.simulation.elements;


import org.socialcars.sinziana.simulation.environment.INode;

import java.util.Collection;

/**
 * the provider interface
 */
public interface IProvider extends IStatic
{
    String colour();

    Collection<? extends INode> depots();

    Collection<? extends IPod> pods();

    Number maxClients();

    Number maxPodsTime();

    //TODO: 01.02.18 function class and obvs parameter need to be written but WHERE????
}
