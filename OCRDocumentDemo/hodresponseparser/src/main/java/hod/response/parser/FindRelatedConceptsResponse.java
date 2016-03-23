package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class FindRelatedConceptsResponse {
    public List<Entities> entities; //(array[Entities] )  A result term or phrase identified in the results set.

    public class Entities
    {
        public int cluster; //(number )  The cluster into which the phrase has been grouped.This value allows you to cluster the elements according to their occurrence.
        public int docs_with_all_terms; //(number )  The number of documents of the results set in which all terms of this element appear.
        public int docs_with_phrase; //( number )  The number of documents in the result set in which this element appears as a phrase.
        public int occurrences; //(number)  The total number of occurrences of this element in the results set.
        public String text; //(string)  The text of the identified term or phrase.
    }
}
