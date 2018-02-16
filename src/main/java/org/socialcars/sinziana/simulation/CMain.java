package org.socialcars.sinziana.simulation;

import org.jxmapviewer.viewer.GeoPosition;
import org.socialcars.sinziana.simulation.environment.osm.COSMEnvironment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * main application
 */
public final class CMain
{
    /**
     * ctor
     */
    private CMain()
    {}

    /**
     * main funtcion
     * @param p_args cli arguments
     * @throws IOException file
     */
    public static void main( final String[] p_args ) throws IOException
    {

        final COSMEnvironment l_env = new COSMEnvironment( "src/test/resources/netherlands-latest.osm.pbf",  52.378023,  52.358227,  4.926724,  4.874718 );

        final List<List<GeoPosition>> l_routes = new ArrayList<>();
        IntStream.range( 0, 10000 )
            .boxed()
            .forEach( i -> l_routes.add( l_env.route( l_env.randomnode(), l_env.randomnode(), Stream.empty() ) ) );

        l_env.drawHeat( l_routes );

    }
}
