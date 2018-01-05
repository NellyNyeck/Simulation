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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * the main class
 */
public final class CMain
{
    protected static int s_idcounter;
    protected static final CGraph<?, CNode, CEdge> s_GR = new CGraph<>();
    protected static CSpecs s_specs;

    protected CMain()
    {
    }

    public static void setSpecs( final JSONObject p_specs )
    {
        s_specs = new CSpecs( p_specs );
    }

    /**
     * read the file and initialize the things
     * @param p_arg the path of the file
     * @throws IOException file
     * @throws ParseException json
     */
    public static void fileRead( final String p_arg ) throws IOException, ParseException
    {
        final JSONParser l_parser = new JSONParser();
        final InputStream l_inputstream = new FileInputStream( p_arg );
        final Reader l_filereader = new InputStreamReader( l_inputstream, "UTF-8" );
        final JSONObject l_object = (JSONObject) l_parser.parse( l_filereader );
        s_specs = new CSpecs( (JSONObject) l_object.get( "simulation specification" ) );
        final JSONObject l_environment = (JSONObject) l_object.get( "environment" );
        JSONArray l_array = (JSONArray) l_environment.get( "nodes" );
        createNodes( l_array );
        l_array = (JSONArray) l_environment.get( "edges" );
        createEdges( l_array );
    }

    /**
     * creates the nodes
     * @param p_nodes json array with the nodes
     */
    public static void createNodes( final JSONArray p_nodes )
    {
        for ( int l_in = 0; l_in < p_nodes.size(); l_in++ )
        {
            final CNode l_cnode = new CNode( (JSONObject) p_nodes.get( l_in ) );
            s_GR.addNode( l_cnode );
            s_idcounter++;
        }

    }

    /**
     * creates edges through a long and arduous process
     * @param p_edges the json array with edges
     */
    public static void createEdges( final JSONArray p_edges )
    {
        for ( int l_in = 0; l_in < p_edges.size(); l_in++ )
        {
            final JSONObject l_rawedge = (JSONObject) p_edges.get( l_in );
            final CNode l_from = s_GR.getNode( (String) l_rawedge.get( "from" ) );
            final CNode l_to = s_GR.getNode( (String) l_rawedge.get( "to" ) );
            final JSONObject l_funct = (JSONObject) l_rawedge.get( "poi distribution function" );
            if ( l_funct.isEmpty() )
            {
                final CEdge l_edge = new CEdge( l_rawedge );
                s_GR.addEdge( l_from, l_to, l_edge );
            }
            else
            {
                final Double l_length = calculateLength( l_from, l_to );
                processStreet( l_rawedge, l_length );
            }
        }
    }



    /**
     * process each given street     *
     */
    public static void processStreet( final JSONObject p_obj, final Double p_len )
    {
        final JSONObject l_funct = (JSONObject) p_obj.get( "poi distribution function" );
        final String l_name = (String) l_funct.get( "name" );
        final JSONArray l_params = (JSONArray) l_funct.get( "parameters" );
        switch ( l_name.toLowerCase() )
        {
            case "even":
                evenFunction( p_obj, p_len, l_params );
                break;
            default:
                break;
        }

    }

    /**
     * calculating the pois with an even function
     * @param p_raw raw edge json object
     * @param p_length length of edge
     * @param p_params the parameters of the function
     */
    public static void evenFunction( final JSONObject p_raw, final Double p_length, final JSONArray p_params )
    {
        final Double l_sepa = (Double) p_params.get( 0 );
        if ( p_length.compareTo( 2 * l_sepa ) >= 0 )
        {
            Double l_padding = 0.00;
            Double l_weight = (Double) p_raw.get( "weight" );
            int l_poi = 0;
            final double l_ceva = p_length % l_sepa;
            if ( l_ceva == 0.00 )
            {
                l_poi = (int) ( p_length / l_sepa ) - 1;
            }
            else
            {
                l_poi = (int) ( p_length / l_sepa );
                l_padding = ( p_length - ( l_poi - 1 ) * l_sepa ) / 2;
            }
            l_weight = l_weight / ( l_poi + 1 );
            final CNode l_from = s_GR.getNode( (String) p_raw.get( "from" ) );
            final CNode l_to = s_GR.getNode( (String) p_raw.get( "to" ) );

            final JSONObject l_funct = new JSONObject(  );
            l_funct.put( "type", calculateOrientation( l_from, l_to ) );
            l_funct.put( "parameters", getab( l_from, l_to, (String) l_funct.get( "type" ) ) );

            toPad( l_from, l_to, l_padding, l_poi, l_funct, l_weight, l_sepa );
        }
        else
        {
            final CNode l_n1 = s_GR.getNode( (String) p_raw.get( "from" ) );
            final CNode l_n2 = s_GR.getNode( (String) p_raw.get( "to" ) );
            final Double l_weigth = (Double) p_raw.get( "weight" );
            bind( l_n1, l_n2, p_length, l_weigth );
        }
    }

    /**
     * calculates the length of the given street based on the coordinates type
     * @param p_from from node
     * @param p_to to node
     * @return the distance
     */
    public static Double calculateLength( final CNode p_from, final CNode p_to )
    {
        if ( s_specs.coordType().contentEquals( "synthetic" ) )
        {
            Double l_distance = ( p_from.firstCoord() - p_to.firstCoord() ) * ( p_from.firstCoord() - p_to.firstCoord() );
            l_distance = l_distance + ( p_from.secondCoord() - p_to.secondCoord() ) * ( p_from.secondCoord() - p_to.secondCoord() );
            l_distance = Math.sqrt( l_distance );
            return l_distance;
        }
        else if ( s_specs.coordType().contentEquals( "geographical" ) )
        {
            return Double.valueOf( 0 );
        }
        return null;
    }

    /**
     * calculates the edge as a function
     * @param p_from start node
     * @param p_to end node
     * @return string with function orientaion
     */
    protected static String calculateOrientation( final CNode p_from, final CNode p_to )
    {
        String l_about = new String(  );
        final Double l_xf = p_from.firstCoord();
        final Double l_yf = p_from.secondCoord();
        final Double l_xt = p_to.firstCoord();
        final Double l_yt = p_to.secondCoord();
        if ( l_xf.compareTo( l_xt ) == 0 )
        {
            if ( l_yf < l_yt )
            {
                l_about = "VA";
            }
            else
            {
                l_about = "VD";
            }
        }
        else if ( l_yf.compareTo( l_yt ) == 0 )
        {
            if ( l_xf < l_xt )
            {
                l_about = "HR";
            }
            else l_about = "HL";
        }
        else
        {
            if ( ( l_xf < l_xt ) && ( l_yf < l_yt ) )
            {
                l_about = "OAR";
            }
            else if ( ( l_xf < l_xt ) && ( l_yf > l_yt ) )
            {
                l_about = "ODR";
            }
            else if ( ( l_xf > l_xt ) && ( l_yf < l_yt ) )
            {
                l_about = "OAL";
            }
            else l_about = "ODL";
        }
        return l_about;
    }

    /**
     * calculates the a and b of the linear function of a street
     *
     * @param p_n1 the starting node
     * @param p_n2 the end node
     * @return a json object containing the a and b
     */
    protected static JSONObject getab( final CNode p_n1, final CNode p_n2, final String p_type )
    {
        final JSONObject l_object = new JSONObject();
        if ( p_type.contains( "O" ) )
        {

            final Double l_xs = p_n1.firstCoord();
            final Double l_ys = p_n1.secondCoord();
            final Double l_xf = p_n2.firstCoord();
            final Double l_yf = p_n2.secondCoord();
            final Double l_ac = ( l_ys - l_yf ) / ( l_xs - l_xf );
            final Double l_bc = l_yf - l_xf * l_ac;
            l_object.put( "a", l_ac );
            l_object.put( "b", l_bc );

        }
        else if ( p_type.contains( "V" ) )
        {
            l_object.put( "a", "?" );
            l_object.put( "b", "?" );
        }
        else if ( p_type.contains( "H" ) )
        {
            l_object.put( "a", 0.00 );
            l_object.put( "b", p_n1.secondCoord() );
        }
        return l_object;
    }


    /**
     * to pad or not to pad
     * @param p_nf the starting node
     * @param p_nt the ending node
     * @param p_pad the padding
     * @param p_poi the nb of pois to be generated
     * @param p_about the characteristics of the function
     * @param p_weight the weigth of each segment
     */
    protected static void toPad( final CNode p_nf, final CNode p_nt, final Double p_pad, final int p_poi, final JSONObject p_about, final double p_weight,
                                 final Double p_sepa )
    {
        JSONObject l_new;
        if ( p_pad.compareTo( 0.00 ) == 0 )
        {
            CNode l_remember = p_nf;
            for ( int l_count = 1; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.firstCoord(), l_remember.secondCoord(), p_sepa );
                final String l_id = String.valueOf( s_idcounter );
                s_idcounter++;
                final Double l_nx = (Double) l_new.get( "first" );
                final Double l_ny = (Double) l_new.get( "second" );
                final CNode l_newnode = new CNode( createPOI( l_id, l_nx, l_ny ) );
                final CPOI l_cpoi = new CPOI( l_newnode );
                s_GR.addPoi( l_cpoi );
                bind( l_remember, l_newnode, p_sepa, p_weight );
                l_remember = l_newnode;
            }
            bind( l_remember, p_nt, p_sepa, p_weight );
        }
        else
        {
            l_new = getCoordinates( p_about, p_nf.firstCoord(), p_nf.secondCoord(), p_pad );
            String l_id = String.valueOf( s_idcounter );
            s_idcounter++;
            CNode l_newnode = new CNode( createPOI( l_id, (Double) l_new.get( "first" ), (Double) l_new.get( "second" ) ) );
            CPOI l_cpoi = new CPOI( l_newnode );
            s_GR.addPoi( l_cpoi );
            bind( p_nf, l_newnode, p_sepa, p_weight );
            CNode l_remember;
            l_remember = l_newnode;
            for ( int l_count = 2; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.secondCoord(), l_remember.firstCoord(), p_sepa );
                l_id = String.valueOf( s_idcounter );
                s_idcounter++;
                l_newnode = new CNode( createPOI( l_id, (Double) l_new.get( "first" ), (Double) l_new.get( "second" ) ) );
                l_cpoi = new CPOI( l_newnode );
                s_GR.addPoi( l_cpoi );
                bind( l_remember, l_newnode, p_sepa, p_weight );
                l_remember = l_newnode;
            }
            bind( l_remember, p_nt, p_pad, p_weight );
        }
    }

    /**
     * calls the respective get coordinates function based on type
     * @param p_about the object containing the functions characteristics
     * @param p_xc the x coordinates of the starting node
     * @param p_yc the y coordinates of the starting node
     * @param p_dist the distance between the starting and the new node
     * @return the constructed json object
     */
    protected static JSONObject getCoordinates( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist )
    {

        final String l_type = (String) p_about.get( "type" );
        if ( l_type.contains( "O" ) )
        {
            return getCoordLinear( p_about, p_xc, p_yc, p_dist );
        }
        else if ( l_type.contains( "V" ) )
        {
            return getCoordVert( p_about, p_xc, p_yc, p_dist );
        }
        else if ( l_type.contains( "H" ) )
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
     */
    protected static JSONObject getCoordLinear( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist )
    {
        final JSONObject l_temp = (JSONObject) p_about.get( "parameters" );
        final Double l_af = (Double) l_temp.get( "a" );
        final Double l_bf = (Double) l_temp.get( "b" );
        final String l_way = (String) p_about.get( "type" );
        final Double l_first = ( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) * ( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc );
        final Double l_second  = 4 * ( 1 + l_af * l_af ) * ( p_xc * p_xc + ( l_bf - p_yc ) * ( l_bf - p_yc ) - p_dist * p_dist );
        final Double l_delta = l_first - l_second;
        final Double l_x1  = ( -( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) + Math.sqrt( l_delta ) ) / ( 2 * ( 1 + l_af * l_af ) );
        final Double l_y1 = l_af * l_x1 + l_bf;
        final Double l_x2  = ( -( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) - Math.sqrt( l_delta ) ) / ( 2 * ( 1 + l_af * l_af ) );
        final Double l_y2 = l_af * l_x2 + l_bf;
        final JSONObject l_object = new JSONObject();
        if ( l_way.contains( "AR" ) )
        {
            if ( ( l_x1 > p_xc ) && ( l_y1 > p_yc ) )
            {
                l_object.put( "first", l_x1 );
                l_object.put( "second", l_y1 );
            }
            else
            {
                l_object.put( "first", l_x2 );
                l_object.put( "second", l_y2 );
            }
        }
        if ( l_way.contains( "DR" ) )
        {
            if ( ( l_x1 > p_xc ) && ( l_y1 < p_yc ) )
            {
                l_object.put( "first", l_x1 );
                l_object.put( "second", l_y1 );
            }
            else
            {
                l_object.put( "first", l_x2 );
                l_object.put( "second", l_y2 );
            }
        }
        if ( l_way.contains( "AL" ) )
        {
            if ( ( l_x1 < p_xc ) && ( l_y1 > p_yc ) )
            {
                l_object.put( "first", l_x1 );
                l_object.put( "second", l_y1 );
            }
            else
            {
                l_object.put( "first", l_x2 );
                l_object.put( "second", l_y2 );
            }
        }
        if ( l_way.contains( "DL" ) )
        {
            if ( ( l_x1 < p_xc ) && ( l_y1 < p_yc ) )
            {
                l_object.put( "first", l_x1 );
                l_object.put( "second", l_y1 );
            }
            else
            {
                l_object.put( "first", l_x2 );
                l_object.put( "second", l_y2 );
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
     */
    protected static JSONObject getCoordVert( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist )
    {
        final JSONObject l_new = new JSONObject();
        final String l_way = (String) p_about.get( "type" );
        if ( l_way.contains( "A" ) )
        {
            final Double l_nx = p_xc;
            final Double l_ny = p_yc + p_dist;
            l_new.put( "first", l_nx );
            l_new.put( "second", l_ny );
        }
        else if ( l_way.contains( "D" ) )
        {
            final Double l_nx = p_xc;
            final Double l_ny = p_yc - p_dist;
            l_new.put( "first", l_nx );
            l_new.put( "second", l_ny );
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
     */
    protected static JSONObject getCoordHori( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist )
    {
        final JSONObject l_new = new JSONObject();
        final String l_way = (String) p_about.get( "type" );
        if ( l_way.contains( "R" ) )
        {
            final Double l_nx = p_xc + p_dist;
            final Double l_ny = p_yc;
            l_new.put( "first", l_nx );
            l_new.put( "second", l_ny );
        }
        else if ( l_way.contains( "L" ) )
        {
            final Double l_nx = p_xc - p_dist;
            final Double l_ny = p_yc;
            l_new.put( "first", l_nx );
            l_new.put( "second", l_ny );
        }
        return l_new;
    }



    /**
     * creates a json object to be used to construct a new node(poi)
     * @param p_id the id of the new poi
     * @param p_xc the x coordinate
     * @param p_yc the y coordinate
     * @return the json object
     */
    protected static JSONObject createPOI( final String p_id, final Double p_xc, final Double p_yc )
    {
        final JSONObject l_coord = new JSONObject();
        l_coord.put( "first coordinate", p_xc );
        l_coord.put( "second coordinate", p_yc );
        final JSONObject l_obj = new JSONObject(  );
        l_obj.put( "name", p_id );
        l_obj.put( "coordinates", l_coord );
        return l_obj;
    }

    /**
     * creates a new edge between a poi and a poi/node
     * @param p_from the origin node
     * @param p_to the destination node
     * @param p_weight the weight of the new edge
     * @param p_length the length of the new edge
     */
    protected static void bind( final CNode p_from, final CNode p_to, final double p_length, final double p_weight )
    {
        JSONObject l_create = new JSONObject();
        l_create.put( "from", p_from.name() );
        l_create.put( "to", p_to.name() );
        l_create.put( "length", p_length );
        l_create.put( "weight", p_weight );
        CEdge l_edge = new CEdge( l_create );
        s_GR.addEdge( p_from, p_to, l_edge );
        l_create = new JSONObject();
        l_create.put( "from", p_to.name() );
        l_create.put( "to", p_from.name() );
        l_create.put( "length", p_length );
        l_create.put( "weight", p_weight );
        l_edge = new CEdge( l_create );
        s_GR.addEdge( p_to, p_from, l_edge );
    }



    /**
     * route creation algorithm
     * @param p_pois the list of end nodes
     * @return the list of taken edges to the destination
     */
    public static ArrayList<List<CEdge>> routing( final Collection<CPOI> p_pois, final CNode p_depot )
    {
        final ArrayList<List<CEdge>> l_routes = new ArrayList<>();
        for ( final CPOI l_poi : p_pois )
        {
            final List<CEdge> l_rou = s_GR.route( p_depot, l_poi.id() );
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
    /*public static void doTheThing( String p_)
    {
        final CSVWriter l_sout = new CSVWriter( "ResultsDijkstra.csv" );
        s_GR.resetEdges();
        final ArrayList<List<CEdge>> l_routes = routing( l_pois );
        final ArrayList<CEdge> l_plat = countPlat();
        l_sout.writeCsvFile( l_plat );
        l_sout.writeNewLine();
        l_sout.done();
    }*/

    /**
     * the main function
     * @param p_args java
     * @throws IOException file
     * @throws ParseException json
     */
    public static void main( final String[] p_args ) throws IOException, ParseException
    {
        fileRead( "src/test/resources/Examples/example2.json" );

    }
}
