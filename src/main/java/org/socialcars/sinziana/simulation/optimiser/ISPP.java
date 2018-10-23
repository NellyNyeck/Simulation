package org.socialcars.sinziana.simulation.optimiser;

import gurobi.GRBException;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;

public interface ISPP
{
    void solve( final Integer p_origin, final Integer p_destination, final CJungEnvironment p_network ) throws GRBException;

    void display( final CJungEnvironment p_env ) throws GRBException;

    Integer length();

    Double cost();

}
