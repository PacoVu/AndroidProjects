package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ConceptExtractionResponse {
    public List<Concepts> concepts; // (array[Concepts] )  A result concept identified in the results set.

    public class Concepts
    {
        public String concept; //(string)  The text of the identified concept.
        public int occurrences; // (number)  The total number of occurrences of this element in the results set.
    }
}
