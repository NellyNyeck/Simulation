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

import org.socialcars.sinziana.simulation.elements.IElement;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;


/**
 * asychronized execution
 */
public final class CPool extends IBaseRuntime
{
    /**
     * stealing pool
     */
    private final ExecutorService m_pool;
    /**
     * error exception
     */
    private final AtomicReference<Exception> m_error = new AtomicReference<>();

    /**
     * ctor
     *
     * @param p_type runtime type
     * @param p_value runtime value
     */
    public CPool( @Nonnull final ERuntime p_type, @Nonnegative final int p_value, @Nonnull final ExecutorService p_pool )
    {
        super( p_type, p_value );
        m_pool = p_pool;
    }

    @Override
    public final IRuntime apply( @Nonnull final Collection<IElement> p_agents )
    {
        final CountDownLatch l_countdown = new CountDownLatch( p_agents.size() );
        p_agents.parallelStream().forEach( i -> new CExecution( l_countdown, i ) );

        try
        {
            l_countdown.await();
        }
        catch ( final InterruptedException l_exception )
        {
            // empty
        }

        return this;
    }

    @Override
    public final void shutdown()
    {
        m_pool.shutdownNow();
    }

    /**
     * execution agent wrapper
     */
    private final class CExecution implements Runnable
    {
        /**
         * referenced agent
         */
        private final IElement m_agent;
        /**
         * count down latch
         */
        private final CountDownLatch m_countdown;

        /**
         * execution
         *
         * @param p_countdown for finializing
         * @param p_agent agent
         */
        CExecution( @Nonnull final CountDownLatch p_countdown, @Nonnull final IElement p_agent )
        {
            m_agent = p_agent;
            m_countdown = p_countdown;
            m_pool.submit( this );
        }

        @Override
        public final void run()
        {
            CPool.this.execute(
                m_agent,
                i ->
                {
                    m_error.set( i );
                    LongStream.range( 0, m_countdown.getCount() ).forEach( j -> m_countdown.countDown() );
                } );

            if ( m_agent.terminate() )
                m_countdown.countDown();
            else
                m_pool.submit( this );
        }
    }

}
