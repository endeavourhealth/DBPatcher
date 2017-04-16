
package org.endeavourhealth.dbpatcher.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}CreateDatabaseIfNotExists"/>
 *         &lt;element ref="{}ApplySchema"/>
 *         &lt;element ref="{}DropFunctions"/>
 *         &lt;element ref="{}ApplyFunctions"/>
 *         &lt;element ref="{}DropTriggers"/>
 *         &lt;element ref="{}ApplyTriggers"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "createDatabaseIfNotExists",
    "applySchema",
    "dropFunctions",
    "applyFunctions",
    "dropTriggers",
    "applyTriggers"
})
@XmlRootElement(name = "DBPatcherActions")
public class DBPatcherActions {

    @XmlElement(name = "CreateDatabaseIfNotExists", required = true)
    protected CreateDatabaseIfNotExists createDatabaseIfNotExists;
    @XmlElement(name = "ApplySchema", required = true)
    protected ApplySchema applySchema;
    @XmlElement(name = "DropFunctions", required = true)
    protected DropFunctions dropFunctions;
    @XmlElement(name = "ApplyFunctions", required = true)
    protected ApplyFunctions applyFunctions;
    @XmlElement(name = "DropTriggers", required = true)
    protected DropTriggers dropTriggers;
    @XmlElement(name = "ApplyTriggers", required = true)
    protected ApplyTriggers applyTriggers;

    /**
     * Gets the value of the createDatabaseIfNotExists property.
     * 
     * @return
     *     possible object is
     *     {@link CreateDatabaseIfNotExists }
     *     
     */
    public CreateDatabaseIfNotExists getCreateDatabaseIfNotExists() {
        return createDatabaseIfNotExists;
    }

    /**
     * Sets the value of the createDatabaseIfNotExists property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateDatabaseIfNotExists }
     *     
     */
    public void setCreateDatabaseIfNotExists(CreateDatabaseIfNotExists value) {
        this.createDatabaseIfNotExists = value;
    }

    /**
     * Gets the value of the applySchema property.
     * 
     * @return
     *     possible object is
     *     {@link ApplySchema }
     *     
     */
    public ApplySchema getApplySchema() {
        return applySchema;
    }

    /**
     * Sets the value of the applySchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplySchema }
     *     
     */
    public void setApplySchema(ApplySchema value) {
        this.applySchema = value;
    }

    /**
     * Gets the value of the dropFunctions property.
     * 
     * @return
     *     possible object is
     *     {@link DropFunctions }
     *     
     */
    public DropFunctions getDropFunctions() {
        return dropFunctions;
    }

    /**
     * Sets the value of the dropFunctions property.
     * 
     * @param value
     *     allowed object is
     *     {@link DropFunctions }
     *     
     */
    public void setDropFunctions(DropFunctions value) {
        this.dropFunctions = value;
    }

    /**
     * Gets the value of the applyFunctions property.
     * 
     * @return
     *     possible object is
     *     {@link ApplyFunctions }
     *     
     */
    public ApplyFunctions getApplyFunctions() {
        return applyFunctions;
    }

    /**
     * Sets the value of the applyFunctions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplyFunctions }
     *     
     */
    public void setApplyFunctions(ApplyFunctions value) {
        this.applyFunctions = value;
    }

    /**
     * Gets the value of the dropTriggers property.
     * 
     * @return
     *     possible object is
     *     {@link DropTriggers }
     *     
     */
    public DropTriggers getDropTriggers() {
        return dropTriggers;
    }

    /**
     * Sets the value of the dropTriggers property.
     * 
     * @param value
     *     allowed object is
     *     {@link DropTriggers }
     *     
     */
    public void setDropTriggers(DropTriggers value) {
        this.dropTriggers = value;
    }

    /**
     * Gets the value of the applyTriggers property.
     * 
     * @return
     *     possible object is
     *     {@link ApplyTriggers }
     *     
     */
    public ApplyTriggers getApplyTriggers() {
        return applyTriggers;
    }

    /**
     * Sets the value of the applyTriggers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ApplyTriggers }
     *     
     */
    public void setApplyTriggers(ApplyTriggers value) {
        this.applyTriggers = value;
    }

}
