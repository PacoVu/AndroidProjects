package hod.response.parser;

import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class SentimentAnalysisResponse {
    public List<Entity> positive;
    public List<Entity> negative;
    public Aggregate aggregate;

    public class Aggregate {
        public String sentiment; // (enum<Sentiment>) The sentiment value for the text.
        public Double score; // (number) The mean confidence score for the sentiment entities (higher scores indicate a more likely sentiment match). Negative scores indicate negative sentiment.
    }

    public class Entity {
        public String sentiment; // ( string or null )
        public String topic; // ( string or null )
        public Double score; // (number) The confidence score for the match (higher scores indicate a more likely match). Negative scores indicate negative sentiment.
        public String original_text; // (string) The original text of the extracted entity.
        public String normalized_text; // (string) The text of the extracted entity, after character normalization.
        public Double original_length; // (number) The original length of the extracted entity.
        public Double normalized_length; // (number) The length of the extracted entity, after character normalization.
    }
}
