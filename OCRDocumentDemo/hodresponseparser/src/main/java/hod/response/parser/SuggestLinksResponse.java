package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/17/15.
 */
public class SuggestLinksResponse {
    public List<Suggestions> suggestions;


    public class Suggestions
    {
        public Source source;
        public List<Nodes> nodes;
    }

    public class Source
    {
        public int id;
        public Attributes attributes;
    }

    public class Attributes
    {
        public String name;
    }

    public class Nodes
    {
        public Attributes attributes;
        public int id;
        public int sort_value;
    }
}
