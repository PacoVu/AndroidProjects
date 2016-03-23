package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class GetCommonNeighborsResponse {
    public List<Nodes> nodes;
    public class Nodes
    {
        public Attributes attributes;
        public int id;
        public int commonality;
        public int sort_value;
    }
    public class Attributes
    {
        public String name;
    }
}
