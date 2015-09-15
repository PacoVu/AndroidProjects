#####################################################################################################
IODClient library for Android is a lightweight Java based API, which helps you easily integrate your 
Android app with HP IDOL OnDemand Services.

IODClient library requires a minimum Android API level 10.

IODClient library uses the Apache httpmime-4.3.2 and httpcore-4.3.2 libraries. For your convenience, 
the library project included the httpmime-4.3.2.jar and httpcore-4.3.jar files. The dependent components 
are licensed under the Apache License 2.0. Please see the files called LICENSE.txt and NOTICE.txt for 
more information.


************************************ IODClient class ************************************************
1. Integrate IODClient into Windows project

a) Create a new or open an existing Android project
b) Select the app main folder and click on the File menu then choose Import Module option.
c) Open the app main project build.gradle and add packaging options and the dependency as follows:

	android {
		packagingOptions {
			exclude 'META-INF/DEPENDENCIES'
			exclude 'META-INF/NOTICE'
			exclude 'META-INF/LICENSE'
			exclude 'META-INF/LICENSE.txt'
			exclude 'META-INF/NOTICE.txt'
			exclude 'META-INF/ASL2.0'
		}
		}
		dependencies {
			compile fileTree(dir: 'libs', include: ['*.jar'])
			compile project (':iodclient')
		}


2. Code Example

public class MyActivity extends Activity implements IIODClientCallback {
	String apiKey = “your-api-key”;
	IODClient iodClient = new IODClient(apiKey, this);

	public void useIODClientAPI() {
		String iodApp = IODApps.ENTITY_EXTRACTION;
		Map<String,Object> params =  new HashMap<String,Object>();
		params.put(“url”, “http://www.cnn.com”);
		Map<String, String> entity_array = new HashMap<String, String>();
		entity_array.put(“entity_type”, “people_eng,places_eng”);
		params.put(“arrays”, entity_array);
		
		iodClient.GetRequest(params, iodApp, IODClient.REQ_MODE.ASYNC);
	}

	@Override
    	public void requestCompletedWithJobID(String response){ 
	
	}
    	@Override
    	public void requestCompletedWithContent(String response){ 
	
	}
    	@Override
    	public void onErrorOccurred(String errorMessage){ 
	
	}
}
