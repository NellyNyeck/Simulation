package environment;

import com.sun.istack.internal.NotNull;

import java.util.List;

public interface IGraph<I, V extends INode<I>, E extends IEdge>
{
    List<E> route(@NotNull final I p_start, @NotNull final I p_end );

}
