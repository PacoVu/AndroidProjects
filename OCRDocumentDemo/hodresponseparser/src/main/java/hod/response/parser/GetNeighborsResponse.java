package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class GetNeighborsResponse {
    public List<Neighbors> neighbors;

    public class Neighbors
    {
        public Target target;
        public Source source;
        public List<Nodes> nodes;
    }

    public class Nodes
    {
        public Attributes attributes;
        public int id;
        public double sort_value;
    }
    public class Attributes
    {
        public String name;
    }
    public class Target
    {
        public int id;
        public Attributes attributes;
    }
    public class Source
    {
        public int id;
        public Attributes attributes;
    }
}
