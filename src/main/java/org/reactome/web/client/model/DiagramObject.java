package org.reactome.web.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import org.reactome.web.diagram.data.graph.model.GraphObject;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@Export()
@ExportPackage("Reactome")
public class DiagramObject extends JavaScriptObject implements Exportable  {

    protected DiagramObject() {
    }

    public static DiagramObject create(GraphObject graphObject){
        DiagramObject obj = (DiagramObject) JavaScriptObject.createObject();
        obj.setStId(graphObject.getStId());
        obj.setDisplayName(graphObject.getDisplayName());
        obj.setSchemaClass(graphObject.getSchemaClass().name());
        return obj;
    }

    private native void setStId(String stId) /*-{
        this.stId = stId;
    }-*/;

    private native void setDisplayName(String displayName) /*-{
        this.displayName = displayName;
    }-*/;

    private native void setSchemaClass(String schemaClass) /*-{
        this.schemaClass = schemaClass;
    }-*/;

}
