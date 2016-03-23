package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class RetrieveConnectorConfigurationFileResponse {
    public String name; //(String )  The name of the connector.
    public String flavor; //( String )  The flavor of the connector.
    public String config; //( String )  The base64 encoded connector configuration.
    public String licenseKey; //( String , optional)  The license key for onsite flavor connectors only.
    public String validation; //(String , optional)  The connector validation key for onsite flavor connectors only.
    public String verification;
}
