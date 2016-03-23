package hod.response.parser;

import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class OCRDocumentResponse {
    public List<TextBlock> text_block; // Details of a section of text found in the image.
    public class TextBlock
    {
        public String text; // (string) The text extracted in this section of the image.
        public Integer left; // (integer) The position of the left edge of the bounding box for the text.
        public Integer top; // (integer) The position of the top edge of the bounding box for the text.
        public Integer width; // (integer) The width of the bounding box for the text.
        public Integer height; // (integer) The height of the bounding box for the text.
    }
}
