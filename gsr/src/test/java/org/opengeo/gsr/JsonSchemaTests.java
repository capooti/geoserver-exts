package org.opengeo.gsr;

import java.io.File;

import org.opengeo.gsr.core.format.GeoServicesJsonFormat;
import org.opengeo.gsr.validation.JSONValidator;

import com.thoughtworks.xstream.XStream;

public class JsonSchemaTests {

    private static XStream xstream;

    public JsonSchemaTests() {
        GeoServicesJsonFormat jsonFormat = new GeoServicesJsonFormat();
        JsonSchemaTests.xstream = jsonFormat.getXStream();
    }

    public static String getJson(Object obj) {
        return xstream.toXML(obj);
    }
    
    protected boolean validateJSON(String json, String schemaPath) {
        String workingDir = System.getProperty("user.dir") + "/src/test/resources/schemas/";
        return JSONValidator.isValidSchema(json, new File(workingDir + schemaPath));
    }
}
