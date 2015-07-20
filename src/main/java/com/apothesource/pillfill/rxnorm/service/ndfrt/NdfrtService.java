package com.apothesource.pillfill.rxnorm.service.ndfrt;
import com.apothesource.pillfill.rxnorm.datamodel.ndf.*;

import java.io.IOException;

/**
 Services to find and retrieve NDF-RT Concepts Some of the parameters & ordering in this interface are intentionally inconsistent to match the NIH documentation.
 http://rxnav.nlm.nih.gov/NdfrtAPIs.html

 ApiList-

 {@link #findConceptsByID}                  Get the NDF-RT concept for a specified identifier
 {@link #findConceptsByName}                Search the concepts for a specified name and kind
 {@link #getAllInfo}                        Get concept information
 {@link #getChildConcepts}                  Get the child concepts for a specified concept
 {@link #getParentConcepts}                 Get the parent concepts for a specified concept
 {@link #getConceptProperties}              Get the concept property value for a specified concept and property name
 {@link #getConceptsByProperty}             Search for concepts containing the specified property
 {@link #getRelatedConceptsByAssociation}   Get the associations for a specified concept
 {@link #getRelatedConceptsByReverseRole}   Get the concepts related to the specified object concept by role.
 {@link #getRelatedConceptsByRole}          Get the associations for a specified concept
 {@link #getEPCClassOfConcept}	            Get the FDA Established Pharmacologic Classes (EPC) for a concept
 {@link #getRelatedConceptsByRole}          Get the concepts related to the specified concept by role
 {@link #getVAClassMembers}                 Get the members of a VA class
 {@link #getVAClassOfConcept}               Get the VA class for a specified concept

 Bulk Concept Queries (Not Implemented):
 #                  /allconcepts	    Get concept information for specified kinds.

 List Type Queries (Not Implemented Since Values are Included in Related Enum Objects):
 {@link AssociationNames}   Get the association names
 {@link IdTypeNames}        Get the identifier type names
 {@link KindNames}          Get the kind names in the NDF-RT data set
 {@link PropertyNames}      Get the property names in the NDF-RT data set
 {@link RoleNames}          Get the role names in the NDF-RT data set

 #                  /version	        Get the version of the NDF-RT data set

 *
 *
 * @author Michael Ramirez (michael@pillfill.com)
 */
public interface NdfrtService {

    /**
     * This resource returns concept information for a specified concept.
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_getAllInfo
     *
     * @param nui - the ndfrt identifier for the concept
     * @return The concept associated with this NUI
     */
    FullConceptResponse getAllInfo(String nui) throws IOException;

    /**
     * Find the concepts from a given identifier.
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_findConceptsByID
     *
     * @param idType - the type of identifier. See /typeList for valid identifier types.
     * @param idString - the value of the identifier
     */
    GroupConceptResponse findConceptsByID(IdTypeNames idType, String idString) throws IOException;

    /**
     * Get the concept identfier given a concept name and kind. The name must match the NDF-RT name or one of its synonyms.
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_findConceptsByName
     *
     * @param conceptName - the name of the concept
     * @param kindName - (optional) the kind value. If not specified, all kinds are returned. See getKindList for valid values.
     */
    GroupConceptResponse findConceptsByName(String conceptName, KindNames kindName) throws IOException;

    /**
     * Get the child concepts of a specified concept
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_getChildConcepts
     *
     * @param nui the NDF-RT identifier
     * @param transitive (optional, default: false) if false, then only direct child concepts are returned. If true, return all descendant concepts.
     * @return The list of descendant concepts
     * @throws IOException if errors occur communicating with the service
     */
    GroupConceptResponse getChildConcepts(String nui, Boolean transitive) throws IOException;

    /**
     * Get the parent concepts of a specified concept
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_getParentConcepts
     *
     * @param nui the NDF-RT identifier
     * @param transitive (optional, default: false) if false, then only direct child concepts are returned. If true, return all ancestor concepts.
     * @return The list of descendant concepts
     * @throws IOException if errors occur communicating with the service
     */
    GroupConceptResponse getParentConcepts(String nui, Boolean transitive) throws IOException;

    /**
     * Get the property values for a specified concept and property
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_getConceptProperties
     *
     * @param nui the NDF-RT identifier
     * @param propertyName  (optional) the property name. If not specified, all concept properties are returned. See /propertyList example for valid property names
     * @return The named properties associated with this concept
     * @throws IOException if errors occur communicating with the service
     */
    GroupConceptResponse getConceptProperties(String nui, PropertyNames propertyName) throws IOException;

    /**
     * Get the concepts that contain the specified property name and value
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_getConceptsByProperty
     *
     * @param propertyName name of the property. See /propertyList example for valid property names.
     * @param propertyValue the property value
     * @return The list of concepts matching the provided property
     * @throws IOException if errors occur communicating with the service
     */
    GroupConceptResponse getConceptsByProperty(PropertyNames propertyName, String propertyValue) throws IOException;

    /**
     * Get the concepts related to the specified object concept by role. In NDF-RT, the relations between two concepts
     * are defined by roles and are one directional. For example, "Fenofibrate" "may_treat" "Arteriosclerosis" is an
     * example of a relationship in NDF-RT where the role "may_treat" is the relationship between the drug "Fenofibrate"
     * (the subject) and the disease "Arteriosclerosis" (the object). To determine which diseases the drug "Fenofibrate"
     * "may_treat", the resource here specifies the subject and the role.
     *
     * This resource allows the reverse direction, specifying the object and the role. In the example above, this means
     * finding the drugs that "may_treat" "Arteriosclerosis".
     *
     * @param nui the NDF-RT identifier of the object of the role
     * @param roleName the role. See /roleList example for valid role names.
     * @param transitive (default:false) if false, then only the concepts directly associated with the role are returned.
     *                   If true, the concepts directly associated with the role along with the descendants of these
     *                   concepts are returned.
     * @return The list of concepts
     */
    GroupConceptResponse getRelatedConceptsByReverseRole(String nui, RoleNames roleName, boolean transitive);

    /**
     * Get the concepts related to the specified concept by role. In NDF-RT, the relations between two concepts are
     * defined by roles and are one directional. For example, "Fenofibrate" "may_treat" "Arteriosclerosis" is an example
     * of a relationship in NDF-RT where the role "may_treat" is the relationship between the drug "Fenofibrate" (the
     * subject) and the disease "Arteriosclerosis" (the object). To determine which diseases the drug "Fenofibrate"
     * "may_treat", this function specifies the subject and the role.
     *
     * {@link #getRelatedConceptsByReverseRole} allows the reverse direction, by specifying the object and the role. In
     * the example above, this means finding the drugs that "may_treat" "Arteriosclerosis".
     *
     * @param nui the NDF-RT identifier of the object of the role
     * @param roleName the role. See /roleList example for valid role names.
     * @param transitive (default:false) if false, then only the concepts directly associated with the role are returned.
     *                   If true, the concepts directly associated with the role and the descendants of these concepts
     *                   are returned.
     * @return The list of concepts
     */
    GroupConceptResponse getRelatedConceptsByRole(String nui, RoleNames roleName, boolean transitive);

    /**
     * Get the associations for a specified concept
     *
     * @param nui the NDF-RT concept identifier
     * @param assocName (optional) the association name. If not specified, then all associations are returned for the concept.
     *                  See getAssociationList for valid values.
     * @return An array of association structures. Each association structure contains: Association name, Concept
     * identifier (NUI) of the object of the association
     */
    GroupConceptResponse getRelatedConceptsByAssociation(String nui, AssociationNames assocName);

    /**
     * Get the FDA Established Pharmacologic classes (EPC) for a specified drug. The specified drug must be an NDF-RT
     * unique identifier (NUI) of a DRUG_KIND concept.
     *
     * @param nui the NDF-RT identifier
     * @return a list of concepts in this class
     */
    GroupConceptResponse getEPCClassOfConcept(String nui);

    /**
     * Get the members of a VA class
     *
     * @param nui the NDF-RT identifier
     * @return a list of concepts in this class
     */
    GroupConceptResponse getVAClassMembers(String nui);

    /**
     * Get the VA class of a specified concept
     *
     * @param nui the NDF-RT identifier. The identifier must be for a concept that is a VA product (Level = "VA Product")
     * @return a list of concepts in this class
     */
    GroupConceptResponse getVAClassOfConcept(String nui);
}
