package transformers;

/**
 * the interface to build the transformer class
 * @param <E> the edge entity
 * @param <Number> the edges weight
 */
public interface ITransformer<E, Number>
{
    Number transform( E e );
}
