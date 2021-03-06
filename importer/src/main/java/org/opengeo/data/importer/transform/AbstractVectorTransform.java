package org.opengeo.data.importer.transform;

import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

public abstract class AbstractVectorTransform implements VectorTransform {

    private static final long serialVersionUID = 1L;
    
    final transient Logger LOGGER = Logging.getLogger(getClass());
    
    public boolean stopOnError(Exception e) {
        return true;
    }
    
    /**
     * Make subclassing less onerous. If an implementation has temporary or transient state,
     * this method allows a hook to create that.
     */
    public void init() {
        // do nothing
    }

}
