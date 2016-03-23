package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ListResourcesResponse {
    public List<Private_resources> private_resources; //(array[Private_resources] )  List of private resources.
    public List<Public_resources> public_resources; // ( array[Public_resources] )  List of public resources.

    public class Private_resources
    {
        public String date_created; // (String )  The date the resource was created.
        public String description; // (String or null , optional)  The description of the resource.
        public String flavor; // ( String or null , optional)  The flavor of the resource.
        public String resource; // ( String )  The resource name.
        public String type; // ( String )  The type of resource.
        public String display_name; // (String , optional)  A friendly name for the resource.
        public String resourceUUID;
    }

    public class Public_resources
    {
        public String description; // (String , optional)  The description of the resource.
        public String resource; // ( String )  The resource name.
        public String type; // ( String )  The type of resource.
    }
}
