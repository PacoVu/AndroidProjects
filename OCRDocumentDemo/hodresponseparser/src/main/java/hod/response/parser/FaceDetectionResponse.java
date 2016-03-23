package hod.response.parser;

import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class FaceDetectionResponse {
    public List<Face> face; // (array[Face]) The details of a detected face.

    public class Face {
        public Integer left; // (integer) The position of the left edge of the bounding box for the face, in pixels from the left edge of the image (for PDF images, position is in points).
        public Integer top; // (integer) The position of the top edge of the bounding box for the face, in pixels from the top edge of the image (for PDF images, position is in points).
        public Integer width; // (integer) The width of the bounding box for the face, in pixels (or points for PDF images).
        public Integer height; // (integer) The height of the bounding box for the face (or points for PDF images).
        public AdditionalInformation additional_information; // (Additional_information, optional)
    }

    public class AdditionalInformation {
        public String age; // (string, optional) Age range of the face
    }
}
