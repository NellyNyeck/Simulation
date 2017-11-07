package environment;

public interface INode<T>
{
    T id();

    @Override
    boolean equals(T t);
}
