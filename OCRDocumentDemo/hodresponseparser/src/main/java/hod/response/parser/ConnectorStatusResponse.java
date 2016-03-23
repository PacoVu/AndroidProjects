package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class ConnectorStatusResponse {
    public String connector; // (String )  The name of the connector.
    public String status; // ( String )  The current state of the connector job.
    public Document_counts document_counts; // ( Document_counts , optional)  Information about documents processed during connector job.
    public String error; // ( String , optional)  Error message for this connector job.
    public String failed; // (String , optional)  The failed reason for this connector job.
    public String process_end_time; // (String , optional)  The time when connector job finished processing.Format: DD/MM/YYYY HH:mm:ss Z.
    public String process_start_time; // (String , optional)  The time when connector job started being processed.Format: DD/MM/YYYY HH:mm:ss Z.  
    public String start_time; // ( String , optional)  The last time when connector job has been updated or processed.Format: DD/MM/YYYY HH:mm:ss Z.
    public String queued_time; // (String , optional)  The time when connector job has been put on the queue.Format: DD/MM/YYYY HH:mm:ss Z.
    public double time_in_queue; // (number , optional)  The number of seconds the connector job spent in the queue.
    public double time_processing; // (number , optional)  The number of seconds spent processing the connector job.
    public String token; // ( String , optional)  Start connector token.
    public Schedule schedule; // ( Schedule , optional)  Schedule information for this connector.
    public class Document_counts
    {
        public int added;
        public int deleted;
        public int errors;
        public int ingest_added;
        public int ingest_deleted;
        public int ingest_failed;
    }
    public class Schedule
    {
        public String last_run_time; // (String or null , optional)  The last time the connector was scheduled to run.Format: DD/MM/YYYY HH:mm:ss Z.
        public String next_run_time; // (String or null , optional)  The next time the connector is scheduled to run.Format: DD/MM/YYYY HH:mm:ss Z.
        public int occurrences_remaining; // (number , optional)  The number occurrences remaining for your connector schedule.If occurrences is unlimited, this value is -1.  
    }
}
