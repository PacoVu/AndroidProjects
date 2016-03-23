package hod.response.parser;

import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class BarcodeRecognitionResponse {

    public List<Barcode> barcode;
    public class BarcodeAdditionalInformation
    {
        public String country;
    }

    public class Barcode
    {
        public String text;
        public String barcode_type;
        public Integer left;
        public Integer top;
        public Integer width;
        public Integer height;
        public BarcodeAdditionalInformation additional_information;
    }
}
