package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class DeleteTextIndexResponse {
    public String confirm; //(string , optional)  The confirmation hash required for deletion.
    public Boolean deleted; //( boolean )  Whether or not the index was deleted.
    public String index; //( string , optional)  Index name
}
