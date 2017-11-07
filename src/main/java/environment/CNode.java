package environment;

public class CNode implements INode<Integer> {

    private Integer id;

    public CNode(int i){
        id=i;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public boolean equals(Integer integer) {
        if(id==integer) return true;
        else return false;
    }

}
