package org.reactome.web.client;

import com.google.gwt.core.client.Scheduler;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.events.DiagramLoadedEvent;
import org.reactome.web.diagram.handlers.DiagramLoadedHandler;
import org.reactome.web.diagram.util.Console;
import org.reactome.web.pwp.model.classes.DatabaseObject;
import org.reactome.web.pwp.model.classes.Event;
import org.reactome.web.pwp.model.client.RESTFulClient;
import org.reactome.web.pwp.model.client.handlers.AncestorsCreatedHandler;
import org.reactome.web.pwp.model.factory.DatabaseObjectFactory;
import org.reactome.web.pwp.model.handlers.DatabaseObjectCreatedHandler;
import org.reactome.web.pwp.model.util.Ancestors;
import org.reactome.web.pwp.model.util.Path;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Antonio Fabregat <fabregat@ebi.ac.uk>
 */
public class DiagramLoader implements DatabaseObjectCreatedHandler, AncestorsCreatedHandler, DiagramLoadedHandler {

    public interface SubpathwaySelectedHandler {
        void onSubPathwaySelected(String identifier);
    }

    private Set<SubpathwaySelectedHandler> handlers = new HashSet<>();
    private final DiagramViewer diagram;
    private String target;

    public DiagramLoader(DiagramViewer diagram) {
        this.diagram = diagram;
        this.diagram.addDiagramLoadedHandler(this);
    }

    public void load(String identifier) {
        DatabaseObjectFactory.get(identifier, this);
    }

    public String getTarget() {
        return target;
    }

    public void addSubpathwaySelectedHandler(SubpathwaySelectedHandler handler) {
        handlers.add(handler);
    }

    @Override
    public void onDatabaseObjectLoaded(DatabaseObject databaseObject) {
        if (databaseObject instanceof Event) {
            target = databaseObject.getIdentifier();
            Event event = (Event) databaseObject;
            RESTFulClient.getAncestors(event, this);
            return;
        }
        target = null;
        Console.error("The provided identifier is not an 'Event' in Reactome");
    }

    @Override
    public void onDatabaseObjectError(Throwable exception) {
        Console.error(exception.getMessage());
    }

    @Override
    public void onAncestorsLoaded(Ancestors ancestors) {
        if (ancestors != null) {
            Iterator<Path> it = ancestors.iterator();
            if (it.hasNext()) {
                Path path = it.next();
                diagram.loadDiagram(path.getLastPathwayWithDiagram().getIdentifier());
                return;
            }
        }
        Console.error("No ancestors found. Please check whether the provided identifier belongs to a Pathway or Reaction.");
    }

    @Override
    public void onAncestorsError(Throwable exception) {
        Console.error(exception.getMessage());
    }

    @Override
    public void onDiagramLoaded(DiagramLoadedEvent event) {
        String loaded = event.getContext().getContent().getStableId();
        if (!loaded.equals(target)) {
            diagram.selectItem(target);
            for (SubpathwaySelectedHandler handler : handlers) {
                handler.onSubPathwaySelected(target);
            }
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    target = null;
                }
            });
        } else {
            target = null;
        }
    }
}
