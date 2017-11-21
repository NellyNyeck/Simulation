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


import com.sun.jndi.cosnaming.CNCtx;
import environment.CEdge;
import environment.CGraph;
import environment.CNode;
import environment.CPOI;
import environment.INode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mortbay.util.ajax.JSON;
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
     */
    public static void graphInit( final String p_arg ) throws IOException
    {
        final String l_text = new String( Files.readAllBytes( Paths.get(  p_arg ) ), StandardCharsets.UTF_8 );
        final JSONObject l_object;
        try
        {
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
                genPOI( l_obj );
            }
        }
        catch ( final JSONException l_er )
        {
            l_er.printStackTrace();
        }
    }

    public static void genPOI( final JSONObject p_object )
    {
        final double l_le;
        try
        {
            l_le = p_object.getDouble( "length" );
            final int l_from  = p_object.getInt( "from" );
            final int l_to = p_object.getInt( "to" );
            final double l_we = p_object.getDouble( "weigth" );
            if ( l_le > s_sepa)
            {
                JSONObject l_funct = getab( s_GR.getNode( l_from ), s_GR.getNode( l_to ));
                final Double l_ap = l_funct.getDouble( "a" );
                final Double l_bp = l_funct.getDouble( "b" );
                final int l_poi = (int) ( l_le % s_sepa - 1 );
                final Double l_pad = ( l_le - ( l_poi -1 ) * s_sepa ) / 2;
                getCoord(l_ap, l_bp, s_GR.getNode( l_from ).xcoord(), s_GR.getNode( l_to ).ycoord(), l_pad);
                for ( int l_count = 1 ; l_count < l_poi ; l_count++ )
                {

                }
            }
        }
        catch ( JSONException p_e )
        {
            p_e.printStackTrace();
        }
    }


    private static void addPOI (final String p_id,final Double p_xc, final Double p_yc
    {
        try
        {
            JSONObject l_object = new JSONObject();
            l_object.put( "id", p_id );
            l_object.put( "x", p_xc );
            l_object.put( "y", p_yc );
            final CPOI l_cpoi = new CPOI( l_object );
            s_GR.addNode( l_cpoi );

        }
        catch ( JSONException p_e )
        {
            p_e.printStackTrace();
        }
    }

    private static JSONObject check_excp ( final CNode n1, final CNode n2, final Double p_dist, final Double p_padding, final int p_poi)
    {
        final Double l_xs = n1.xcoord();
        final Double l_ys = n1.ycoord();
        final Double l_xf = n2.xcoord();
        final Double l_yf = n2.ycoord();
        if ( l_xs == l_xf )
        {
            final Double l_nx = l_xf;
            if( p_padding != 0 )
            {
                try
                {
                    Double l_ny = l_ys + p_padding;
                    JSONObject l_create = new JSONObject();
                    l_create.put( "id", l_nx+" " + l_ny + "|1" );
                    l_create.put(  )
                }
                catch ( JSONException p_e )
                {
                    p_e.printStackTrace();
                }
            }

        }

    }

    private static JSONObject getab( final CNode n1, final CNode n2 )
    {
        final JSONObject l_object = new JSONObject();
        final Double p_xs = n1.xcoord();
        final Double p_ys = n1.ycoord();
        final Double p_xf = n2.xcoord();
        final Double p_yf = n2.ycoord();
        final Double l_ac =  (p_ys - p_yf) / ( p_xs - p_xf );
        final Double l_bc = p_yf + p_xf * l_ac;
        try
        {
            l_object.put( "a", l_ac );
            l_object.put( "b", l_bc );
        }
        catch ( JSONException l_err )
        {
        }
        return l_object;
    }

    private static JSONObject getCoord( final Double p_af, final Double p_bf, final Double p_xc, final Double p_yc, final Double p_dist )
    {
        final Double l_delta = 4 * ( p_xc + p_yc * p_af ) * ( p_xc + p_yc * p_af ) - 4 * ( 1 + p_af * p_af ) * ( p_xc * p_xc +  p_yc * p_yc - p_dist * p_dist );
        final Double l_x1  = ( 2 * ( p_xc + p_yc * p_af ) + Math.sqrt( l_delta ) ) / 2 * ( 1 + p_af * p_af );
        final Double l_y1 = p_af * l_x1 + p_bf;
        final Double l_x2  = ( 2 * ( p_xc + p_yc * p_af ) - Math.sqrt( l_delta ) ) / 2 * ( 1 + p_af * p_af );
        final Double l_y2 = p_af * l_x2 + p_bf;
        final JSONObject l_object = new JSONObject();
        try
        {
            if ( ( l_x1 >= p_xc ) || ( l_y1 >= p_yc ) )
            {
                l_object.put( "x", l_x1 );
                l_object.put( "y", l_y1 );
            }
            else if ( ( l_x2 >= p_xc ) || ( l_y2 >= p_yc ) )
            {
                l_object.put( "x", l_x2 );
                l_object.put( "y", l_y2 );
            }
        }
        catch ( JSONException p_e )
        {
            p_e.printStackTrace();
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
            final Collection<CPOI> l_pois = genPOI();
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
     */
    public static void main( final String[] p_args ) throws IOException
    {
        graphInit( "src/test/resources/Scenario1.json" );
        doTheThing();
    }
}
