package environment;

public class PEdge implements IEdge {

    private String about;

    private int counter;

    public PEdge(CEdge edge){
        about= edge.about();
        counter=1;
    }

    public int getCounter(){
        return counter;
    }

    public String getAbout() {
        return about;
    }

    public void add(){
        counter++;
    }

}
