package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class GetShortestPathResponse {
    public List<Edges> edges;
    public List<Nodes> nodes;

    public class Edges
    {
        public Attributes attributes;
        public int length; //(number )  Length/weight/cost of edge.
        public int source; //( integer )  Source node ID.
        public int target; //( integer )  Target node ID.
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
