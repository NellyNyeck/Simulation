import environment.CEdge;
import environment.CGraph;
import environment.CNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Go {

    public static void main(String args[]) throws IOException {
        CGraph g=new CGraph();
        FileReader fr = new FileReader("Edges.txt");
        BufferedReader bf = new BufferedReader(fr);
        String text = bf.readLine();
        while (text!=null){
            String del = "[ ]";
            String[] tokens = text.split(del);
            CNode c1 = new CNode(Integer.valueOf(tokens[0]));
            CNode c2 = new CNode(Integer.valueOf(tokens[1]));
            CNode n1= (CNode) g.getNode(c1);
            CNode n2 = (CNode) g.getNode(c2);
            CEdge e1 = new CEdge();
            CEdge e2 = new CEdge();
            if ((n1!=null) && (n2!=null)){
                g.addEdge(n1, n2, e1);
                g.addEdge(n2, n1, e2);
            }
            else if (n1!=null){
                g.addEdge(n1,c2,e1);
                g.addEdge(c2,n1,e2);
            }
            else if(n2!=null){
                g.addEdge(c1, n2, e1);
                g.addEdge(n2,c1,e2);
            }
            else{
                g.addEdge(c1, c2, e1);
                g.addEdge(c2,c1,e2);
            }
            text=bf.readLine();
        }
        bf.close();
    }
}
