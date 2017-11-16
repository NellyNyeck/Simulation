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
        final Collection<CPOI> l_list  = CMain.genPOI( 6 );
        if ( l_list.size() == 6 )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
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
        final ArrayList<List<CEdge>> l_routes = CMain.routing( l_list );
        if ( l_routes.size() == l_list.size() )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }


    /**
     * test platooning edges count
     * @throws IOException cuz file
     */
    @Test
    public void platoon() throws IOException
    {
        CMain.graphInit( "Edges.txt" );
        final Collection<CPOI> l_list = CMain.genPOI( 6 );
        final ArrayList<List<CEdge>> l_routes = CMain.routing( l_list );
        final Collection<CEdge> l_edges = CMain.countPlat();
        if ( ( l_edges.size() > 0 ) && ( l_routes.size() == l_list.size() ) )
        {
            System.out.println( true );
        }
        else
        {
            System.out.println( false );
        }
    }

    /**
     * test do the thing
     */
    @Test
    public void doit()
    {
        CMain.doTheThing();
    }

    @Test
    public void themain() throws IOException
    {
        CMain.main( new String[5] );
    }
}
