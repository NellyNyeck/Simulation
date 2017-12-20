package objects;

/**
 * interface for the  pod vehicles
 */
public interface IPOD<I>
{
    I id();

    Number capacity();

    String provider();

    String strategy();
}
