package hod.response.parser;

import java.util.List;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ConnectorHistoryResponse {
    public List<History> history;
    public class History
    {
        public String connector;
        public Document_counts document_counts;
        public String error;
        public String failed;
        public String process_end_time; // Format: DD/MM/YYYY HH:mm:ss Z.
        public String process_start_time; // Format: DD/MM/YYYY HH:mm:ss Z.  
        public String start_time; // Format: DD/MM/YYYY HH:mm:ss Z.
        public String queued_time; // Format: DD/MM/YYYY HH:mm:ss Z.
        public String status; // 
        public double time_in_queue; // 
        public double time_processing; // 
        public String token; // 
    }
    public class Document_counts
    {
        public int added;
        public int deleted;
        public int errors;
        public int ingest_added;
        public int ingest_deleted;
        public int ingest_failed;
    }
    
}
