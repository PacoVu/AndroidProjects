package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class RecommendResponse {
    public List<Allrecommendations> allRecommendations; // (array[Allrecommendations] , optional)  The data set values.
    public List<Fields> fields; // (array[Fields] )  Metadata definition of the data set, describing the structure of the dataset fields.

    public class Allrecommendations
    {
        public List<String> originalValues; // (array[String] , optional)  An array of original record values.Ordered in correlation to the list of Fields Metadata.
        public List<Recommendations> recommendations; // ( array[Recommendations] , optional)  An array of recommended records.
    }

    public class Recommendations
    {
        public String confidence; // (number , optional)  The confidence that the label will be as required
        public String distance; // (number , optional)  Distance between the original record to the recommended record.
        public String new_prediction; //  ( String , optional)  Predicted field result according to prediction model.
        public List<String> recommendation; //  ( array[String] , optional)  An array of recommended record values.Ordered in correlation to the list of Fields Metadata.
    }

    public class Fields
    {
        public String name; // (String )  The name of the field.
        public int order; //  ( integer )  The order of the field.
        public String type; //  ( String )  The type of the field.
    }
}
