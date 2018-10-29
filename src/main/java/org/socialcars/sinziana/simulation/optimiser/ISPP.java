package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRBException;

/**
 * interface for optimisation problems
 */
public interface ISPP
{
    /**
     * solve function
     * @throws GRBException gurobi
     */
    void solve() throws GRBException;

    /**
     * displays the result
     * @throws GRBException gurobi
     */
    void display() throws GRBException;

    /**
     * the length of the route
     * @return length
     */
    Integer length();

    /**
     * cost of the route
     * @return cost
     */
    Double cost();

}
