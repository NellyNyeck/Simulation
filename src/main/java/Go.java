import Output.CSVWriter;
import environment.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Go {

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

    static CGraph g;

    static CSVWriter out;

    public static void graphInit() throws IOException
    {
        g=new CGraph();
        FileReader fr = new FileReader("Edges.txt");
        BufferedReader bf = new BufferedReader(fr);
        String text = bf.readLine();
        int nodes=Integer.valueOf(text);
        for(int i=0;i<nodes;i++){
            CNode n=new CNode(i);
            g.addNode(n);
        }
        text=bf.readLine();
        while (text!=null){
            String del = "[ ]";
            String[] tokens = text.split(del);
            CNode n1= (CNode) g.getNode(Integer.valueOf(tokens[0]));
            CNode n2 = (CNode) g.getNode(Integer.valueOf(tokens[1]));
            CEdge e1 = new CEdge(tokens[0]+" "+tokens[1]);
            CEdge e2 = new CEdge(tokens[1]+" "+tokens[0]);
            g.addEdge(n1, n2, e1);
            g.addEdge(n2,n1,e2);
            text=bf.readLine();
        }
        bf.close();
    }

    public static Collection<CPOI> genPOI(int n){
        Set<CPOI> col = new HashSet<>();
        while (col.size()<n){
            int val=(int)(Math.random()*(g.countNodes()));
            if(val!=0){
                CPOI p = new CPOI(g.getNode(val));
                col.add(p);
            }
        }
        return col;
    }

    public static ArrayList<List> routing(Collection<CPOI> pois){
        ArrayList<List> routes=new ArrayList<List>();
        for(CPOI p : pois){
            List<CEdge> r = g.route(g.getNode(0),p.id());
            routes.add(r);
        }
        return routes;
    }

    public static ArrayList<PEdge> countPlat(ArrayList<List> routes ){
        ArrayList<PEdge> platoons = new ArrayList<>();
        for(List r : routes){
            for(int i=0; i<r.size(); i++){
                CEdge comp = (CEdge) r.get(i);
                boolean check = false;
                PEdge p = new PEdge(comp);
                for(PEdge plat : platoons){
                    if (p.getAbout().equals(plat.getAbout())){
                        plat.add();
                        check=true;
                    }
                }
                if (check==false) platoons.add(p);
            }
        }
        return platoons;
    }

    public static void doTheThing(){
        out=new CSVWriter("ResultsDijkstra.csv");
        int count=0;
        while(count<10000){
            Collection<CPOI> pois=genPOI(6);
            ArrayList<List> routes=routing(pois);
            ArrayList<PEdge> plat=countPlat(routes);
            out.writeCsvFile(plat);
            out.writeNewLine();
            count++;
        }
        out.done();
        System.out.println(count);
    }
    public static void main(String args[]) throws IOException {
        graphInit();
        doTheThing();
    }
}
