package environment;

public class CEdge implements IEdge {

    private double weight;

    private String about;

    public CEdge(String s){
        about=s;
        weight=1;
    }
    //@Override
    public double weight() {
        return weight;
    }

    public String about(){
        return about;
    }


}
