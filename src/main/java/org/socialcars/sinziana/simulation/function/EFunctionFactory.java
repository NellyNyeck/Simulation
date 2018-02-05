package org.socialcars.sinziana.simulation.function;

import org.socialcars.sinziana.simulation.data.input.CFunctionpojo;
import org.socialcars.sinziana.simulation.data.input.CParameterpojo;

import java.util.Collection;
import java.util.Locale;


/**
 * function factory
 */
public enum EFunctionFactory implements IFunctionFactory
{
    EVEN;

    @Override
    public final IFunction apply( final Collection<CParameterpojo> p_parameter )
    {
        switch ( this )
        {
            case EVEN:
                return new CEven( p_parameter );

            default:
                //throw new RuntimeException( MessageFormat.format( "no function factory found for type [{0}]", this ) );
                return IFunction.EMPTY;
        }
    }

    /**
     * factory
     *
     * @param p_parameter parameter projo
     * @return function instance
     */
    public static IFunction of( final CFunctionpojo p_parameter )
    {
        return EFunctionFactory.valueOf( p_parameter.getName().toUpperCase( Locale.ROOT ) ).apply( p_parameter.getParameters() );
    }

}
