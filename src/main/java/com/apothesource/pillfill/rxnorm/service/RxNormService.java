package com.apothesource.pillfill.rxnorm.service;

import com.apothesource.pillfill.rxnorm.datamodel.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * The RxNorm API is a web service for accessing the current RxNorm data set.
 *
 * Created by Michael Ramirez (michael@pillfill.com) on 7/21/15.
 */
public interface RxNormService {

    /**
     * Search for an identifier from another vocabulary and return the RxCUIs of any concepts which have an RxNorm term as a synonym or have that identifier as an attribute. See the /idtypes example for the valid types.
     *
     * @param type   the identifier type. Valid types are listed in the table below:
     * @param id     the identifier
     * @param allSrc an optional field indicating whether all RxCUIs are to be returned. If set to 1, all non-suppressed RxCUIs will be returned, including those which contain no terms from the RxNorm vocabulary. If set to 0 (the default), only RxCUIs which contain a non-suppressed RxNorm vocabulary term will be returned.
     * @return A set of IdGroups associated with the provided identifier
     */
    IdGroupResponse findRxcuiById(IdTypeNames type, String id, int allSrc) throws IOException;

    /**
     * Search for a name in the RxNorm data set and return the RxCUIs of any concepts which have that name as an RxNorm term or as a synonym of an RxNorm term.
     *
     * @param name   - the search string. This must be specified.
     * @param search - a number indicating the type of search to be performed. If set to 0 (the default), exact match search will be performed. If set to 1, a normalized string search will be done. When search = 2, then an exact match search will be done, and if no results are found, a normalized search will be done. For more information on the normalized string search, click <a href="http://rxnav.nlm.nih.gov/RxNormNorm.html">here.</a>
     * @return A set of IdGroups associated with the provided name
     */
    IdGroupResponse findRxcuiByName(String name, int search) throws IOException;

    /**
     * Return the properties for a specified concept.
     *
     * @param id       The RxCUI ID of the concept
     * @param propName the property categories for the properties to be returned. This field is required.
     * @return A set of properties associated with this concept ID of the provided type
     * @throws IOException
     */
    PropertiesGroupResponse getAllProperties(String id, PropCategoryNames propName) throws IOException;


    /**
     * Get all the related RxNorm concepts for a given RxNorm identifier. This includes concepts of term types "IN",
     * "MIN", "PIN", "BN", "SBD", "SBDC", "SBDF", "SBDG", "SCD", "SCDC", "SCDF", "SCDG", "DF", "DFG", "BPCK" and "GPCK".
     * See default paths for the paths traveled to get concepts for each term type.
     *
     * @param id The RxCUI for term
     * @return related concepts for the provided terms
     * @throws IOException
     */
    AllRelatedGroupResponse getAllRelatedInfo(String id) throws IOException;

    /**
     * Do an approximate match search to determine the strings in the data set that most closely match the search string.
     *
     * @param id         The term to search for
     * @param maxEntries The max number of entries to return (default 10)
     * @return Result containing a list of {@link Candidate} concepts that may match the term provided
     * @throws IOException
     */
    ApproximateGroupResponse getApproximateMatch(String id, int maxEntries) throws IOException;

    /**
     * Get the related RxNorm identifiers of an RxNorm concept specified by a relational attribute.
     *
     * @param id The Rxcui for the term
     * @param rela a list of the relationship attribute names such as "tradename_of", "has_form", "isa", etc. This
     *             field is required. See the /relatypes example for the valid relationship attributes.
     * @return A response containing {@link ConceptProperty} items related to the identified concept
     */
    RelatedGroupResponse getRelatedByRelationship(String id, List<RelationshipNames> rela) throws IOException;

    /**
     * Get the related RxNorm identifiers of an RxNorm concept specified by one or more term types. See default paths
     * for the paths traveled to get concepts for each term type.
     *
     * @param id The Rxcui for the term
     * @param tty a list of one or more RxNorm term types. This field is required. See the /termtypes example for the
     *            valid term types.
     * @return A response containing {@link ConceptProperty} items related to the identified concept
     */
    RelatedGroupResponse getRelatedByType(String id, List<TTYNames> tty) throws IOException;

    /**
     * Get the National Drug Codes (NDCs) for the RxNorm concept. The NDCs are returned in the CMS 11-digit NDC derivative form.
     *
     * @param id The Rxcui for the term
     * @return A list of NDCs associated with this RxNorm CUI
     * @throws IOException
     */
    NdcsGroupResponse getNdcs(String id) throws IOException;

    /**
     * Get the brands that contain all the specified ingredients. Note that the brands returned may contain other
     * ingredients in addition to those specified.
     *
     * @param ingredientIds list of ingredient RxCUIs that the returned brands must contain.
     * @return A list of brand concepts that include the specified ingredients
     */
    BrandGroupResponse getMultiIngredBrand(List<String> ingredientIds) throws IOException;
}
