package environment;

/**
 * The Interface for any implementation of the edge of the graph
 */
public interface IEdge
{
    String name();

    String from();

    String to();

    Number weight();

    void add();

    void reset();

    int visited();
}
