package org.socialcars.sinziana.simulation.elements;

import org.socialcars.sinziana.simulation.environment.CIntersection;

import java.util.List;

/**
 * the provider interface
 */
public interface IProvider extends IStatic
{
    String colour();

    List<CIntersection> depots();

    List<CPOD> pods();

    Number maxClients();

    Number maxPodsTime();

    //TODO: 01.02.18 function class and obvs parameter need to be written but WHERE????
}
