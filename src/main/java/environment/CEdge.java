package environment;

public class CEdge implements IEdge {

    private double weight;

    private String about;

    public CEdge(){
        weight=1;
    }

    public void setAbout(CNode n1, CNode n2){
        about=n1.id()+" "+n2.id();
    }
    //@Override
    public double weight() {
        return weight;
    }

    public String about(){
        return about;
    }


}
