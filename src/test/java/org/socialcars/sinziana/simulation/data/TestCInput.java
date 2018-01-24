package org.socialcars.sinziana.simulation.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.socialcars.sinziana.simulation.data.Input.CInput;

import java.io.File;
import java.io.IOException;


/**
 * unit-test for data input
 */
public final class TestCInput
{
    /**
     * read input json
     *
     * @throws IOException throw io exception
     */
    @Test
    public void read() throws IOException
    {
        final CInput l_data = new ObjectMapper().readValue( new File( "src/test/resources/example_input.json" ), CInput.class );
    }

}
