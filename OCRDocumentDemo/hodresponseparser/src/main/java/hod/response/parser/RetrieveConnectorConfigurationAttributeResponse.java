package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class RetrieveConnectorConfigurationAttributeResponse {
    public String name; //(String )  The name of the connector.
    public String flavor; //( String )  The flavor of the connector.
    public Config config; //(Config )  The configured attributes for this connector.

    public class Config
    {
        public ConfigObj config; // (object , optional)  The configured config attributes for this connector.
        public DestinationObj destination; // ( object , optional)  The configured destination attributes for this connector.
        public ScheduleObj schedule; // ( object , optional)  The configured schedule attributes for this connector.
        public CredentialsObj credentials; // ( object , optional)  The configured credentials attributes for this connector.
        public CredentialsPolicy credentials_policy; // ( object , optional)  The configured credentials_policy attributes for this connector.
        public String description; // ( String , optional)  The connector description.
    }
    public class ConfigObj
    {
        public String url;
        public int max_pages;
    }
    public class DestinationObj
    {
        public String action;
        public String index;
    }
    public class ScheduleObj
    {
        public class frequency
        {
            public String frequency_type;
            public double interval;
        }
    }
    public class CredentialsObj
    {
        public String login_value;
        public String password_value;
    }
    public class CredentialsPolicy
    {
        public String notification_email;
    }
}
