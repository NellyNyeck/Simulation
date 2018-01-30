/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++) Scratchpad                     #
 * # Copyright (c) 2017, LightJason (info@lightjason.org)                               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.socialcars.sinziana.simulation.runtime;

import javax.annotation.Nonnull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.function.Function;


/**
 * returns a runtime instance
 */
public enum ERuntime implements Function<Number, IRuntime>
{
    SYNCHRONIZED,
    WORKSTEALING,
    FIXEDSIZE,
    CACHED,
    SCHEDULED,
    SINGLE;

    //Checkstyle:OFF:ReturnCount
    @Override
    public final IRuntime apply( final Number p_number )
    {
        switch ( this )
        {
            case SYNCHRONIZED:
                return new CSynchronized( this, p_number.intValue() );

            case WORKSTEALING:
                return new CPool( this, p_number.intValue(), Executors.newWorkStealingPool() );

            case FIXEDSIZE:
                return new CPool( this, p_number.intValue(), Executors.newFixedThreadPool( p_number.intValue() ) );

            case CACHED:
                return new CPool( this, p_number.intValue(), Executors.newCachedThreadPool() );

            case SCHEDULED:
                return new CPool( this, p_number.intValue(), Executors.newScheduledThreadPool( p_number.intValue() ) );

            case SINGLE:
                return new CPool( this, p_number.intValue(), Executors.newSingleThreadExecutor() );

            default:
                throw new RuntimeException( MessageFormat.format( "unknown runtime definition [{0}]", this ) );
        }
    }
    //Checkstyle:ON:ReturnCount

    /**
     * returns runtime instance of string value
     *
     * @param p_name name
     * @return runtime instance
     */
    public static ERuntime from( @Nonnull final String p_name )
    {
        return ERuntime.valueOf( p_name.toUpperCase( Locale.ROOT ) );
    }

    @Override
    public final String toString()
    {
        return super.toString().toLowerCase( Locale.ROOT );
    }
}
