package org.reactome.web.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import org.reactome.web.diagram.search.SearchArguments;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

@Export()
@ExportPackage("Reactome")
public class JsSearchArguments extends JavaScriptObject implements Exportable {
    protected JsSearchArguments() {

    }

    public static JsSearchArguments create(SearchArguments searchArguments) {
        JsSearchArguments object = (JsSearchArguments) JavaScriptObject.createObject();
        object.setQuery(searchArguments.getQuery());
        object.setDiagramStId(searchArguments.getDiagramStId());
        object.setSpecies(searchArguments.getSpecies());
        searchArguments.getFacets().forEach(object::addFacet);
        searchArguments.getTerms().forEach(object::addTerm);
        object.setFacetsScope(searchArguments.getFacetsScope());
        return object;
    }

    private native void setQuery(String query) /*-{
        this.query = query;
    }-*/;

    private native void setDiagramStId(String diagramStId) /*-{
        this.diagramStId = diagramStId;
    }-*/;

    private native void setSpecies(String species) /*-{
        this.species = species;
    }-*/;

    private native void setFacetsScope(int facetsScope) /*-{
        this.facetsScope = facetsScope;
    }-*/;

    private native void addFacet(String facet) /*-{
        if (!this.facets) this.facets = [];
        this.facets.push(facet);
    }-*/;

    private native void addTerm(String term) /*-{
        if (!this.terms) this.terms = [];
        this.terms.push(term);
    }-*/;
}
