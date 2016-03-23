package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ExpandTermsResponse {
    public List<Terms> terms; // ( array[Terms] )  The details of the expanded terms.

    public class Terms
    {
        public int documents; //(integer , optional)  The number of documents that the expanded term occurs in.
        public String term; //(string )  The expanded term.
    }
}
