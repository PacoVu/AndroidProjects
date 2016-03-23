package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class IndexStatusResponse {
    //public int 24hr_index_updates; // ( number )  The total number of document updates in the last 24 hours.
    public int component_count; //( number )  The total number of index components contained in the text index (Haven OnDemand creates additional components when a text index grows larger than the component quota size for the flavor).
    public int total_documents; //( number )  The total number of documents in the index.
    public int total_index_size; //( number )  The total size of the index in bytes.
}
