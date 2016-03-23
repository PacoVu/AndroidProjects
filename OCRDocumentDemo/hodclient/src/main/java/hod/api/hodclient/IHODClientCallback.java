package hod.api.hodclient;

/**
 * Created by vuv on 10/10/2015.
 */
public interface IHODClientCallback {
    public void requestCompletedWithContent(String response);
    public void requestCompletedWithJobID(String response);
    public void onErrorOccurred(String errorMessage);
}