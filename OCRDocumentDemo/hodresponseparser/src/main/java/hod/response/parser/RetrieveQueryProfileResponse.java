package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class RetrieveQueryProfileResponse {
    public String query_profile; //(String )  The name of the query profile.
    public String description; //(String or null )  Query profile description.
    public String query_manipulation_index; //( String )  The name of the query manipulation index.
    public Boolean promotions_enabled; //( Booleanean )  Whether to enable promotion.
    public List<String> promotion_categories; //(array[String] )  List of promotion categories.
    public Boolean promotions_identified; //(Booleanean )  Whether to identify whether documents are a promotion or not.
    public Boolean synonyms_enabled; //(Booleanean )  Whether to enable synonyms.
    public List<String> synonym_categories; //(array[String] )  List of synonym categories.
    public Boolean blacklists_enabled; //(Booleanean )  Whether to enable blacklist.
    public List<String> blacklist_categories; //(array[String] )  List of blacklist categories.
}
