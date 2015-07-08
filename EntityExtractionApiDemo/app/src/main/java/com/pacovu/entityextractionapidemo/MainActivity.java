package com.pacovu.entityextractionapidemo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ListActivity {

    private EntityItemTemplates m_adapter;
    ListView lvSearchResult;
    String[] entitiesType = {"people_eng", "places_eng", "companies_eng", "organizations", "languages", "drugs_eng", "professions", "universities","number_phone_us", "films", "address_us", "date_eng", "internet_email"};

    ProgressBar pbLoadingIndicator;
    WebView wvContainer;
    WebView wvDetail;

    LinearLayout llResultContainer;
    LinearLayout llContextMenu;
    LinearLayout llDetailForm;
    LinearLayout llPlaceForm;
    LinearLayout llPeopleForm;

    Button btLoadMap;
    Button btLoadWiki;
    Button btCanGoback;
    Button btPeople;
    Button btPlaces;

    TextView tvEntityType;
    EditText etWebPageAddress;

    AppModel mAppModel;
    private String jobID = "";
    private Object mSelectedItem;

    private class EntityItem {
        public String name = "";
        public String wikiLink = "";
    }
    private class KnownPlace extends EntityItem {
        public String geoPosition = "";
        public String population = "";
        public String type = "";
        public String code = "";
        public String continent = "";
    };
    private class KnownPeople extends EntityItem {
        public String profession = "";
        public String dob = "";
    };

    ArrayList<Object> mPlaces;
    ArrayList<Object> mPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppModel = (AppModel) getApplication();

        llResultContainer = (LinearLayout) findViewById(R.id.llresultcontainer);
        llContextMenu = (LinearLayout) findViewById(R.id.llcontextmenu);
        llDetailForm = (LinearLayout) findViewById(R.id.lldetailform);
        llPlaceForm = (LinearLayout) findViewById(R.id.place_form_layout);
        llPeopleForm = (LinearLayout) findViewById(R.id.people_form_layout);

        btLoadMap  = (Button) findViewById(R.id.btloadmap);
        btLoadWiki  = (Button) findViewById(R.id.btloadwiki);
        btCanGoback = (Button) findViewById(R.id.btgoback);
        btPeople = (Button) findViewById(R.id.btpeople);
        btPlaces = (Button) findViewById(R.id.btplaces);

        tvEntityType = (TextView) findViewById(R.id.tventitytype);
        wvContainer = (WebView) findViewById(R.id.wvContainer);
        pbLoadingIndicator = (ProgressBar) findViewById(R.id.pbloadingindicator);
        etWebPageAddress = (EditText) findViewById(R.id.etwebpageaddress);

        if(savedInstanceState == null) {
            initialize();
        }
    }
    @Override
    public void onBackPressed() {
        if (llResultContainer.getVisibility() == View.VISIBLE) {
            DoCloseResultContainer(null);
            return;
        } else if (llDetailForm.getVisibility() == View.VISIBLE) {
            DoCloseDetailContainer(null);
            return;
        } else
            finish();
        super.onBackPressed();
    }
    protected void onResume() {
        super.onResume();
        mAppModel = (AppModel) getApplication();
    }
    private void initialize() {
        mPlaces = new ArrayList<Object>();
        mPeople = new ArrayList<Object>();

        lvSearchResult = (ListView) findViewById(android.R.id.list);
        lvSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View myView, int myItemInt, long mylng) {
                Object item = m_adapter.getItem(myItemInt);
                llDetailForm.setVisibility(View.VISIBLE);
                if (item instanceof KnownPlace) {
                    llResultContainer.setVisibility(View.GONE);
                    llPlaceForm.setVisibility(View.VISIBLE);
                    TextView name = (TextView) findViewById(R.id.tvplacename);
                    TextView populate = (TextView) findViewById(R.id.tvplacepopulation);
                    TextView code = (TextView) findViewById(R.id.tvplacecountrycode);
                    TextView continent = (TextView) findViewById(R.id.tvplacecontinent);
                    String placeType = ((KnownPlace) item).type;

                    name.setText("Name: " + ((KnownPlace) item).name);
                    //if (placeType.isEmpty())
                    String countryCode = ((KnownPlace) item).code;
                    if (countryCode.isEmpty())
                        code.setText("");
                    else {
                        if (placeType.equals("country"))
                            code.setText("Country code: " + countryCode);
                        else if (placeType.equals("populated place"))
                            code.setText("State code: " + countryCode);
                        else
                            code.setText("Code: " + countryCode);
                    }
                    String placeContinent = ((KnownPlace) item).continent;
                    if (placeContinent.isEmpty())
                        continent.setText("");
                    else
                        continent.setText("Continent: " + placeContinent);
                    String population = ((KnownPlace) item).population;
                    if (population.isEmpty())
                        populate.setText("");
                    else
                        populate.setText("Population: " + population);
                    if (((KnownPlace) item).geoPosition.isEmpty())
                        btLoadMap.setVisibility(View.GONE);
                    else
                        btLoadMap.setVisibility(View.VISIBLE);

                    if (((KnownPlace) item).wikiLink.isEmpty())
                        btLoadWiki.setEnabled(false);
                    else
                        btLoadWiki.setEnabled(true);

                    mSelectedItem = (KnownPlace) item;
                } else if (item instanceof KnownPeople) {
                    llResultContainer.setVisibility(View.GONE);
                    llPeopleForm.setVisibility(View.VISIBLE);
                    TextView name = (TextView) findViewById(R.id.tvpeoplename);
                    TextView prof = (TextView) findViewById(R.id.tvpeopleprofession);
                    TextView dob = (TextView) findViewById(R.id.tvpeopledob);
                    name.setText("Name: " + ((KnownPeople) item).name);
                    if (((KnownPeople) item).profession.isEmpty())
                        prof.setText("");
                    else
                        prof.setText("Profession: " + ((KnownPeople) item).profession);
                    if (((KnownPeople) item).dob.isEmpty())
                        dob.setText("");
                    else
                        dob.setText("DoB: " + ((KnownPeople) item).dob);

                    btLoadMap.setVisibility(View.GONE);
                    if (((KnownPeople) item).wikiLink.isEmpty())
                        btLoadWiki.setEnabled(false);
                    else
                        btLoadWiki.setEnabled(true);
                    mSelectedItem = (KnownPeople) item;
                }
            }
        });

        wvDetail = (WebView) findViewById(R.id.ivplaceimage);
        wvDetail.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);

                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                pbLoadingIndicator.setVisibility(View.GONE);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbLoadingIndicator.setVisibility(View.VISIBLE);
            }
        });

        WebSettings webSettings = wvContainer.getSettings();
        webSettings.setJavaScriptEnabled(true);

        wvContainer.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView viewx, String urlx) {
                viewx.loadUrl(urlx);
                jobID = "";
                llResultContainer.setVisibility(View.GONE);
                mAppModel.mLink = urlx;
                StartExtractEntity();
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbLoadingIndicator.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                etWebPageAddress.setText(url);
                if (wvContainer.canGoBack())
                    btCanGoback.setEnabled(true);
                else
                    btCanGoback.setEnabled(false);
                pbLoadingIndicator.setVisibility(View.GONE);
                etWebPageAddress.setFocusableInTouchMode(true);
                etWebPageAddress.setFocusable(true);
            }
        });
        etWebPageAddress.setText(mAppModel.mLink);
        wvContainer.loadUrl(mAppModel.mLink);
    }
    private void HideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
    public void DoGoBack(View v) {
        if (wvContainer.canGoBack())
            wvContainer.goBack();
    }
    public void DoLoadWebPage(View v) {
        HideKeyboard();
        String link = etWebPageAddress.getText().toString();
        jobID = "";
        llResultContainer.setVisibility(View.GONE);
        if (link.indexOf("http://") < 0)
            link = "http://" + link;
        if (!link.isEmpty()) {
            wvContainer.loadUrl(link);
            mAppModel.mLink = link;
            StartExtractEntity();
        }
    }
    private void StartExtractEntity() {
        if (jobID.length() == 0) {
            llContextMenu.setVisibility(View.INVISIBLE);

            Map<String, String> map = new HashMap<String, String>();
            String fileType = "";

            map.put("url", mAppModel.mLink);
            String service = "https://api.idolondemand.com/1/api/sync/extractentities/v1?";
            String entityType = "places_eng,people_eng";
            map.put("entity_type", entityType);
            map.put("show_alternatives", "false");
            map.put("unique_entities", "true");

            mAppModel.mCommsEngine.ServicePostRequest(service, fileType, map, new OnServerRequestCompleteListener() {
                @Override
                public void onServerRequestComplete(String response) {
                    try {
                        JSONObject mainObject = new JSONObject(response);
                        if (mainObject.isNull("jobID"))
                            ParseSyncResponse(response);
                        else {
                            jobID = mainObject.getString("jobID");
                            getResultByJobId();
                        }
                    } catch (Exception ex) {
                    }
                }
                public void onErrorOccurred(String error) {
                    // handle error
                }
            });
        }
    }
    private void getResultByJobId() {
        String idol_ocr_job_result = "https://api.idolondemand.com/1/job/result/";
        String param = idol_ocr_job_result + jobID + "?";
        mAppModel.mCommsEngine.ServiceGetRequest(param, "", new
                OnServerRequestCompleteListener() {
                    @Override
                    public void onServerRequestComplete(String response) {
                        ParseAsyncResponse(response);
                    }
                    public void onErrorOccurred(String error) {
                        // handle error
                    }
                });
    }
    private void ParseSyncResponse(String response) {
        if (response == null)
            return;
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray entitiesArray = mainObject.getJSONArray("entities");
            int count = entitiesArray.length();
            int i = 0;
            if (count > 0) {
                mPeople.clear();
                mPlaces.clear();
                for (i = 0; i < count; i++) {
                    JSONObject entity = entitiesArray.getJSONObject(i);
                    String type = entity.getString("type");
                    if (type.equals("places_eng")) {
                        KnownPlace place = new KnownPlace();
                        place.name = entity.getString("normalized_text");
                        JSONObject extraInfo = entity.getJSONObject("additional_information");
                        if (!extraInfo.isNull("lon")) {
                            place.geoPosition = extraInfo.getString("lat");
                            place.geoPosition += ",";
                            place.geoPosition += extraInfo.getString("lon");
                        }
                        if (!extraInfo.isNull("wikipedia_eng"))
                            place.wikiLink = extraInfo.getString("wikipedia_eng");
                        if (!extraInfo.isNull("place_country_code"))
                            place.code = extraInfo.getString("place_country_code");
                        if (!extraInfo.isNull("place_population"))
                            place.population = extraInfo.getString("place_population");
                        if (!extraInfo.isNull("place_type"))
                            place.type = extraInfo.getString("place_type");
                        if (!extraInfo.isNull("place_continent"))
                            place.continent = extraInfo.getString("place_continent");
                        mPlaces.add(place);
                    } else if (type.equals("people_eng")) {
                        KnownPeople people = new KnownPeople();
                        people.name = entity.getString("normalized_text");
                        JSONObject extraInfo = entity.getJSONObject("additional_information");
                        if (!extraInfo.isNull("person_profession")) {
                            JSONArray professionArray = extraInfo.getJSONArray("person_profession");
                            for (int n = 0; n < professionArray.length(); n++) {
                                if (people.profession.length() > 0)
                                    people.profession += "; ";
                                people.profession += professionArray.getString(n);
                            }
                        }
                        if (!extraInfo.isNull("person_date_of_birth"))
                            people.dob = extraInfo.getString("person_date_of_birth");
                        if (!extraInfo.isNull("wikipedia_eng"))
                            people.wikiLink = extraInfo.getString("wikipedia_eng");

                        mPeople.add(people);
                    }
                }
                if (mPeople.size() > 0)
                    btPeople.setVisibility(View.VISIBLE);
                else
                    btPeople.setVisibility(View.GONE);
                if (mPlaces.size() > 0)
                    btPlaces.setVisibility(View.VISIBLE);
                else
                    btPlaces.setVisibility(View.GONE);
                llContextMenu.setVisibility(View.VISIBLE);
            } else {
                llContextMenu.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) { }

    }
    private void ParseAsyncResponse(String response) {
        if (response == null)
            return;
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray textBlockArray = mainObject.getJSONArray("actions");
            int count = textBlockArray.length();
            int i = 0;
            if (count > 0) {
                for (i = 0; i < count; i++) {
                    JSONObject actions = textBlockArray.getJSONObject(i);
                    JSONObject result = actions.getJSONObject("result");
                    JSONArray entitiesArray = result.getJSONArray("entities");
                    count = entitiesArray.length();
                    mPeople.clear();
                    mPlaces.clear();
                    if (count > 0) {
                        for (i = 0; i < count; i++) {
                            JSONObject entity = entitiesArray.getJSONObject(i);
                            String type = entity.getString("type");
                            if (type.equals("places_eng")) {
                                KnownPlace place = new KnownPlace();
                                place.name = entity.getString("original_text");
                                JSONObject extraInfo = entity.getJSONObject("additional_information");
                                if (!extraInfo.isNull("lon")) {
                                    place.geoPosition = extraInfo.getString("lat");
                                    place.geoPosition += ",";
                                    place.geoPosition += extraInfo.getString("lon");
                                }
                                if (!extraInfo.isNull("wikipedia_eng"))
                                    place.wikiLink = extraInfo.getString("wikipedia_eng");
                                if (!extraInfo.isNull("place_country_code"))
                                    place.code = extraInfo.getString("place_country_code");
                                if (!extraInfo.isNull("place_population"))
                                    place.population = extraInfo.getString("place_population");
                                if (!extraInfo.isNull("place_type"))
                                    place.type = extraInfo.getString("place_type");
                                if (!extraInfo.isNull("place_continent"))
                                    place.continent = extraInfo.getString("place_continent");
                                mPlaces.add(place);
                            }
                            else if (type.equals("people_eng")) {
                                KnownPeople people = new KnownPeople();
                                people.name = entity.getString("original_text");
                                JSONObject extraInfo = entity.getJSONObject("additional_information");
                                if (!extraInfo.isNull("person_profession")) {
                                    JSONArray professionArray = extraInfo.getJSONArray("person_profession");
                                    for (int n = 0; n < professionArray.length(); n++) {
                                        if (people.profession.length() > 0)
                                            people.profession += "; ";
                                        people.profession += professionArray.getString(n);
                                    }
                                }
                                if (!extraInfo.isNull("person_date_of_birth"))
                                    people.dob = extraInfo.getString("person_date_of_birth");
                                if (!extraInfo.isNull("wikipedia_eng"))
                                    people.wikiLink = extraInfo.getString("wikipedia_eng");

                                mPeople.add(people);
                            }
                        }
                        if (mPeople.size() > 0)
                            btPeople.setVisibility(View.VISIBLE);
                        else
                            btPeople.setVisibility(View.GONE);
                        if (mPlaces.size() > 0)
                            btPlaces.setVisibility(View.VISIBLE);
                        else
                            btPlaces.setVisibility(View.GONE);
                        llContextMenu.setVisibility(View.VISIBLE);
                    } else {
                        llContextMenu.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                Toast.makeText(this, "Not available", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) { }

    }
    public void DoShowPeople(View v) {
        llResultContainer.setVisibility(View.VISIBLE);
        llContextMenu.setVisibility(View.GONE);
        tvEntityType.setText("Known People");
        this.m_adapter = new EntityItemTemplates(this, R.layout.row, mPeople);
        setListAdapter(this.m_adapter);
    }

    public void DoShowPlaces(View v) {
        llResultContainer.setVisibility(View.VISIBLE);
        llContextMenu.setVisibility(View.GONE);
        tvEntityType.setText("Known Places");
        this.m_adapter = new EntityItemTemplates(this, R.layout.row, mPlaces);
        setListAdapter(this.m_adapter);
    }
    public void DoCloseResultContainer(View v) {
        llResultContainer.setVisibility(View.GONE);
        llContextMenu.setVisibility(View.VISIBLE);
    }
    public void DoCloseDetailContainer(View v) {
        llDetailForm.setVisibility(View.GONE);
        if (mSelectedItem instanceof KnownPlace)
            llPlaceForm.setVisibility(View.GONE);
        else if (mSelectedItem instanceof KnownPeople)
            llPeopleForm.setVisibility(View.GONE);

        wvDetail.loadUrl("");
        llContextMenu.setVisibility(View.VISIBLE);
    }
    public void DoLoadMoreDetails(View v) {
        int btnId = v.getId();
        switch (btnId) {
            case R.id.btloadmap:
                if (((KnownPlace) mSelectedItem).geoPosition.length() > 0) {
                    String uri = "geo:" + ((KnownPlace) mSelectedItem).geoPosition + "?z=8";//&q="+ ((KnownPlace) mSelectedItem).geoPosition + "(" + ((KnownPlace) mSelectedItem).name + ")";//&q="+ geopos[1] + "," + geopos[0] + " (" + "point" + ")";
                    startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
            }
            case R.id.btloadwiki: {
                String wikiLink = "";
                if (mSelectedItem instanceof KnownPlace)
                    wikiLink = ((KnownPlace) mSelectedItem).wikiLink;
                else
                    wikiLink = ((KnownPeople) mSelectedItem).wikiLink;
                wvDetail.loadUrl(wikiLink);
                break;
            }
            default:
                break;
        }
    }
    public class EntityItemTemplates extends ArrayAdapter<Object> {

        private ArrayList<Object> items;

        public EntityItemTemplates(Context context, int textViewResourceId, ArrayList<Object> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, parent, false);
            }
            Object obj = items.get(position);
            EntityItem item = (EntityItem) obj;
            if (obj != null) {
                TextView template_title = (TextView) v.findViewById(R.id.p_title);
                if (template_title != null) {
                    template_title.setText(item.name);
                }
            }
            return v;
        }
    }
}
