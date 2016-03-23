package hod.response.parser;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vuv on 9/24/2015.
 */
public class HODResponseParser {
    private List<HODErrorObject> errors;
    private Gson gsonObj;
    public HODResponseParser()
    {
        errors = new ArrayList<HODErrorObject>();
        gsonObj = new Gson();
    }
    private void ResetErrorList()
    {
        errors.clear();
    }
    private void AddError(HODErrorObject error)
    {
        errors.add(error);
    }
    public List<HODErrorObject> GetLastError()
    {
        return errors;
    }
    public String ParseJobID(String jsonString)
    {
        String jobID = "";
        try {
            JSONObject mainObject = new JSONObject(jsonString);
            if (!mainObject.isNull("jobID")) {
                jobID = mainObject.getString("jobID");
            } else {
                HODErrorObject error = new HODErrorObject();
                error.error = HODErrorCode.INVALID_HOD_RESPONSE;
                error.reason = "Unrecognized response from HOD";
                this.AddError(error);
            }
        } catch (Exception ex) {
            HODErrorObject error = new HODErrorObject();
            error.error = HODErrorCode.UNKNOWN_ERROR;
            error.reason = "Unknown error";
            error.detail = ex.getMessage();
            this.AddError(error);
            return jobID;
        }
        return jobID;
    }
    private String getResult(String jsonStr) {
        String result = jsonStr;
        if (jsonStr.length() == 0) {
            HODErrorObject error = new HODErrorObject();
            error.error = HODErrorCode.INVALID_HOD_RESPONSE;
            error.reason = "Empty response";
            this.AddError(error);
            return null;
        }
        try {
            JSONObject mainObject = new JSONObject(jsonStr);
            JSONObject actions = null;
            if (!mainObject.isNull("actions")) {
                actions = mainObject.getJSONArray("actions").getJSONObject(0);
                //String action = actions.getString("action");
                String status = actions.getString("status");
                if (status.equals("finished"))
                    result = actions.getJSONObject("result").toString();
                else if (status.equals("failed")) {
                    JSONArray errorArr = actions.getJSONArray("errors");
                    int count = errorArr.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject error = errorArr.getJSONObject(i);
                        HODErrorObject err = new HODErrorObject();
                        err.error = error.getInt("error");
                        err.reason = error.getString("reason");
                        if (!error.isNull("detail"))
                            err.detail = error.getString("detail");
                        this.AddError(err);
                    }
                    return null;
                } else if (status.equals("queued")) {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.QUEUED;
                    error.reason = "Task is in queue.";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                } else if (status.equals("in progress")) {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.IN_PROGRESS;
                    error.reason = "Task is in progress.";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                } else {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.UNKNOWN_ERROR;
                    error.reason = "Unknown error";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                }
            }
        } catch (Exception ex) {
            HODErrorObject error = new HODErrorObject();
            error.error = HODErrorCode.INVALID_HOD_RESPONSE;
            error.reason = "Unrecognized response from HOD";
            error.detail = ex.getMessage();
            this.AddError(error);
            return null;
        }
        return result;
    }
    public SpeechRecognitionResponse ParseSpeechRecognitionResponse(String jsonStr)
    {
        SpeechRecognitionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, SpeechRecognitionResponse.class);
        }
        return obj;
    }
    public CancelConnectorResponse ParseCancelConnectorResponse(String jsonStr) {
        CancelConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, CancelConnectorResponse.class);
        }
        return obj;
    }
    public ConnectorHistoryResponse ParseConnectorHistoryResponse(String jsonStr) {
        ConnectorHistoryResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ConnectorHistoryResponse.class);
        }
        return obj;
    }
    public ConnectorStatusResponse ParseConnectorStatusResponse(String jsonStr) {
        ConnectorStatusResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ConnectorStatusResponse.class);
        }
        return obj;
    }
    public CreateConnectorResponse ParseCreateConnectorResponse(String jsonStr) {
        CreateConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, CreateConnectorResponse.class);
        }
        return obj;
    }
    public DeleteConnectorResponse ParseDeleteConnectorResponse(String jsonStr) {
        DeleteConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, DeleteConnectorResponse.class);
        }
        return obj;
    }
    public RetrieveConnectorConfigurationAttributeResponse ParseRetrieveConnectorConfigurationAttributeResponse(String jsonStr) {
        RetrieveConnectorConfigurationAttributeResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, RetrieveConnectorConfigurationAttributeResponse.class);
        }
        return obj;
    }
    public RetrieveConnectorConfigurationFileResponse ParseRetrieveConnectorConfigurationFileResponse(String jsonStr) {
        RetrieveConnectorConfigurationFileResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, RetrieveConnectorConfigurationFileResponse.class);
        }
        return obj;
    }
    public StartConnectorResponse ParseStartConnectorResponse(String jsonStr) {
        StartConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, StartConnectorResponse.class);
        }
        return obj;
    }
    public StopConnectorResponse ParseStopConnectorResponse(String jsonStr) {
        StopConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, StopConnectorResponse.class);
        }
        return obj;
    }
    public UpdateConnectorResponse ParseUpdateConnectorResponse(String jsonStr) {
        UpdateConnectorResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, UpdateConnectorResponse.class);
        }
        return obj;
    }
    public ExpandContainerResponse ParseExpandContainerResponse(String jsonStr) {
        ExpandContainerResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ExpandContainerResponse.class);
        }
        return obj;
    }
    public StoreObjectResponse ParseStoreObjectResponse(String jsonStr) {
        StoreObjectResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, StoreObjectResponse.class);
        }
        return obj;
    }
    public ViewDocumentResponse ParseViewDocumentResponse(String jsonStr) {
        ViewDocumentResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ViewDocumentResponse.class);
        }
        return obj;
    }
    public GetCommonNeighborsResponse ParseGetCommonNeighborsResponse(String jsonStr) {
        GetCommonNeighborsResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, GetCommonNeighborsResponse.class);
        }
        return obj;
    }
    public GetNeighborsResponse ParseGetNeighborsResponse(String jsonStr) {
        GetNeighborsResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, GetNeighborsResponse.class);
        }
        return obj;
    }
    public GetNodesResponse ParseGetNodesResponse(String jsonStr) {
        GetNodesResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, GetNodesResponse.class);
        }
        return obj;
    }
    public GetShortestPathResponse ParseGetShortestPathResponse(String jsonStr) {
        GetShortestPathResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, GetShortestPathResponse.class);
        }
        return obj;
    }
    public GetSubgraphResponse ParseGetSubgraphResponse(String jsonStr) {
        GetSubgraphResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, GetSubgraphResponse.class);
        }
        return obj;
    }
    public SuggestLinksResponse ParseSuggestLinksResponse(String jsonStr) {
        SuggestLinksResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, SuggestLinksResponse.class);
        }
        return obj;
    }
    public SummarizeGraphResponse ParseSummarizeGraphResponse(String jsonStr) {
        SummarizeGraphResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, SummarizeGraphResponse.class);
        }
        return obj;
    }
    public OCRDocumentResponse ParseOCRDocumentResponse(String jsonStr) {
        OCRDocumentResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, OCRDocumentResponse.class);
        }
        return obj;
    }
    public BarcodeRecognitionResponse ParseBarcodeRecognitionResponse(String jsonStr) {
        BarcodeRecognitionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, BarcodeRecognitionResponse.class);
        }
        return obj;
    }
    public FaceDetectionResponse ParseFaceDetectionResponse(String jsonStr) {
        FaceDetectionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, FaceDetectionResponse.class);
        }
        return obj;
    }
    public ImageRecognitionResponse ParseImageRecognitionResponse(String jsonStr) {
        ImageRecognitionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ImageRecognitionResponse.class);
        }
        return obj;
    }
    public PredictResponse ParsePredictResponse(String jsonStr) {
        PredictResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, PredictResponse.class);
        }
        return obj;
    }
    public RecommendResponse ParseRecommendResponse(String jsonStr) {
        RecommendResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, RecommendResponse.class);
        }
        return obj;
    }
    public TrainPredictionResponse ParseTrainPredictionResponse(String jsonStr) {
        TrainPredictionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, TrainPredictionResponse.class);
        }
        return obj;
    }
    public CreateQueryProfileResponse ParseCreateQueryProfileResponse(String jsonStr) {
        CreateQueryProfileResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, CreateQueryProfileResponse.class);
        }
        return obj;
    }
    public DeleteQueryProfileResponse ParseDeleteQueryProfileResponse(String jsonStr) {
        DeleteQueryProfileResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, DeleteQueryProfileResponse.class);
        }
        return obj;
    }
    public RetrieveQueryProfileResponse ParseRetrieveQueryProfileResponse(String jsonStr) {
        RetrieveQueryProfileResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, RetrieveQueryProfileResponse.class);
        }
        return obj;
    }
    public UpdateQueryProfileResponse ParseUpdateQueryProfileResponse(String jsonStr) {
        UpdateQueryProfileResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, UpdateQueryProfileResponse.class);
        }
        return obj;
    }
    public FindRelatedConceptsResponse ParseFindRelatedConceptsResponse(String jsonStr) {
        FindRelatedConceptsResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, FindRelatedConceptsResponse.class);
        }
        return obj;
    }
    public AutoCompleteResponse ParseAutoCompleteResponse(String jsonStr) {
        AutoCompleteResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, AutoCompleteResponse.class);
        }
        return obj;
    }
    public ConceptExtractionResponse ParseConceptExtractionResponse(String jsonStr) {
        ConceptExtractionResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ConceptExtractionResponse.class);
        }
        return obj;
    }
    public ExpandTermsResponse ParseExpandTermsResponse(String jsonStr) {
        ExpandTermsResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ExpandTermsResponse.class);
        }
        return obj;
    }
    public HighlightTextResponse ParseHighlightTextResponse(String jsonStr) {
        HighlightTextResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, HighlightTextResponse.class);
        }
        return obj;
    }
    public LanguageIdentificationResponse ParseLanguageIdentificationResponse(String jsonStr) {
        LanguageIdentificationResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, LanguageIdentificationResponse.class);
        }
        return obj;
    }
    public SentimentAnalysisResponse ParseSentimentAnalysisResponse(String jsonStr) {
        SentimentAnalysisResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, SentimentAnalysisResponse.class);
        }
        return obj;
    }
    public TextTokenizationResponse ParseTextTokenizationResponse(String jsonStr) {
        TextTokenizationResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, TextTokenizationResponse.class);
        }
        return obj;
    }
    public AddToTextIndexResponse ParseAddToTextIndexResponse(String jsonStr) {
        AddToTextIndexResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, AddToTextIndexResponse.class);
        }
        return obj;
    }
    public CreateTextIndexResponse ParseCreateTextIndexResponse(String jsonStr) {
        CreateTextIndexResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, CreateTextIndexResponse.class);
        }
        return obj;
    }
    public DeleteTextIndexResponse ParseDeleteTextIndexResponse(String jsonStr) {
        DeleteTextIndexResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, DeleteTextIndexResponse.class);
        }
        return obj;
    }
    public DeleteFromTextIndexResponse ParseDeleteFromTextIndexResponse(String jsonStr) {
        DeleteFromTextIndexResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, DeleteFromTextIndexResponse.class);
        }
        return obj;
    }
    public IndexStatusResponse ParseIndexStatusResponse(String jsonStr) {
        IndexStatusResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, IndexStatusResponse.class);
        }
        return obj;
    }
    public ListResourcesResponse ParseListResourcesResponse(String jsonStr) {
        ListResourcesResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, ListResourcesResponse.class);
        }
        return obj;
    }
    public RestoreTextIndexResponse ParseRestoreTextIndexResponse(String jsonStr) {
        RestoreTextIndexResponse obj = null;
        String result = getResult(jsonStr);
        if (result != null) {
            obj = gsonObj.fromJson(result, RestoreTextIndexResponse.class);
        }
        return obj;
    }
    public Object ParseCustomResponse(Class<?> T, String jsonStr)
    {
        Object obj = null;
        String result = jsonStr;
        if (jsonStr.length() == 0) {
            HODErrorObject error = new HODErrorObject();
            error.error = HODErrorCode.INVALID_HOD_RESPONSE;
            error.reason = "Empty response";
            this.AddError(error);
            return obj;
        }
        try {
            JSONObject mainObject = new JSONObject(jsonStr);
            //JSONObject actions = null;
            if (!mainObject.isNull("actions"))
            {
                JSONObject actions = mainObject.getJSONArray("actions").getJSONObject(0);
                String action = actions.getString("action");
                String status = actions.getString("status");
                if (status.equals("finished"))
                    result = actions.getJSONObject("result").toString();
                else if (status.equals("failed")) {
                    JSONArray errorArr = actions.getJSONArray("errors");
                    int count = errorArr.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject error = errorArr.getJSONObject(i);
                        HODErrorObject err = new HODErrorObject();
                        err.error = error.getInt("error");
                        err.reason = error.getString("reason");
                        if (!error.isNull("detail"))
                            err.detail = error.getString("detail");
                        this.AddError(err);
                    }
                    return null;
                } else if (status.equals("queued")) {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.QUEUED;
                    error.reason = "Task is in queue.";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                } else if (status.equals("in progress")) {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.IN_PROGRESS;
                    error.reason = "Task is in progress.";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                }
                else {
                    HODErrorObject error = new HODErrorObject();
                    error.error = HODErrorCode.UNKNOWN_ERROR;
                    error.reason = "Unknown error";
                    error.jobID = actions.getString("jobID");
                    this.AddError(error);
                    return null;
                }
            }
            obj = gsonObj.fromJson(result, T);
        } catch (Exception ex) {
            HODErrorObject error = new HODErrorObject();
            error.error = HODErrorCode.INVALID_HOD_RESPONSE;
            error.reason = "Unrecognized response from HOD";
            error.detail = ex.getMessage();
            this.AddError(error);
        }
        return obj;
    }
}
