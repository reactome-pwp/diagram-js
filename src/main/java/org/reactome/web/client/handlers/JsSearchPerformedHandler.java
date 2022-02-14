package org.reactome.web.client.handlers;

import org.reactome.web.client.model.JsSearchArguments;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;

@ExportClosure
public interface JsSearchPerformedHandler extends Exportable {
    void onSearchPerformed(JsSearchArguments arguments);
}
