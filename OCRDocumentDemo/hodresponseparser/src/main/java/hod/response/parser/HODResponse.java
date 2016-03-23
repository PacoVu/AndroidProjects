package hod.response.parser;

import java.util.List;

// HOD API Response Models

public class HODResponse {

    public static class Action
    {
        public Object result;
        public String status;
        public String action;
        public String version;
    }

    public static class JobBatchResponse
    {
        public List<Action> actions;
        public String jobID;
        public String status;
    }
}
