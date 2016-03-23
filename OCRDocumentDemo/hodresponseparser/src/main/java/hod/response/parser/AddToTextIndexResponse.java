package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class AddToTextIndexResponse {
    public String index; //(string )  The text index that the file was indexed to.
    public List<References> references; // ( array[References] )  Files indexed

    public class References
    {
        public int id;
        public String reference; //(string , optional)  File reference.
    }
}
