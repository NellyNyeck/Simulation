package transformers;

import environment.IEdge;

/**
 * The transformer class needed for weighted Dijkstra
 * @param <E> the edge
 * @param <D> the weight of the edge
 */
public class CTransformer<E extends IEdge, D> implements ITransformer<E, Number>
{

    /**
     * the function to return the edges weight
     * @param e the edge in question
     * @return it's weight
     */
    @Override
    public Number transform( final E e )
    {
        return e.weight();
    }
}
