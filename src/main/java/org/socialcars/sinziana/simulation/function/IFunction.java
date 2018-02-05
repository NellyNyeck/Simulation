package org.socialcars.sinziana.simulation.function;

/**
 * the function inferface
 */
public interface IFunction
{
    IFunction EMPTY = new IFunction()
    {
        @Override
        public Number apply( final Number a, final Number b, final Number c )
        {
            return 0;
        }

        @Override
        public Number apply( final Number p_sigma, final Number p_mu )
        {
            return 0;
        }

        @Override
        public Number apply( final Number p_distance )
        {
            return 0;
        }
    };


    Number apply( Number a, Number b, Number c );

    Number apply( Number p_sigma, Number p_mu );

    Number apply( Number p_distance );

}
