import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import environment.CEdge;
import environment.CGraph;
import environment.CNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class Go {

    static CGraph g;

    public static void graphInit() throws IOException {
        g=new CGraph();
        FileReader fr = new FileReader("Edges.txt");
        BufferedReader bf = new BufferedReader(fr);
        String text = bf.readLine();
        while (text!=null){
            String del = "[ ]";
            String[] tokens = text.split(del);
            CNode n1= (CNode) g.getNode(Integer.valueOf(tokens[0]));
            CNode n2 = (CNode) g.getNode(Integer.valueOf(tokens[1]));
            CEdge e1 = new CEdge();
            CEdge e2 = new CEdge();
            if ((n1!=null) && (n2!=null)){
                g.addEdge(n1, n2, e1);
                g.addEdge(n2, n1, e2);
            }
            else if (n1!=null){
                CNode c2 = new CNode(Integer.valueOf(tokens[1]));
                g.addEdge(n1,c2,e1);
                g.addEdge(c2,n1,e2);
            }
            else if(n2!=null){
                CNode c1 = new CNode(Integer.valueOf(tokens[0]));
                g.addEdge(c1, n2, e1);
                g.addEdge(n2,c1,e2);
            }
            else{
                CNode c1 = new CNode(Integer.valueOf(tokens[0]));
                CNode c2 = new CNode(Integer.valueOf(tokens[1]));
                g.addEdge(c1, c2, e1);
                g.addEdge(c2,c1,e2);
            }
            text=bf.readLine();
        }
        bf.close();
    }

    public static Collection<CNode> genPOI(int n){
        ArrayList<CNode> col = new ArrayList<>();
        for(int i=0;i<n; i++){
            int val=(int)(Math.random()*(g.countNodes()+1));
            col.add((CNode)g.getNode(val));
        }
        return col;
    }

    public static void main(String args[]) throws IOException {
        graphInit();
        Collection<CNode> pois=genPOI(6);



    }
}
