package org.socialcars.sinziana.simulation.function;

import org.socialcars.sinziana.simulation.data.input.CParameterpojo;

import java.util.Set;

/**
 * the even distribution function
 */
public class CEven implements IFunction<Number>
{
    private final String m_name = "even";

    private Number m_parameter;

    public CEven( final Set<CParameterpojo> p_parameters )
    {
        p_parameters.forEach( p -> m_parameter = p.getValue() );
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
