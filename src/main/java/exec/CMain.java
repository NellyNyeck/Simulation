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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * the main class
 */
public final class CMain
{
    private static final CGraph<?, CNode, CEdge> s_GR = new CGraph<>();
    private static CSpecs s_specs;

    protected CMain()
    {
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
            final Double l_length = calculateLength( l_from, l_to );
            processStreet( l_rawedge, l_length );
        }
    }



    /**
     * process each given street     *
     */
    public static void processStreet( final JSONObject p_obj, final Double p_len )
    {


    }

    public static void evenFunction(  )
    {

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


        /*final Double l_length = p_object.getDouble( "length" );
        if ( l_length.compareTo( 2 * s_sepa ) >= 0 )
        {
            int l_poi = (int) ( l_length % s_sepa );
            if ( l_poi == 0 )
                l_poi = (int) ( s_sepa - 1 );
            final Double l_padding = ( l_length - ( l_poi + 1 ) * s_sepa ) / 2;
            final Double l_weight = p_object.getDouble( "weight" ) / ( l_poi + 1 );

            final CNode l_nf = s_GR.getNode( p_object.getInt( "from" ) );
            final CNode l_nt = s_GR.getNode( p_object.getInt( "to" ) );


            l_funct.put( "type", functType( l_nf, l_nt ) );
            l_funct.put( "parameters", getab( l_nf, l_nt, l_funct.getString( "type" ) ) );
            l_funct.put( "direction", getDirection( l_nf, l_nt ) );

            toPad( l_nf, l_nt, l_padding, l_poi, l_funct, l_weight );
        }
        else
        {
            final CNode l_n1 = s_GR.getNode( p_object.getInt( "from" ) );
            final CNode l_n2 = s_GR.getNode( p_object.getInt( "to" ) );
            final Double l_weigth = p_object.getDouble( "weight" );
            bind( l_n1, l_n2, l_length, l_weigth );
        }*/
    //}

   /* protected static String functType( final CNode p_nf, final CNode p_nt )
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
    /*protected static JSONObject getab( final CNode p_n1, final CNode p_n2, final String p_type ) throws JSONException
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
    /*protected static void toPad( final CNode p_nf, final CNode p_nt, final Double p_pad, final int p_poi, final JSONObject p_about, final double p_weight )
                    throws JSONException
    {
        JSONObject l_new;
        if ( p_pad.compareTo( 0.00 ) == 0 )
        {
            CNode l_remember = p_nf;
            for ( int l_count = 1; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.xcoord(), l_remember.ycoord(), s_sepa );
                final Integer l_id = s_idcounter;
                s_idcounter++;
                final Double l_nx = l_new.getDouble( "x" );
                final Double l_ny = l_new.getDouble( "y" );
                final CNode l_newnode = new CNode( createPOI( l_id, l_nx, l_ny ) );
                final CPOI l_cpoi = new CPOI( l_newnode );
                s_GR.addPoi( l_cpoi );
                bind( l_remember, l_newnode, s_sepa, p_weight );
                l_remember = l_newnode;
            }
            bind( l_remember, p_nt, s_sepa, p_weight );
        }
        else
        {
            l_new = getCoordinates( p_about, p_nf.xcoord(), p_nf.ycoord(), p_pad );
            Integer l_id = s_idcounter;
            s_idcounter++;
            CNode l_newnode = new CNode( createPOI( l_id, l_new.getDouble( "x" ), l_new.getDouble( "y" ) ) );
            CPOI l_cpoi = new CPOI( l_newnode );
            s_GR.addPoi( l_cpoi );
            bind( p_nf, l_newnode, s_sepa, p_weight );
            CNode l_remember;
            l_remember = l_newnode;
            for ( int l_count = 2; l_count <= p_poi; l_count++ )
            {
                l_new = getCoordinates( p_about, l_remember.xcoord(), l_remember.ycoord(), s_sepa );
                l_id = s_idcounter;
                s_idcounter++;
                l_newnode = new CNode( createPOI( l_id, l_new.getDouble( "x" ), l_new.getDouble( "y" ) ) );
                l_cpoi = new CPOI( l_newnode );
                s_GR.addPoi( l_cpoi );
                bind( l_remember, l_newnode, s_sepa, p_weight );
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
     * @throws JSONException working with json
     */
    /*protected static JSONObject getCoordinates( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
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
    /*protected static JSONObject getCoordLinear( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {
        final JSONObject l_temp = p_about.getJSONObject( "parameters" );
        final Double l_af = l_temp.getDouble( "a" );
        final Double l_bf = l_temp.getDouble( "b" );
        final String l_way = p_about.getString( "direction" );
        final Double l_first = ( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) * ( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc );
        final Double l_second  = 4 * ( 1 + l_af * l_af ) * ( p_xc * p_xc + ( l_bf - p_yc ) * ( l_bf - p_yc ) - p_dist * p_dist );
        final Double l_delta = l_first - l_second;
        final Double l_x1  = ( -( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) + Math.sqrt( l_delta ) ) / ( 2 * ( 1 + l_af * l_af ) );
        final Double l_y1 = l_af * l_x1 + l_bf;
        final Double l_x2  = ( -( 2 * l_af * ( l_bf - p_yc ) - 2 * p_xc ) - Math.sqrt( l_delta ) ) / ( 2 * ( 1 + l_af * l_af ) );
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
    /*protected static JSONObject getCoordVert( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
    {
        final JSONObject l_new = new JSONObject();
        final String l_way = p_about.getString( "direction" );
        if ( l_way.contentEquals( "A" ) )
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
   /* protected static JSONObject getCoordHori( final JSONObject p_about, final Double p_xc, final Double p_yc, final Double p_dist ) throws JSONException
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
    /*protected static JSONObject createPOI( final int p_id, final Double p_xc, final Double p_yc ) throws JSONException
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
    /*protected static void bind( final CNode p_from, final CNode p_to, final double p_length, final double p_weight ) throws JSONException
    {
        JSONObject l_create = new JSONObject();
        l_create.put( "from", p_from.id() );
        l_create.put( "to", p_to.id() );
        l_create.put( "length", p_length );
        l_create.put( "weight", p_weight );
        CEdge l_edge = new CEdge( l_create );
        s_GR.addEdge( p_from, p_to, l_edge );
        l_create = new JSONObject();
        l_create.put( "from", p_to.id() );
        l_create.put( "to", p_from.id() );
        l_create.put( "length", p_length );
        l_create.put( "weight", p_weight );
        l_edge = new CEdge( l_create );
        s_GR.addEdge( p_to, p_from, l_edge );
    }

    /**
     * Generates a list of Points of Interest
     * @return the said list of POIs
     */
    /*public static Collection<CPOI> randomPOI()
    {
        final ArrayList<CPOI> l_col = new ArrayList<>();
        final HashMap<Integer, CPOI> l_pois = s_GR.getPois();
        final Random l_random = new Random();
        while ( l_col.size() < s_poi )
        {
            final int l_smtg = l_random.nextInt( s_idcounter - 1 );
            if ( l_pois.get( l_smtg ) != null )
            {
                l_col.add( l_pois.get( l_smtg ) );
            }
        }
        return l_col;
    }

    /**
     * route creation algorithm
     * @param p_pois the list of end nodes
     * @return the list of taken edges to the destination
     */
    /*public static ArrayList<List<CEdge>> routing( final Collection<CPOI> p_pois )
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
    /*public static ArrayList<CEdge> countPlat()
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
