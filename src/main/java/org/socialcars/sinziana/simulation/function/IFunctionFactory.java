package org.socialcars.sinziana.simulation.function;

import org.socialcars.sinziana.simulation.data.input.CParameterpojo;

import java.util.Collection;
import java.util.function.Function;


/**
 * function factory
 */
public interface IFunctionFactory extends Function<Collection<CParameterpojo>, IFunction>
{
}
