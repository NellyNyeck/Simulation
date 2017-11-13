package transformers;

public interface ITransformer< E, Number >
{
    Number transform(E e);
}
