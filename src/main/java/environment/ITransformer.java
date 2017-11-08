package environment;

public interface ITransformer<E extends IEdge, Number> {
    Number transform(E e);
}
