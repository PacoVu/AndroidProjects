package hod.response.parser;

/**
 * Created by vanphongvu on 12/7/15.
 */
public class CreateConnectorResponse {
    public String connector; // ( string )  The name of the connector.
    public Download_link download_link; // ( Download_link , optional)  Object of download links to available connector installers for different operating systems and CPU architectures. This will only be available if created connector is an "on-site" flavor connector.  
    public String message; // ( string )  Indicates that the connector was created.

    public class Download_link
    {
        public String linux_x86; // ( string , optional)  Download link to Linux 32-bit connector installer.
        public String linux_x86_64; // ( string , optional)  Download link to Linux 64-bit connector installer.
        public String windows_x86; // ( string , optional)  Download link to Windows 32-bit connector installer.
        public String windows_x86_64; // ( string , optional)  Download link to Windows 64-bit connector installer.
    }
}
