package org.socialcars.sinziana.simulation.function;

import org.socialcars.sinziana.simulation.data.input.CParameterpojo;

import java.util.Collection;

/**
 * the even distribution function
 */
public class CEven implements IFunction
{
    private Number m_parameter;

    /**
     * ctor
     *
     * @param p_parameters parameter
     */
    public CEven( final Collection<CParameterpojo> p_parameters )
    {
        m_parameter = p_parameters.stream()
                                  .findFirst()
                                  .orElseThrow( () -> new RuntimeException( "no parameter found" ) )
                                  .getValue();
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
        return m_parameter;
    }
}
