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



import output.CSVWriter;
import environment.CGraph;
import environment.CNode;
import environment.CEdge;
import environment.CPOI;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * the main class
 */
public final class CMain
{

    //private static final CGraph<Object,CNode, CEdge> s_GR = new CGraph();
    private static final CGraph s_GR = new CGraph<>();

    private static CSVWriter s_out;

    private CMain()
    {

    }

    /**
     * initialization of the graph by reading the nodes and edges from file
     * @throws IOException because file
     */
    public static void graphInit() throws IOException
    {
        final FileReader l_fr = new FileReader( "Edges.txt" );
        final BufferedReader l_bf = new BufferedReader( l_fr );
        String l_text = l_bf.readLine();
        final int l_nodes = Integer.valueOf( l_text );
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
            final CNode l_n1 = (CNode) s_GR.getNode( Integer.valueOf( l_tokens[0] ) );
            final CNode l_n2 = (CNode) s_GR.getNode( Integer.valueOf( l_tokens[1] ) );
            final CEdge l_e1 = new CEdge( l_tokens[0] + " " + l_tokens[1] );
            final CEdge l_e2 = new CEdge( l_tokens[1] + " " + l_tokens[0] );
            s_GR.addEdge( l_n1, l_n2, l_e1 );
            s_GR.addEdge( l_n2, l_n1, l_e2 );
            l_text = l_bf.readLine();
        }
        l_bf.close();
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
                final CPOI l_poi = new CPOI( s_GR.getNode( l_val ) );
                l_col.add( l_poi );
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
        s_out = new CSVWriter();
        s_out.start( "ResultsDijkstra.csv" );
        int l_count = 0;
        while ( l_count < 10000 )
        {
            s_GR.resetEgdes();
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
        graphInit();
        doTheThing();
    }
}
