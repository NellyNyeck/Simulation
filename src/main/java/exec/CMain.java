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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
    private static Integer s_pod;
    private static Integer s_runs;
    private static CSVWriter s_out;

    protected CMain()
    {

    }

    /**
     * initialization of the graph by reading the nodes and edges from file
     * @throws IOException because file
     */
    public static void graphInit( final String p_arg ) throws IOException
    {
        String l_text = new String( Files.readAllBytes( Paths.get(  "src/test/resources/Scenario1.json" ) ), StandardCharsets.UTF_8 );
        final JSONObject l_object;
        try
        {
            l_object = new JSONObject( l_text );
            s_poi = l_object.getInt( "Nb_POI" );
            s_pod = l_object.getInt( "Nb_POD" );
            s_runs = l_object.getInt( "runs" );
            final JSONArray l_array = l_object.getJSONArray( "environment" );

        }
        catch ( final JSONException l_er )
        {
            l_er.printStackTrace();
        }


        final Reader l_fr;
        BufferedReader l_bf = null;
        try
        {
            l_fr = new InputStreamReader( new FileInputStream( "Edges.txt" ), "UTF-8" );
            l_bf = new BufferedReader( l_fr );
            l_text = l_bf.readLine();
            while ( l_text == null )
            {
                l_text = l_bf.readLine();
            }
            final int l_nodes = Integer.parseInt( l_text );
            for ( int i = 0; i < l_nodes; i++ )
            {
                final CNode l_no = new CNode( i );
                s_GR.addNode( l_no );
            }
            l_text = l_bf.readLine();
            while ( l_text != null )
            {
                final String l_del = "[ ]";
                final String[] l_tokens = l_text.split( l_del );
                final CNode l_n1 = s_GR.getNode( Integer.valueOf( l_tokens[0] ) );
                final CNode l_n2 = s_GR.getNode( Integer.valueOf( l_tokens[1] ) );
                final CEdge l_e1 = new CEdge( l_tokens[0] + " " + l_tokens[1] );
                final CEdge l_e2 = new CEdge( l_tokens[1] + " " + l_tokens[0] );
                s_GR.addEdge( l_n1, l_n2, l_e1 );
                s_GR.addEdge( l_n2, l_n1, l_e2 );
                l_text = l_bf.readLine();
            }
            l_bf.close();
        }
        catch ( final FileNotFoundException l_err )
        {
            l_err.printStackTrace();
        }
        catch ( final IOException l_err )
        {
            l_err.printStackTrace();
        }
        finally
        {
            if ( l_bf != null )
            {
                l_bf.close();
            }

        }
    }

    /**
     * Generates a list of Points of Interest
     * @param p_nb the number of POI to be generated
     * @return the said list of POIs
     */
    public static Collection<CPOI> genPOI( final int p_nb )
    {
        final Set<CPOI> l_col = new HashSet<>();
        while ( l_col.size() < p_nb )
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
        while ( l_count < 10000 )
        {
            s_GR.resetEdges();
            final Collection<CPOI> l_pois = genPOI( 6 );
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
        graphInit( "src/test/resources/Edges.txt" );
        doTheThing();
    }
}
