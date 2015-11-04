package org.reactome.web.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.reactome.web.client.handlers.*;
import org.reactome.web.client.model.DiagramObject;
import org.reactome.web.client.model.JsProperties;
import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.data.graph.model.GraphObject;
import org.reactome.web.diagram.events.*;
import org.reactome.web.diagram.handlers.*;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;


/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
@SuppressWarnings("unused")
@ExportPackage("Reactome")
@Export("Diagram")
public class Diagram implements Exportable {

    private static final String SERVER = "//www.reactome.org";
    private static DiagramViewer diagram;
    private static DiagramLoader loader;

    private Diagram() {
    }

    public static Diagram create(JavaScriptObject input) {
        JsProperties jsProp = new JsProperties(input);
        return create(jsProp.get("placeHolder"), jsProp.get("proxyPrefix", ""), jsProp.getInt("width", 500), jsProp.getInt("height", 400));
    }

    public static Diagram create(String placeHolder, int width, int height) {
        return create(placeHolder, "", width, height);
    }

    public static Diagram create(String placeHolder, String server, final int width, final int height) {
        final Element element = Document.get().getElementById(placeHolder);
        if (element == null)
            throw new RuntimeException("Reactome diagram cannot be initialised. Please provide a valid 'placeHolder' (\"" + placeHolder + "\" invalid place holder).");

        if (diagram == null) {
            RESTFulClient.SERVER = server;
            DiagramFactory.SERVER = server;
            DiagramFactory.ILLUSTRATION_SERVER = SERVER;
            DiagramFactory.SHOW_FIREWORKS_BTN = false;
            diagram = DiagramFactory.createDiagramViewer();
            diagram.asWidget().getElement().getStyle().setProperty("height", "inherit");
            loader = new DiagramLoader(diagram);
        }

        HTMLPanel holder = HTMLPanel.wrap(element);
        diagram.asWidget().removeFromParent();
        holder.add(diagram);

        final Diagram viewer = new Diagram();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                viewer.resize(width, height);
            }
        });

        return viewer;
    }

    public void flagItems(String term) {
        diagram.flagItems(term);
    }

    public void highlightItem(String stableIdentifier) {
        diagram.highlightItem(stableIdentifier);
    }

    public void highlightItem(Long dbIdentifier) {
        diagram.highlightItem(dbIdentifier);
    }

    public void loadDiagram(String stId) {
        loader.load(stId);
    }

    public void onAnalysisReset(final JsAnalysisResetHandler handler) {
        diagram.addAnalysisResetHandler(new AnalysisResetHandler() {
            @Override
            public void onAnalysisReset(AnalysisResetEvent event) {
                handler.analysisReset();
            }
        });
    }

    public void onCanvasNotSupported(final JsCanvasNotSupported handler) {
        diagram.addCanvasNotSupportedEventHandler(new CanvasNotSupportedHandler() {
            @Override
            public void onCanvasNotSupported(CanvasNotSupportedEvent event) {
                handler.canvasNotSupported();
            }
        });
    }


    public void onDiagramLoaded(final JsDiagramLoadedHandler handler) {
        loader.addSubpathwaySelectedHandler(new DiagramLoader.SubpathwaySelectedHandler() {
            @Override
            public void onSubPathwaySelected(String identifier) {
                handler.loaded(identifier);
            }
        });
        diagram.addDiagramLoadedHandler(new DiagramLoadedHandler() {
            @Override
            public void onDiagramLoaded(DiagramLoadedEvent event) {
                if (loader.getTarget() == null) {
                    handler.loaded(event.getContext().getContent().getStableId());
                }
            }
        });
    }

    public void onFlagsReset(final JsFlagsResetHandler handler) {
        diagram.addDiagramObjectsFlagResetHandler(new DiagramObjectsFlagResetHandler() {
            @Override
            public void onDiagramObjectsFlagReset(DiagramObjectsFlagResetEvent event) {
                handler.flagsReset();
            }
        });
    }

    public void onObjectSelected(final JsGraphObjectSelectedHandler handler) {
        diagram.addDatabaseObjectSelectedHandler(new GraphObjectSelectedHandler() {
            @Override
            public void onGraphObjectSelected(GraphObjectSelectedEvent event) {
                GraphObject object = event.getGraphObject();
                handler.selected(object == null ? null : DiagramObject.create(object));
            }
        });
    }


    public void onObjectHovered(final JsGraphObjectHoveredHandler handler) {
        diagram.addDatabaseObjectHoveredHandler(new GraphObjectHoveredHandler() {
            @Override
            public void onGraphObjectHovered(GraphObjectHoveredEvent event) {
                GraphObject object = event.getGraphObject();
                handler.hovered(object == null ? null : DiagramObject.create(object));
            }
        });
    }

    public void resetAnalysis() {
        diagram.resetAnalysis();
    }

    public void resetFlaggedItems() {
        diagram.resetFlaggedItems();
    }

    public void resetHighlight() {
        diagram.resetHighlight();
    }


    public void resetSelection() {
        diagram.resetSelection();
    }

    public void resize(int width, int height) {
        diagram.asWidget().setWidth(width + "px");
        diagram.asWidget().setHeight(height + "px");
        diagram.onResize();
    }


    public void selectItem(String stableIdentifier) {
        diagram.selectItem(stableIdentifier);
    }


    public void selectItem(Long dbIdentifier) {
        diagram.selectItem(dbIdentifier);
    }


    public void setAnalysisToken(String token, String resource) {
        diagram.setAnalysisToken(token, resource);
    }
}
