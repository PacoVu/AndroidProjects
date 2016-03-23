package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class LanguageIdentificationResponse {
    public String encoding; //( enum<Encoding> )  The identified encoding of the input text.
    public String language; //( enum<Language> )  The identified language of the input text.
    public String language_iso639_2b; //( enum<Language_iso639_2b> )  The ISO639-2B code for the identified language of the input text, "UND" if the language could not be identified.
    public List<String> unicode_scripts; // (array[string] , optional)  The UTF-8 character ranges that your input text includes.
}
