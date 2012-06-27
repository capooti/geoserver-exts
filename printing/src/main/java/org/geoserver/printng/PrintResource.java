package org.geoserver.printng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.MediaTypes;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StreamRepresentation;
import org.restlet.resource.Variant;

/**
 * Base support class for print endpoints
 * @author Ian Schneider <ischneider@opengeo.org>
 */
public abstract class PrintResource extends Resource {

    RenderingSupport renderingSupport;

    PrintResource(Request req, Response resp) {
        super(null, req, resp);
    }
    
    protected File getGeoserverDirectory(boolean create,String... path) {
        GeoServerDataDirectory dataDir = GeoServerExtensions.bean(GeoServerDataDirectory.class);
        String[] parts = new String[path.length + 1];
        parts[0] = "printing2";
        System.arraycopy(path, 0, parts, 1, path.length);
        try {
            if (create) {
                return dataDir.findOrCreateDir(parts);
            } else {
                return dataDir.findFile(parts);
            }
        } catch (IOException ex) {
            throw new RestletException("Error getting data dir subdirectories", Status.SERVER_ERROR_INTERNAL, ex);
        }
    }
    
    protected RenderingSupport renderingSupport() {
        if (renderingSupport == null) {
            // @todo cache directory ?
            GeoServerDataDirectory dataDir = GeoServerExtensions.bean(GeoServerDataDirectory.class);
            File imageCache = getGeoserverDirectory(true,"imageCache");
            renderingSupport = new RenderingSupport(imageCache);
        }
        return renderingSupport;
    }
    
    protected String initVariants(String fname,MediaType defaultType) {
        int idx = fname.lastIndexOf('.');
        String extension = idx > 0 ? fname.substring(idx + 1) : null;
        fname = idx > 0 ? fname.substring(0, idx) : fname;
        MediaType mediaType = defaultType == null ? MediaType.TEXT_HTML : defaultType;
        if (extension != null) {
            mediaType = MediaTypes.getMediaTypeForExtension(extension);
        }
        if (mediaType != null) {
            getVariants().add(new Variant(mediaType));
        }
        return fname;
    }

    protected Representation getPDFRepresentation() {
        return new StreamRepresentation(MediaType.APPLICATION_PDF) {

            @Override
            public InputStream getStream() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void write(OutputStream outputStream) throws IOException {
                renderingSupport.renderPDF(outputStream);
            }
        };
    }

    protected Representation getImageRepresentation(MediaType type,final String ext, final int width, final int height) {
        return new StreamRepresentation(type) {

            @Override
            public InputStream getStream() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void write(OutputStream outputStream) throws IOException {
                renderingSupport.renderImage(outputStream, ext, width, height); 
            }
            
        };
    }
}
