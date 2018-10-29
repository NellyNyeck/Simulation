package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRBException;

/**
 * interface for shortest path problems
 */
public interface IPSPP
{
    /**
     * solving the problem
     * @throws GRBException gurobi
     */
    void solve() throws GRBException;

    /**
     * displaying the results
     * @throws GRBException gurobi
     */
    void display() throws GRBException;
}
