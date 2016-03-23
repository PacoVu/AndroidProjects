package hod.response.parser;

import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class ImageRecognitionResponse {

    public List<HODObject> object; // (array[Object]) Details of a recognized object in the image.

    public class HODObject {
        public String unique_name; // (string) The unique name of the object recognized from the database.
        public String name; // (string) The descriptive name of the recognized object.
        public String db; // (enum<Db>) The name of the recognition database that the recognized object is stored in.
        public List<Corners> corners; // (array[Corners]) The positions of the corners of a quadrilateral bounding box containing the recognized object.
    }

    public class Corners {
        public Integer x; // (integer) The horizontal position of the corner of the bounding box.
        public Integer y; // (integer) The vertical position of the corner of the bounding box.
    }

}
