package environment;

/**
 * The interface for any implementation of Node/Vertex
 * @param <T> the generic for the node id
 */
public interface INode<T>
{
    T name();

    Double firstCoord();

    Double secondCoord();

}
