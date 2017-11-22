package exec;
/*
* @cond LICENSE
* ######################################################################################
* # LGPL License                                                                       #
* #                                                                                    #
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


import environment.CEdge;
import environment.CGraph;
import environment.CNode;
import environment.CPOI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import output.CSVWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * the main class
 */
public final class CMain
{
    private static final CGraph<?, CNode, CEdge> s_GR = new CGraph<>();
    private static Integer s_poi;
    //private static Integer s_pod;
    private static Integer s_runs;
    private static CSVWriter s_out;
    private static Double s_sepa;
    //private static int s_podcap;
    //private static String s_strateg;

    protected CMain()
    {

    }

    /**
     * initialization of the graph by reading the nodes and edges from file
     * @throws IOException because file
     * @throws JSONException because working with json objects
     */
    public static void graphInit( final String p_arg ) throws IOException, JSONException
    {
        final String l_text = new String( Files.readAllBytes( Paths.get(  p_arg ) ), StandardCharsets.UTF_8 );
        final JSONObject l_object;
        l_object = new JSONObject( l_text );
        s_sepa = l_object.getDouble( "POI_dist" );
        s_poi = l_object.getInt( "Nb_POI" );
            //s_pod = l_object.getInt( "Nb_POD" );
            //s_podcap = l_object.getInt( "POD_cap" );
        s_runs = l_object.getInt( "Runs" );
            //s_strateg = l_object.getString( "Strategy" );
        JSONArray l_array = l_object.getJSONObject( "Environment" ).getJSONArray( "Intersections" );
        for ( int l_in = 0; l_in < l_array.length(); l_in++ )
        {
            final CNode l_cnode = new CNode( l_array.getJSONObject( l_in ) );
            s_GR.addNode( l_cnode );
        }
        l_array = l_object.getJSONObject( "Environment" ).getJSONArray( "Streets" );
        for ( int l_in = 0; l_in < l_array.length(); l_in++ )
        {
            final JSONObject l_obj = l_array.getJSONObject( l_in );
            processStreet( l_obj );
        }
    }

    /**
     * process each given street to check if length < separation distance between the POI
     * @param p_object the JSON object which holds the street information
     * @throws JSONException working with json objects
     */
    public static void processStreet( final JSONObject p_object ) throws JSONException
    {
        final double l_le = p_object.getDouble( "length" );
        if ( l_le > s_sepa )
        {
            genPOI( p_object );
        }
        else
        {
            final CEdge l_cedge = new CEdge( p_object );
            final CNode l_from = s_GR.getNode( p_object.getDouble( "from" ) );
            final CNode l_to = s_GR.getNode( p_object.getDouble( "to" ) );
            s_GR.addEdge( l_from, l_to, l_cedge );
        }
    }

    /**
     * checks if the street function is vertical or horrizontal
     * @param p_object the individual street
     * @throws JSONException working with json object
     */
    public static void genPOI( final JSONObject p_object ) throws JSONException
    {
        if ( !checkExcp( p_object ) )
        {
            irregular( p_object );
        }

    }

    /**
     * if street is not regular, check the direction of the lane and calculate the linear function for the street
     * @param p_object the street given
     * @throws JSONException working with json object
     */
    private static void irregular( final JSONObject p_object ) throws JSONException
    {

        final CNode l_from = s_GR.getNode(  p_object.getDouble( "from" ) );
        final CNode l_to = s_GR.getNode( p_object.getDouble( "to" ) );
        final JSONObject l_funct = getab( s_GR.getNode( p_object.getDouble( "from" ) ), s_GR.getNode( p_object.getDouble( "to" ) ) );

        if ( ( l_from.xcoord() >= l_to.xcoord() ) || ( l_from.ycoord() >= l_to.ycoord() ) )
        {
            sprinklesRight( p_object, l_funct );
        }
        else
        {
            sprinklesLeft( p_object, l_funct );
        }


    }

    /**
     * if the function for the street is ascending, creates the POIs and the edges in between
     * @param p_street the street given
     * @param p_factor the function parameters for the street
     * @throws JSONException working with json object
     */
    private static void sprinklesRight( final JSONObject p_street, final JSONObject p_factor ) throws JSONException
    {

        final Double l_le = p_street.getDouble( "length" );
        int l_poi = (int) ( l_le % s_sepa );
        if  ( l_poi == 0 ) l_poi = (int) ( s_sepa - 1 );
        final Double l_pad = ( l_le - ( l_poi - 1 ) * s_sepa ) / 2;
        Double l_dist = l_pad;
        final Double l_weight = p_street.getDouble( "weight" ) / l_poi;

        final CNode l_from = s_GR.getNode( p_street.getDouble( "from" ) );
        final CNode l_to = s_GR.getNode( p_street.getDouble( "to" ) );

        final Double l_ap = p_factor.getDouble( "a" );
        final Double l_bp = p_factor.getDouble( "b" );

        JSONObject l_next = getCoord( l_ap, l_bp, l_from.xcoord(), l_from.ycoord(), l_pad, "max" );
        Double l_nx = l_next.getDouble( "x" );
        Double l_ny = l_next.getDouble( "y" );


        final JSONObject l_create = new JSONObject();
        l_create.put( "id", l_nx + " " + l_ny + "|1" );
        l_create.put( "x", l_nx );
        l_create.put( "y", l_ny );
        CNode l_nn = new CNode( l_create );
        CPOI l_np = new CPOI( l_nn );
        s_GR.addNode( l_nn );
        s_GR.addPoi( l_np );
        bind( l_from, l_nn, l_weight, l_pad );
        CNode l_remember = l_nn;

        for ( int l_count = 2; l_count <= l_poi; l_count++ )
        {
            l_dist = l_dist + s_sepa;
            l_next = getCoord( l_ap, l_bp, l_remember.xcoord(), l_remember.ycoord(), s_sepa, "max" );
            l_nx = l_next.getDouble( "x" );
            l_ny = l_next.getDouble( "y" );
            final JSONObject l_poio  = createPOI( l_nx + " " + l_ny + "|" + l_count, l_nx, l_ny );
            l_nn = new CNode( l_poio );
            l_np = new CPOI( l_nn );
            s_GR.addNode( l_nn );
            s_GR.addPoi( l_np );
            bind( l_remember, l_nn, l_weight, l_pad );
            l_remember = l_nn;
        }

        bind( l_remember, l_to, l_weight, l_pad );

    }

    /**
     * if the function for the street is descending, creates the POIs and the edges in between
     * @param p_street the street given
     * @param p_factor the function parameters for the street
     * @throws JSONException working with json object
     */
    private static void sprinklesLeft( final JSONObject p_street, final JSONObject p_factor ) throws JSONException
    {
        final Double l_le = p_street.getDouble( "length" );
        int l_poi = (int) ( l_le % s_sepa );
        if  ( l_poi == 0 ) l_poi = (int) ( s_sepa - 1 );
        final Double l_pad = ( l_le - ( l_poi - 1 ) * s_sepa ) / 2;
        Double l_dist = l_pad;
        final Double l_weight = p_street.getDouble( "weight" ) / l_poi;

        final CNode l_from = s_GR.getNode( p_street.getDouble( "from" ) );
        final CNode l_to = s_GR.getNode( p_street.getDouble( "to" ) );

        final Double l_ap = p_factor.getDouble( "a" );
        final Double l_bp = p_factor.getDouble( "b" );

        JSONObject l_next = getCoord( l_ap, l_bp, l_from.xcoord(), l_from.ycoord(), l_pad, "min" );
        Double l_nx = l_next.getDouble( "x" );
        Double l_ny = l_next.getDouble( "y" );


        final JSONObject l_create = new JSONObject();
        l_create.put( "id", l_nx + " " + l_ny + "|1" );
        l_create.put( "x", l_nx );
        l_create.put( "y", l_ny );
        CNode l_nn = new CNode( l_create );
        CPOI l_np = new CPOI( l_nn );
        s_GR.addNode( l_nn );
        s_GR.addPoi( l_np );
        bind( l_from, l_nn, l_weight, l_pad );
        CNode l_remember = l_nn;

        for ( int l_count = 2; l_count <= l_poi; l_count++ )
        {
            l_dist = l_dist + s_sepa;
            l_next = getCoord( l_ap, l_bp, l_remember.xcoord(), l_remember.ycoord(), s_sepa, "min" );
            l_nx = l_next.getDouble( "x" );
            l_ny = l_next.getDouble( "y" );
            final JSONObject l_poio  = createPOI( l_nx + " " + l_ny + "|" + l_count, l_nx, l_ny );
            l_nn = new CNode( l_poio );
            l_np = new CPOI( l_nn );
            s_GR.addNode( l_nn );
            s_GR.addPoi( l_np );
            bind( l_remember, l_nn, l_weight, l_pad );
            l_remember = l_nn;
        }

    }

    /**
     * checks if the street given is vertical or horizontal
     * @param p_object the street given
     * @return true if the street is vertical or horizontal
     * @throws JSONException working with json object
     */
    private static boolean checkExcp( final JSONObject p_object ) throws JSONException
    {
        final CNode l_from = s_GR.getNode( p_object.getInt( "from" ) );
        final CNode l_to = s_GR.getNode( p_object.getInt( "to" ) );
        final Double l_xfrom = l_from.xcoord();
        final Double l_yfrom = l_from.ycoord();
        final Double l_xto = l_to.xcoord();
        final Double l_yto = l_to.ycoord();
        if ( l_xfrom == l_xto )
        {
            vertical( p_object );
            return true;
        }
        else if ( l_yfrom == l_yto )
        {
            horrizontal( p_object );
            return true;
        }
        return false;
    }

    /**
     * generates pois if function is vertical
     * @param p_object the street given
     * @throws JSONException working with json object
     */
    private static void vertical( final JSONObject p_object ) throws JSONException
    {
        CNode l_remember;
        final double l_le = p_object.getDouble( "length" );
        int l_poi = (int) ( l_le % s_sepa );
        if  ( l_poi == 0 ) l_poi = (int) ( s_sepa - 1 );
        final double l_weight = p_object.getDouble( "weight" ) / ( l_poi + 1 );
        final Double l_pad = ( l_le - ( l_poi - 1 ) * s_sepa ) / 2;
        final CNode l_from = s_GR.getNode( p_object.getInt( "from" ) );
        final Double l_ys = l_from.ycoord();
        final Double l_xs = l_from.xcoord();

        if ( l_pad != 0 )
        {
            Double l_ny = l_ys + l_pad;
            final JSONObject l_create = new JSONObject();
            l_create.put( "id", l_xs + " " + l_ny + "|1" );
            l_create.put( "x", l_xs );
            l_create.put( "y", l_ny );
            CNode l_nn = new CNode( l_create );
            CPOI l_np = new CPOI( l_nn );
            s_GR.addNode( l_nn );
            s_GR.addPoi( l_np );
            bind( l_from, l_nn, l_weight, l_pad );
            l_remember = l_nn;

            for ( int l_count = 2; l_count <= l_poi; l_count++ )
            {
                l_ny = l_ny + s_sepa;
                final JSONObject l_poio  = createPOI( l_xs + " " + l_ny + "|" + l_count, l_xs, l_ny );
                l_nn = new CNode( l_poio );
                l_np = new CPOI( l_nn );
                s_GR.addNode( l_nn );
                s_GR.addPoi( l_np );
                bind( l_remember, l_nn, l_weight, l_pad );
                l_remember = l_nn;
            }

            bind( l_remember, s_GR.getNode( p_object.getInt( "to" ) ), l_weight, l_pad );

        }
    }

    /**
     * generates pois if function is horizontal
     * @param p_object the street given
     * @throws JSONException working with json object
     */
    private static void horrizontal( final JSONObject p_object ) throws JSONException
    {
        CNode l_remember;
        final double l_le = p_object.getDouble( "length" );
        int l_poi = (int) ( l_le % s_sepa );
        if  ( l_poi == 0 ) l_poi = (int) ( s_sepa - 1 );
        final double l_weight = p_object.getDouble( "weight" ) / ( l_poi + 1 );
        final Double l_pad = ( l_le - ( l_poi - 1 ) * s_sepa ) / 2;
        final CNode l_from = s_GR.getNode( p_object.getInt( "from" ) );
        final Double l_ys = l_from.ycoord();
        final Double l_xs = l_from.xcoord();

        if ( l_pad != 0 )
        {
            Double l_nx = l_xs + l_pad;
            final JSONObject l_create = new JSONObject();
            l_create.put( "id", l_nx + " " + l_ys + "|1" );
            l_create.put( "x", l_nx );
            l_create.put( "y", l_ys );
            CNode l_nn = new CNode( l_create );
            CPOI l_np = new CPOI( l_nn );
            s_GR.addNode( l_nn );
            s_GR.addPoi( l_np );
            bind( l_from, l_nn, l_weight, l_pad );
            l_remember = l_nn;

            for ( int l_count = 2; l_count <= l_poi; l_count++ )
            {
                l_nx = l_nx + s_sepa;
                final JSONObject l_poio  = createPOI( l_nx + " " + l_ys + "|" + l_count, l_nx, l_ys );
                l_nn = new CNode( l_poio );
                l_np = new CPOI( l_nn );
                s_GR.addNode( l_nn );
                s_GR.addPoi( l_np );
                bind( l_remember, l_nn, l_weight, l_pad );
                l_remember = l_nn;
            }

            bind( l_remember, s_GR.getNode( p_object.getInt( "to" ) ), l_weight, l_pad );
        }

    }

    /**
     * creates a json object to be used to construct a new node(poi)
     * @param p_id the id of the new poi
     * @param p_xc the x coordinate
     * @param p_yc the y coordinate
     * @return the json object
     * @throws JSONException working with json objects
     */
    private static JSONObject createPOI( final String p_id, final Double p_xc, final Double p_yc ) throws JSONException
    {
        final JSONObject l_object = new JSONObject();
        l_object.put( "id", p_id );
        l_object.put( "x", p_xc );
        l_object.put( "y", p_yc );
        return l_object;
    }

    /**
     * creates a new edge between a poi and a poi/node
     * @param p_from the origin node
     * @param p_to the destination node
     * @param p_weight the weight of the new edge
     * @param p_length the length of the new edge
     * @throws JSONException working with json object
     */
    private static void bind( final CNode p_from, final CNode p_to, final double p_weight, final double p_length ) throws JSONException
    {
        final JSONObject l_create = new JSONObject();
        l_create.put( "from", p_from.id() );
        l_create.put( "to", p_to.id() );
        l_create.put( "length", p_length );
        l_create.put( "weight", p_weight );
        final CEdge l_edge = new CEdge( l_create );
        s_GR.addEdge( p_from, p_to, l_edge );
    }

    /**
     * calculates the a and b of the linear function of a street
     * @param p_n1 the starting node
     * @param p_n2 the end node
     * @return a json object containing the a and b
     */
    private static JSONObject getab( final CNode p_n1, final CNode p_n2 )
    {
        final JSONObject l_object = new JSONObject();
        final Double l_xs = p_n1.xcoord();
        final Double l_ys = p_n1.ycoord();
        final Double l_xf = p_n2.xcoord();
        final Double l_yf = p_n2.ycoord();
        final Double l_ac =  ( l_ys - l_yf ) / ( l_xs - l_xf );
        final Double l_bc = l_yf + l_xf * l_ac;
        try
        {
            l_object.put( "a", l_ac );
            l_object.put( "b", l_bc );
        }
        catch ( final JSONException l_err )
        {
        }
        return l_object;
    }

    /**
     * calculates the x and y coordinates of the new node
     * @param p_af the a parameter of the linear function
     * @param p_bf the b parameter of the linear function
     * @param p_xc the x coordinates of the start node
     * @param p_yc the y coordinates of the start node
     * @param p_dist the distance between the start node and the new node
     * @param p_way the way of the function, increasing/decreasing
     * @return the json object which holds the xy coordinates for the new node
     * @throws JSONException working with json object
     */
    private static JSONObject getCoord( final Double p_af, final Double p_bf, final Double p_xc, final Double p_yc, final Double p_dist, final String p_way )
                    throws JSONException
    {
        final Double l_delta = 4 * ( p_xc + p_yc * p_af ) * ( p_xc + p_yc * p_af ) - 4 * ( 1 + p_af * p_af ) * ( p_xc * p_xc +  p_yc * p_yc - p_dist * p_dist );
        final Double l_x1  = ( 2 * ( p_xc + p_yc * p_af ) + Math.sqrt( l_delta ) ) / 2 * ( 1 + p_af * p_af );
        final Double l_y1 = p_af * l_x1 + p_bf;
        final Double l_x2  = ( 2 * ( p_xc + p_yc * p_af ) - Math.sqrt( l_delta ) ) / 2 * ( 1 + p_af * p_af );
        final Double l_y2 = p_af * l_x2 + p_bf;
        final JSONObject l_object = new JSONObject();
        if ( p_way.contentEquals( "min" ) )
        {
            if ( ( l_x1 < p_xc ) && ( l_y1 < p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else if ( ( l_x2 < p_xc ) && ( l_y2 < p_yc ) )
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        if ( p_way.contentEquals( "max" ) )
        {
            if ( ( l_x1 > p_xc ) && ( l_y1 > p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else if ( ( l_x2 > p_xc ) && ( l_y2 > p_yc ) )
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        return l_object;
    }

    /**
     * Generates a list of Points of Interest
     * @return the said list of POIs
     */
    public static Collection<CPOI> randomPOI()
    {
        final Set<CPOI> l_col = new HashSet<>();
        while ( l_col.size() < s_poi )
        {
            final int l_val = (int)( Math.random() *  s_GR.countNodes() );
            if ( l_val != 0 )
            {
                final CNode l_node =  s_GR.getNode( l_val );
                if ( l_node != null )
                {
                    final CPOI l_poi = new CPOI( l_node );
                    l_col.add( l_poi );
                }
            }
        }
        return l_col;
    }

    /**
     * route creation algorithm
     * @param p_pois the list of end nodes
     * @return the list of taken edges to the destination
     */
    public static ArrayList<List<CEdge>> routing( final Collection<CPOI> p_pois )
    {
        final ArrayList<List<CEdge>> l_routes = new ArrayList<>();
        for ( final CPOI l_poi : p_pois )
        {
            final List<CEdge> l_rou = s_GR.route( s_GR.getNode( 0 ), l_poi.id() );
            l_routes.add( l_rou );
        }
        return l_routes;
    }

    /**
     * Counting the edges which are visited more than once
     * @return the list of platooned edges along with the respective counter
     */
    public static ArrayList<CEdge> countPlat()
    {
        final ArrayList<CEdge> l_platoons = new ArrayList<>();
        final Collection<CEdge> l_edges =  s_GR.getEdges();
        for ( final CEdge l_comp : l_edges )
        {
            if ( l_comp.visited() > 1 ) l_platoons.add( l_comp );
        }
        return l_platoons;
    }

    /**
     * does the 10000 runs
     */
    public static void doTheThing()
    {
        s_out = new CSVWriter( "ResultsDijkstra.csv" );
        int l_count = 0;
        while ( l_count < s_runs )
        {
            s_GR.resetEdges();
            final Collection<CPOI> l_pois = randomPOI();
            final ArrayList<List<CEdge>> l_routes = routing( l_pois );
            final ArrayList<CEdge> l_plat = countPlat();
            s_out.writeCsvFile( l_plat );
            s_out.writeNewLine();
            l_count++;
        }
        s_out.done();
    }

    /**
     * the main function
     * @param p_args this is what java wants
     * @throws IOException because of the file work in graphInit
     * @throws JSONException because working with json objects
     */
    public static void main( final String[] p_args ) throws IOException, JSONException
    {
        graphInit( "src/test/resources/Scenario1.json" );
        doTheThing();
    }
}
