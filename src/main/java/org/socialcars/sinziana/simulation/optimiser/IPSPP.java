package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRBException;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

import java.util.ArrayList;

public interface IPSPP
{
    void solve() throws GRBException;

    void display() throws GRBException;
}
