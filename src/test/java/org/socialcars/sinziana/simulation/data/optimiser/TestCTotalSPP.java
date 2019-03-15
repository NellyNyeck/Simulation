package org.socialcars.sinziana.simulation.data.optimiser;

import com.fasterxml.jackson.databind.ObjectMapper;
import gurobi.GRBException;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CInputpojo;
import org.socialcars.sinziana.simulation.data.input.CPodpojo;
import org.socialcars.sinziana.simulation.elements.CPod;
import org.socialcars.sinziana.simulation.environment.jung.CJungEnvironment;
import org.socialcars.sinziana.simulation.environment.jung.IEdge;
import org.socialcars.sinziana.simulation.optimiser.CPodsSPP;
import org.socialcars.sinziana.simulation.units.CUnits;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * testing the optimiser class to bring everything together
 */
public class TestCTotalSPP
{
    private static final CInputpojo INPUT;
    private CJungEnvironment m_env;
    private CPodsSPP m_opt;
    private ArrayList<CPod> m_pods;
    private CUnits m_unit;
    private Integer m_time;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/25-5x5Total.json" ), CInputpojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     */
    @Before
    public void init()
    {
        m_env = new CJungEnvironment( INPUT.getGraph() );
        m_pods = new ArrayList<>();
        final Set<CPodpojo> l_pods = new HashSet<>();
        INPUT.getProviders().forEach( p ->
        {
            final Set<CPodpojo> l_po = p.getPods();
            l_po.forEach( d -> l_pods.add( d ) );
        } );
        l_pods.forEach( p -> m_pods.add( new CPod( p, 0 ) ) );
        m_unit = new CUnits( 1, 10 );
        m_time = 0;
    }

    /**
     * testing the optimiser
     * @param p_pods pods
     * @param p_origin origin node
     * @throws GRBException gurobi
     */
    @Test
    public void runOptimiser( final ArrayList<CPod> p_pods, final Integer p_origin, final Integer p_time ) throws GRBException
    {
        m_opt = new CPodsSPP( m_env, p_origin, m_pods, p_time );
        m_opt.solve();
        m_opt.display();
        m_opt.getCosts();
    }

    /**
     * testing pod movement
     */
    @Test
    public void movement()
    {
        final HashMap<CPod, List<IEdge>> l_routes = new HashMap<>();
        m_pods.forEach( p -> l_routes.put( p, m_env.route( p.origin(), p.destination() ) ) );
        final HashMap<CPod, String> l_status = new HashMap<>();
        m_pods.forEach( p -> l_status.put( p, "Incomplete" ) );
        //TODO: 2019-03-15 break this down, is A MESS 
        while ( l_status.containsValue( "Incomplete" ) )
        {
            IntStream.range( 0, m_pods.size() ).boxed().forEach( i ->
            {
                if ( ( m_pods.get( i ).position() == 0 ) & ( !m_pods.get( i ).location().contentEquals( m_pods.get( i ).destination() ) ) )
                {
                    final HashSet<CPod> l_platoon = new HashSet<>();
                    l_platoon.add( m_pods.get( i ) );
                    IntStream.range( i + 1, m_pods.size() ).boxed().forEach( j ->
                    {
                        if  ( ( m_pods.get( j ).position() == 0 )
                                & ( m_pods.get( j ).location().contentEquals( m_pods.get( i ).location() ) )
                                & ( !m_pods.get( j ).destination().contentEquals( m_pods.get( j ).location() ) ) ) l_platoon.add( m_pods.get( j ) );
                    } );
                    if ( l_platoon.size() > 1 )
                    {
                        final ArrayList<CPod> l_pl = new ArrayList<CPod>( l_platoon );
                        try
                        {
                            runOptimiser( l_pl, Integer.valueOf( m_pods.get( i ).location() ), m_time );
                            final HashMap<CPod, HashSet<IEdge>> l_platroutes = m_opt.getRoutes();
                            l_platroutes.keySet().forEach( k -> l_routes.replace( k, new ArrayList<>( l_platroutes.get( k ) ) ) );
                            l_pl.forEach( p ->
                            {
                                final ArrayList<CPod> l_lp = new ArrayList<>();
                                l_pl.forEach( meh -> l_lp.add( meh ) );
                                l_lp.remove( p );
                                p.formed( p.location(), m_time, l_lp );
                            } );
                        }
                        catch ( final GRBException l_err )
                        {
                            l_err.printStackTrace();
                        }
                    }
                }
                /*else if ( m_pods.get( i ).location().contentEquals( m_pods.get( i ).destination() ) )
                {
                    final Collection<IEvent> l_ev = m_pods.get( i ).events();
                    final Boolean[] l_bool = {false};
                    l_ev.forEach( e ->
                    {
                        if ( e.what().equals( EEvenType.FORMED ) ) l_bool[0] = true;
                    });
                    if ( l_bool[0] ) m_pods.get( i ).split( m_pods.get( i ).location(), m_time );
                } todo event management */
            } );

            m_pods.forEach( p ->
            {
                if ( l_routes.get( p ).isEmpty() ) l_status.put( p, "Complete" );
                else
                {
                    final IEdge l_edge = l_routes.get( p ).iterator().next();
                    if ( p.position().equals( 0.0 ) ) p.departed( l_edge, m_time );
                    if ( p.position() < l_edge.length() ) p.move( m_unit );
                    else
                    {
                        p.arrived( l_edge, m_time );
                        l_routes.get( p ).remove( 0 );
                    }
                }
            } );
            m_time++;
        }
    }

    /**
     * main
     * @param p_args cli
     * @throws GRBException gurobi
     */
    public static void main( final String[] p_args ) throws GRBException
    {
        final TestCTotalSPP l_test = new TestCTotalSPP();
        l_test.init();
        //l_test.runOptimiser( l_test.m_pods, 0, l_test.m_time );
        l_test.movement();
    }
}
