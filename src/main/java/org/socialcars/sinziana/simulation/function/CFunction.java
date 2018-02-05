package org.socialcars.sinziana.simulation.function;

import org.socialcars.sinziana.simulation.data.input.CFunctionpojo;

/**
 * the function class
 */
public class CFunction implements IFunction<Number>
{

    /**
     * creates a function corresponding with the pojo
     * @param p_function function pojo
     * @return a function instance
     */
    public IFunction<Number> createFunction( final CFunctionpojo p_function )
    {
        final String l_name = p_function.getName();
        switch ( l_name.toLowerCase() )
        {
            case "even":
                final CEven l_even = new CEven( p_function.getParameters() );
                return l_even;
            default:
                break;

        }
        return null;
    }

    @Override
    public Number apply( final Number a, final Number b, final Number c )
    {
        return null;
    }

    @Override
    public Number apply( final Number p_sigma, final Number p_mu )
    {
        return null;
    }

    @Override
    public Number apply( final Number p_distance )
    {
        return null;
    }
}
