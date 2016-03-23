package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class SummarizeGraphResponse {
    public List<Attributes> attributes;
    public int edges;
    public int nodes;

    public class Attributes
    {
        public String data_type;
        public String element_type;
        public String name;
        public int number;
    }
}
