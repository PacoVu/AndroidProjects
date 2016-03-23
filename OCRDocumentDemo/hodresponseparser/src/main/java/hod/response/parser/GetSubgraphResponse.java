package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class GetSubgraphResponse {
    public List<Edges> edges;
    public List<Nodes> nodes;

    public class Edges
    {
        public Attributes attributes;
        public int source;
        public int target;
    }

    public class Attributes
    {
        public double weight;
    }

    public class Nodes
    {
        public Attributes attributes;
        public int id;
    }

}
