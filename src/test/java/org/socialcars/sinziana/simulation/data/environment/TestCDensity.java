package org.socialcars.sinziana.simulation.data.environment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.input.CDensitiespojo;
import org.socialcars.sinziana.simulation.environment.demand.CDensity;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * density testing class
 */
public final class TestCDensity
{
    private static final CDensitiespojo INPUT;
    private CDensity m_densities;

    static
    {
        try
        {
            INPUT = new ObjectMapper().readValue( new File( "src/test/resources/Density20.json" ), CDensitiespojo.class );
        }
        catch ( final IOException l_exception )
        {
            throw new UncheckedIOException( l_exception );
        }
    }

    /**
     * initializing
     * @throws IOException file
     */
    @Before
    public void init() throws IOException
    {
        m_densities = new CDensity( 20.0, INPUT.getDensity() );
    }

    @Test
    public void check()
    {
        m_densities.getAll();
    }
}
