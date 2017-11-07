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
            if ((n1!=null) && (n2!=null)){

            }
        }



    }
}
