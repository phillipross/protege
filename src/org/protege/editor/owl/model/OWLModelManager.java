package org.protege.editor.owl.model;

import org.protege.editor.core.ModelManager;
import org.protege.editor.owl.model.entity.OWLEntityFactory;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.model.find.EntityFinder;
import org.protege.editor.owl.model.hierarchy.OWLHierarchyManager;
import org.protege.editor.owl.model.history.HistoryManager;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.io.IOListener;
import org.protege.editor.owl.model.library.OntologyLibraryManager;
import org.protege.editor.owl.model.selection.ontologies.OntologySelectionStrategy;
import org.protege.editor.owl.ui.clsdescriptioneditor.OWLExpressionCheckerFactory;
import org.protege.editor.owl.ui.error.OntologyLoadErrorHandler;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLObjectRenderer;
import org.semanticweb.owlapi.inference.OWLReasoner;
import org.semanticweb.owlapi.model.*;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 19-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public interface OWLModelManager extends ModelManager {

    public static final String OWL_OBJECT_COMPARATOR_KEY = "OWL_OBJECT_COMPARATOR_KEY";


    void addListener(OWLModelManagerListener listener);

    void removeListener(OWLModelManagerListener listener);

    void fireEvent(EventType event);

    void addIOListener(IOListener listener);

    void removeIOListener(IOListener listener);

    /**
     * Creates a new, empty ontology that has the specified
     * ontology ID - i.e. the name of the ontology.
     */
    OWLOntology createNewOntology(OWLOntologyID ontologyID, URI physicalURI) throws OWLOntologyCreationException;


    OWLOntology reload(OWLOntology ont) throws OWLOntologyCreationException;


    /**
     * Remove the given ontology from the model manager.
     * Cannot remove the last ontology from the model manager.
     *
     * @param ont the ontology to remove
     * @return false if the ontology cannot be removed
     * (eg if it does not exist or is the last open ontology)
     */
    boolean removeOntology(OWLOntology ont);


    /**
     * Performs a save operation.  The behaviour of this is implementation
     * specific.  For example, some implementations may choose to save the
     * active ontology, other implementations may choose to save all open
     * ontologies etc.
     * @throws org.semanticweb.owlapi.model.OWLOntologyStorageException if a problem occurs during the save
     */
    void save() throws OWLOntologyStorageException;


    /**
     * Save only the ontology specified
     * @param ont the ontology to save
     * @throws org.semanticweb.owlapi.model.OWLOntologyStorageException if a problem occurs during the save
     */
    void save(OWLOntology ont) throws OWLOntologyStorageException;


    /**
     * @deprecated - this method would require user interaction - use <code>OWLEditorKit.saveAs()</code> instead
     * @throws OWLOntologyStorageException if a problem occurs during the save
     */
    void saveAs() throws OWLOntologyStorageException;


    /**
     * Gets the ontologies that are loaded into this model.
     * These are usually ontologies that are the imports closure
     * of some base ontology.  For example, if OntA imports OntB
     * and OntC then the collection of returned ontology will
     * typically contain OntA, OntB and OntC.
     * @return A <code>Set</code> of open <code>OWLOntology</code>
     *         objects.
     */
    Set<OWLOntology> getOntologies();


    /**
     * Gets the set of loaded ontologies that have been edited
     * but haven't been saved.
     * @return A <code>Set</code> of <code>OWLOntology</code>
     *         objects
     */
    Set<OWLOntology> getDirtyOntologies();


    /**
     * Forces the system to believe that an ontology
     * has been modified.
     * @param ontology The ontology to be made dirty.
     */
    void setDirty(OWLOntology ontology);


    /**
     * Gets the active ontology.  This is the ontology that
     * edits that cause information to be added to an ontology
     * usually take place in.
     */
    OWLOntology getActiveOntology();


    /**
     * Gets the ontologies that are in the imports
     * closure of the the active ontology.
     * @return A <code>Set</code> of <code>OWLOntologies</code>
     */
    Set<OWLOntology> getActiveOntologies();


    /**
     * A convenience method that determines if the active
     * ontology is mutable.
     * @return <code>true</code> if the active ontology is mutable
     *         or <code>false</code> if the active ontology isn't mutable.
     */
    boolean isActiveOntologyMutable();


    /**
     * Determines if the specified ontology is mutable.
     * @param ontology The ontology to be tested/
     * @return <code>true</code> if the ontology is mutable
     *         and can be edited, <code>false</code> if the ontology
     *         is not mutable i.e. can't be edited.
     */
    boolean isMutable(OWLOntology ontology);


    /**
     * Gets the ontology library manager which is an interface
     * for a repository of "standard"/frequently used ontologies (e.g. upper
     * ontologies).
     */
    public OntologyLibraryManager getOntologyLibraryManager();


    OWLHierarchyManager getOWLHierarchyManager();


    void setActiveOntology(OWLOntology activeOntology);


    void setActiveOntologiesStrategy(OntologySelectionStrategy strategy);


    OntologySelectionStrategy getActiveOntologiesStrategy();


    Set<OntologySelectionStrategy> getActiveOntologiesStrategies();


    ////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Change
    //
    ////////////////////////////////////////////////////////////////////////////////////////////


    void applyChange(OWLOntologyChange change);


    void applyChanges(List<? extends OWLOntologyChange> changes);


    boolean isChangedEntity(OWLEntity entity);


    /**
     * Gets the change history manager.  This tracks the changes that have
     * been made to various ontologies and has support for undo and redo.
     */
    public HistoryManager getHistoryManager();


    /**
     * Adds an ontology history listener.  The listener will be notified of
     * any changes to any of the ontologies that are managed by this model
     * manager.
     */
    void addOntologyChangeListener(OWLOntologyChangeListener listener);


    /**
     * Removes a previously added listener.
     */
    void removeOntologyChangeListener(OWLOntologyChangeListener listener);


    OWLModelManagerEntityRenderer getOWLEntityRenderer();


    void setOWLEntityRenderer(OWLModelManagerEntityRenderer renderer);


    OWLObjectRenderer getOWLObjectRenderer();


    OWLExpressionCheckerFactory getOWLExpressionCheckerFactory();


    EntityFinder getEntityFinder();


    <T extends OWLObject> Comparator<T> getOWLObjectComparator();


    OWLReasonerManager getOWLReasonerManager();


    OWLReasoner getReasoner();


    URI getOntologyPhysicalURI(OWLOntology ontology);


    void setPhysicalURI(OWLOntology ontology, URI physicalURI);


    OWLEntityFactory getOWLEntityFactory();


    void setOWLEntityFactory(OWLEntityFactory owlEntityFactory);


    OWLOntologyManager getOWLOntologyManager();


    /**
     * Gets the data factory for the active ontology
     */
    OWLDataFactory getOWLDataFactory();


    String getRendering(OWLObject object);


    void setMissingImportHandler(MissingImportHandler handler);


    void setSaveErrorHandler(SaveErrorHandler handler);


    void setLoadErrorHandler(OntologyLoadErrorHandler handler);
}
