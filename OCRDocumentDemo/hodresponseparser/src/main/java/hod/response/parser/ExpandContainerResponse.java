package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ExpandContainerResponse {
    public List<Files> files; //A list of files and object store references.

    public class Files
    {
        public String name; // The name of the extracted file.
        public String reference;
    }
}
