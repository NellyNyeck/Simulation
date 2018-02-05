package org.socialcars.sinziana.simulation.function;

/**
 * the function inferface
 */
public interface IFunction<T>
{
    T apply( T a, T b, T c );

    T apply( T p_sigma, T p_mu );

    T apply( T p_distance );

}
