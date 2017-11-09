import Output.CSVWriter;
import environment.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Go {

    static CGraph g;

    static CSVWriter out;

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
                e1.setAbout(n1, n2);
                e2.setAbout(n2,n1);
                g.addEdge(n1, n2, e1);
                g.addEdge(n2, n1, e2);
            }
            else if (n1!=null){
                CNode c2 = new CNode(Integer.valueOf(tokens[1]));
                e1.setAbout(n1, c2);
                e2.setAbout(c2,n1);
                g.addEdge(n1,c2,e1);
                g.addEdge(c2,n1,e2);
            }
            else if(n2!=null){
                CNode c1 = new CNode(Integer.valueOf(tokens[0]));
                e1.setAbout(c1,n2);
                e2.setAbout(n2,c1);
                g.addEdge(c1, n2, e1);
                g.addEdge(n2,c1,e2);
            }
            else{
                CNode c1 = new CNode(Integer.valueOf(tokens[0]));
                CNode c2 = new CNode(Integer.valueOf(tokens[1]));
                e1.setAbout(c1,c2);
                e2.setAbout(c2,c1);
                g.addEdge(c1, c2, e1);
                g.addEdge(c2,c1,e2);
            }
            text=bf.readLine();
        }
        bf.close();
    }

    public static Collection<CPOI> genPOI(int n){
        Set<CPOI> col = new HashSet<>();
        for(int i=0;i<n; i++){
            int val=(int)(Math.random()*(g.countNodes()+1));
            if(val!=0){
                CPOI p = new CPOI(g.getNode(val));
                col.add(p);
            }
            else i--;
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
            count++;
        }
        out.done();
    }
    public static void main(String args[]) throws IOException {
        graphInit();
        doTheThing();
        /*
        Collection<CPOI> pois=genPOI(6);
        ArrayList<List> routes=routing(pois);
        int i=0;
        for (CPOI p : pois){
            CNode dest = (CNode) p.id();
            System.out.println("For POI " + dest.id() + " the route is: ");
            List<CEdge> rou = routes.get(i);
            for(CEdge e : rou){
                System.out.print("(" + e.about()+ ") " );
            }
            System.out.println();
            i++;
        }
        ArrayList<PEdge> plat=countPlat(routes);
        System.out.println("The edges on which platooning can occur are: ");
        for(PEdge p : plat){
            if (p.getCounter()>1)   System.out.println( "( " + p.getAbout() + ") " + p.getCounter());
        }
        out=new CSVWriter("ResultsDijkstra.csv");
        out.writeCsvFile(plat);
        out.done();*/
    }
}
