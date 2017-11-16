package exec;

import environment.CEdge;
import environment.CPOI;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * test class
 */
public final class TestCMain
{
    private CMain m_ain;

    /**
     * the initialization function
     */
    @Before
    public void init()
    {
        m_ain = new CMain();
    }

    /**
     * checking the graph init with right file
     * @throws IOException cuz file
     */
    @Test
    public void graphinit() throws IOException
    {
        CMain.graphInit( "Edges.txt" );
    }

    /**
     * initialize graph with a patchy file (lots of empty lines)
     * @throws IOException cuz file
     */
    @Test
    public void graphinitpatchy() throws IOException
    {
        CMain.graphInit( "PathcyEdges.txt" );
    }

    /**
     * checking the graph init with wrong file
     * @throws IOException cuz file
     */
    @Test
    public void graphnon() throws IOException
    {
        CMain.graphInit( "notafile.txt" );
    }

    /**
     * test generate POI
     * @throws IOException cuz file
     */
    @Test
    public void poi() throws IOException
    {
        CMain.graphInit( "Edges.txt" );
        CMain.genPOI( 6 );
    }

    /**
     * test routing to poi
     * @throws IOException cuz file
     */
    @Test
    public void route() throws IOException
    {
        CMain.graphInit( "Edges.txt" );
        final Collection<CPOI> l_list = CMain.genPOI( 6 );
        System.out.println( l_list.size() );
        for ( final CPOI l_poi : l_list )
        {
            System.out.println( l_poi.id() );
        }
        final ArrayList<List<CEdge>> l_routes = CMain.routing( l_list );
        System.out.println( l_routes.size() );
    }
}
