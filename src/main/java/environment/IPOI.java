package environment;

import java.util.Collection;

/**
 * the interface for any Point of interest implementation
 */

public interface IPOI extends INode
{
    Collection<ILabel> labels();
}
