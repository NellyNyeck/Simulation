package environment;

public interface Transformer<E extends IEdge, Number> {
    Number transform(E e);
}
