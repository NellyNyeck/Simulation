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
    private static Double s_sepa;
    //private static int s_podcap;
    //private static String s_strateg;

    protected CMain()
    {
    }

    /**
     * initialization of the graph by reading the nodes and edges from file
     *
     * @throws IOException because file
     * @throws JSONException because working with json objects
     */
    private static void graphInit( final String p_arg ) throws IOException, JSONException
    {
        JSONObject l_trans;
        final String l_text = new String( Files.readAllBytes( Paths.get( p_arg ) ), StandardCharsets.UTF_8 );
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
            l_trans = l_array.getJSONObject( l_in );
            processStreet( l_trans );
        }
    }

    /**
     * process each given street
     *
     * @param p_object the JSON object which holds the street information
     * @throws JSONException working with json objects
     */
    public static void processStreet( final JSONObject p_object ) throws JSONException
    {
        final JSONObject l_funct = new JSONObject();
        final Double l_length = p_object.getDouble( "length" );
        if ( l_length.compareTo( 2 * s_sepa ) >= 0 )
        {
            int l_poi = (int) ( l_length % s_sepa );
            if ( l_poi == 0 )
                l_poi = (int) ( s_sepa - 1 );
            final Double l_padding = ( l_length - ( l_poi + 1 ) * s_sepa ) / 2;
            final Double l_weight = p_object.getDouble( "weight" ) / ( l_poi + 1 );

            final CNode l_nf = s_GR.getNode( p_object.getString( "from" ) );
            final CNode l_nt = s_GR.getNode( p_object.getString( "to" ) );


            l_funct.put( "type", functType( l_nf, l_nt ) );
            l_funct.put( "parameters", getab( l_nf, l_nt, l_funct.getString( "type" ) ) );
            l_funct.put( "direction", getDirection( l_nf, l_nt ) );

            toPad( l_nf, l_nt, l_padding, l_poi, l_funct, l_weight );
        }
        else
        {
            final CNode l_n1 = s_GR.getNode( p_object.getString( "from" ) );
            final CNode l_n2 = s_GR.getNode( p_object.getString( "to" ) );
            final Double l_weigth = p_object.getDouble( "weight" );
            bind( l_n1, l_n2, l_length, l_weigth );
        }
        /**
         * calculate paddin, number of pois, and weight of segments
         * get starting point coordinates
         * calculate the function type: vertical, horizontal, linear
         * calculate functions a and b
         * calculate function direction
         * calculate new coordinates
         * add first poi
         * for loop the others
         * bind last poi
         */
    }

    protected static String functType( final CNode p_nf, final CNode p_nt )
    {
        final Double l_xf = p_nf.xcoord();
        final Double l_yf = p_nf.ycoord();
        final Double l_xt = p_nt.xcoord();
        final Double l_yt = p_nt.ycoord();
        if ( l_xf.compareTo( l_xt ) == 0 )
        {
            return "Vertical";
        }
        else if ( l_yf.compareTo( l_yt ) == 0 )
        {
            return "Horizontal";
        }
        else
        {
            return "Normal";
        }
    }

    /**
     * calculates the a and b of the linear function of a street
     *
     * @param p_n1 the starting node
     * @param p_n2 the end node
     * @return a json object containing the a and b
     */
    protected static JSONObject getab( final CNode p_n1, final CNode p_n2, final String p_type ) throws JSONException
    {
        final JSONObject l_object = new JSONObject();
        if ( p_type.contentEquals( "Normal" ) )
        {

            final Double l_xs = p_n1.xcoord();
            final Double l_ys = p_n1.ycoord();
            final Double l_xf = p_n2.xcoord();
            final Double l_yf = p_n2.ycoord();
            final Double l_ac = ( l_ys - l_yf ) / ( l_xs - l_xf );
            final Double l_bc = l_yf - l_xf * l_ac;
            l_object.put( "a", l_ac );
            l_object.put( "b", l_bc );

        }
        else if ( p_type.contentEquals( "Vertical" ) )
        {
            l_object.put( "a", "?" );
            l_object.put( "b", "?" );
        }
        else if ( p_type.contentEquals( "Horizontal" ) )
        {
            l_object.put( "a", 0 );
            l_object.put( "b", p_n1.ycoord() );
        }
        return l_object;
    }


    protected static String getDirection( final CNode p_nf, final CNode p_nt )
    {
        final Double l_xf = p_nf.xcoord();
        final Double l_yf = p_nf.ycoord();
        final Double l_xt = p_nt.xcoord();
        final Double l_yt = p_nt.ycoord();
        if ( l_xf.compareTo( l_xt ) < 0 )
        {
            if ( l_yf.compareTo( l_yt ) == 0 )
            {
                return "R";
            }
            else if ( l_yf.compareTo( l_yt ) < 0 )
            {
                return "AR";
            }
            else
            {
                return "DR";
            }
        }
        else if ( l_xf.compareTo( l_xt ) > 0 )
        {
            if ( l_yf.compareTo( l_yt ) == 0 )
            {
                return "L";
            }
            else if ( l_yf.compareTo( l_yt ) < 0 )
            {
                return "AL";
            }
            else
            {
                return "DL";
            }
        }
        else
        {
            if ( l_yf.compareTo( l_yt ) < 0 )
            {
                return "A";
            }
            else if ( l_yf.compareTo( l_yt ) > 0 )
            {
                return "D";
            }
        }
        return null;
    }


    /**
     * to pad or not to pad
     * @param p_nf the starting node
     * @param p_nt the ending node
     * @param p_pad the padding
     * @param p_poi the nb of pois to be generated
     * @param p_about the characteristics of the function
     * @param p_weight the weigth of each segment
     * @throws JSONException working with json object
     */
    private static void toPad( final CNode p_nf, final CNode p_nt, final Double p_pad, final int p_poi, final JSONObject p_about, final double p_weight ) throws JSONException
    {
        JSONObject l_new;
        if ( p_pad.compareTo( 0.00 ) == 0 )
        {
            CNode l_remember = p_nf;
            for ( int l_count = 1; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.xcoord(), l_remember.ycoord(), s_sepa );
                final String l_id = p_nf.id() + "|" + p_nt.id() + "." + l_count;
                final Double l_nx = l_new.getDouble( "x" );
                final Double l_ny = l_new.getDouble( "y" );
                final CNode l_newnode = new CNode( createPOI( l_id, l_nx, l_ny ) );
                bind( l_remember, l_newnode, s_sepa, p_weight );
                l_remember = l_newnode;
            }
            bind( l_remember, p_nf, s_sepa, p_weight );
        }
        else
        {
            CNode l_remember;
            l_new = getCoordinates( p_about, p_nf.xcoord(), p_nf.ycoord(), p_pad );
            String l_id = p_nf.id() + "|" + p_nt.id() + ".1";
            CNode l_newnode = new CNode( createPOI( l_id, l_new.getDouble( "x" ), l_new.getDouble( "y" ) ) );
            bind( p_nf, l_newnode, s_sepa, p_weight );
            l_remember = l_newnode;
            for ( int l_count = 2; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.xcoord(), l_remember.ycoord(), s_sepa );
                l_id = p_nf.id() + "|" + p_nt.id() + "." + l_count;
                l_newnode = new CNode( createPOI( l_id, l_new.getDouble( "x" ), l_new.getDouble( "y" ) ) );
                bind( l_remember, l_newnode, s_sepa, p_weight );
                l_remember = l_newnode;
            }
            bind( l_remember, p_nf, p_pad, p_weight );
        }
    }

    /**
     * calls the respective get coordinates function based on type
     * @param p_about the object containing the functions characteristics
     * @param p_xc the x coordinates of the starting node
     * @param p_yc the y coordinates of the starting node
     * @param p_dist the distance between the starting and the new node
     * @return the constructed json object
     * @throws JSONException working with json
     */
    private static JSONObject getCoordinates( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {

        final String l_type = p_about.getString( "type" );
        if ( l_type.contentEquals( "Normal" ) )
        {
            return getCoordLinear( p_about, p_xc, p_yc, p_dist );
        }
        else if ( l_type.contentEquals( "Vertical" ) )
        {
            return getCoordVert( p_about, p_xc, p_yc, p_dist );
        }
        else if ( l_type.contentEquals( "Horizontal" ) )
        {
            return getCoordHori( p_about, p_xc, p_yc, p_dist );
        }
        return null;
    }

    /**
     * calculates the x and y coordinates of the new node if the function is normal linear
     * @param p_about the Json object which holds the functions characteristics
     * @param p_xc the x coordinates of the start node
     * @param p_yc the y coordinates of the start node
     * @param p_dist the distance between the start node and the new node
     * @return the json object which holds the xy coordinates for the new node
     * @throws JSONException working with json object
     */
    private static JSONObject getCoordLinear( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {
        final JSONObject l_temp = p_about.getJSONObject( "parameters" );
        final Double l_af = l_temp.getDouble( "a" );
        final Double l_bf = l_temp.getDouble( "b" );
        final String l_way = p_about.getString( "direction" );
        final Double l_delta = 4 * ( p_xc + p_yc * l_af ) * ( p_xc + p_yc * l_af ) - 4 * ( 1 + l_af * l_af ) * ( p_xc * p_xc +  p_yc * p_yc - p_dist * p_dist );
        final Double l_x1  = ( 2 * ( p_xc + p_yc * l_af ) + Math.sqrt( l_delta ) ) / 2 * ( 1 + l_af * l_af );
        final Double l_y1 = l_af * l_x1 + l_bf;
        final Double l_x2  = ( 2 * ( p_xc + p_yc * l_af ) - Math.sqrt( l_delta ) ) / 2 * ( 1 + l_af * l_af );
        final Double l_y2 = l_af * l_x2 + l_bf;
        final JSONObject l_object = new JSONObject();
        if ( l_way.contentEquals( "AR" ) )
        {
            if ( ( l_x1 > p_xc ) && ( l_y1 > p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        if ( l_way.contentEquals( "DR" ) )
        {
            if ( ( l_x1 > p_xc ) && ( l_y1 < p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        if ( l_way.contentEquals( "AL" ) )
        {
            if ( ( l_x1 < p_xc ) && ( l_y1 > p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        if ( l_way.contentEquals( "DL" ) )
        {
            if ( ( l_x1 < p_xc ) && ( l_y1 < p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        return l_object;
    }

    /**
     * calculates the x and y coordinates of the new node if the function is vertical
     * @param p_about the Json object which holds the functions characteristics
     * @param p_xc the x coordinates of the starting point
     * @param p_yc the y coordinates of the starting point
     * @param p_dist the distance between the starting point and the new point
     * @return a json object containing the new coordinates
     * @throws JSONException working with json object
     */
    private static JSONObject getCoordVert( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {
        final JSONObject l_new = new JSONObject();
        final String l_way = p_about.getString( "direction" );
        if ( l_way.contentEquals( "U" ) )
        {
            final Double l_nx = p_xc;
            final Double l_ny = p_yc + p_dist;
            l_new.put( "x", l_nx );
            l_new.put( "y", l_ny );
        }
        else if ( l_way.contentEquals( "D" ) )
        {
            final Double l_nx = p_xc;
            final Double l_ny = p_yc - p_dist;
            l_new.put( "x", l_nx );
            l_new.put( "y", l_ny );
        }
        return l_new;
    }

    /**
     * calculates the x and y coordinates of the new node if the function is horizontal
     * @param p_about the Json object which holds the functions characteristics
     * @param p_xc the x coordinates of the starting point
     * @param p_yc the y coordinates of the starting point
     * @param p_dist the distance between the starting point and the new point
     * @return a json object containing the new coordinates
     * @throws JSONException working with json object
     */
    private static JSONObject getCoordHori( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {
        final JSONObject l_new = new JSONObject();
        final String l_way = p_about.getString( "direction" );
        if ( l_way.contentEquals( "R" ) )
        {
            final Double l_nx = p_xc + p_dist;
            final Double l_ny = p_yc;
            l_new.put( "x", l_nx );
            l_new.put( "y", l_ny );
        }
        else if ( l_way.contentEquals( "L" ) )
        {
            final Double l_nx = p_xc - p_dist;
            final Double l_ny = p_yc;
            l_new.put( "x", l_nx );
            l_new.put( "y", l_ny );
        }
        return l_new;
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
    protected static void bind( final CNode p_from, final CNode p_to, final double p_length, final double p_weight ) throws JSONException
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
                final CNode l_node =  s_GR.getNode( String.valueOf( l_val ) );
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
            final List<CEdge> l_rou = s_GR.route( s_GR.getNode( "0" ), l_poi.id() );
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
        final CSVWriter l_sout = new CSVWriter( "ResultsDijkstra.csv" );
        int l_count = 0;
        while ( l_count < s_runs )
        {
            s_GR.resetEdges();
            final Collection<CPOI> l_pois = randomPOI();
            final ArrayList<List<CEdge>> l_routes = routing( l_pois );
            final ArrayList<CEdge> l_plat = countPlat();
            l_sout.writeCsvFile( l_plat );
            l_sout.writeNewLine();
            l_count++;
        }
        l_sout.done();
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
