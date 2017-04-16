
package org.endeavourhealth.dbpatcher.configuration;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.endeavourhealth.dbpatcher.configuration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.endeavourhealth.dbpatcher.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ApplyFunctions }
     * 
     */
    public ApplyFunctions createApplyFunctions() {
        return new ApplyFunctions();
    }

    /**
     * Create an instance of {@link ApplySchema }
     * 
     */
    public ApplySchema createApplySchema() {
        return new ApplySchema();
    }

    /**
     * Create an instance of {@link Database }
     * 
     */
    public Database createDatabase() {
        return new Database();
    }

    /**
     * Create an instance of {@link DBPatcherActions }
     * 
     */
    public DBPatcherActions createDBPatcherActions() {
        return new DBPatcherActions();
    }

    /**
     * Create an instance of {@link CreateDatabaseIfNotExists }
     * 
     */
    public CreateDatabaseIfNotExists createCreateDatabaseIfNotExists() {
        return new CreateDatabaseIfNotExists();
    }

    /**
     * Create an instance of {@link DropFunctions }
     * 
     */
    public DropFunctions createDropFunctions() {
        return new DropFunctions();
    }

    /**
     * Create an instance of {@link DropTriggers }
     * 
     */
    public DropTriggers createDropTriggers() {
        return new DropTriggers();
    }

    /**
     * Create an instance of {@link ApplyTriggers }
     * 
     */
    public ApplyTriggers createApplyTriggers() {
        return new ApplyTriggers();
    }

}
