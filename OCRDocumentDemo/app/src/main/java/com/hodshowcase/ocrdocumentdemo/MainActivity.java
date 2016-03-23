package com.hodshowcase.ocrdocumentdemo;

        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Matrix;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ProgressBar;
        import android.widget.Toast;


        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.util.HashMap;
        import java.util.Map;

        import hod.api.hodclient.HODApps;
        import hod.api.hodclient.HODClient;
        import hod.api.hodclient.IHODClientCallback;
        import hod.response.parser.HODResponseParser;
        import hod.response.parser.OCRDocumentResponse;


public class MainActivity extends Activity implements IHODClientCallback {
    private static final int SELECT_PICTURE = 1;
    private static final int TAKE_PICTURE = 2;
    private String mImageFullPathAndName = "";
    private String localImagePath = "";
    private static final int OPTIMIZED_LENGTH = 1024;

    HODClient hodClient;
    HODResponseParser parser;

    ImageView ivSelectedImg;
    EditText edTextResult;
    LinearLayout llResultContainer;
    LinearLayout llCameraButtons;
    LinearLayout llOperations;
    ProgressBar pbOCRReconizing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivSelectedImg = (ImageView) findViewById(R.id.imageView);
        llCameraButtons = (LinearLayout) findViewById(R.id.llcamerabuttons);
        llOperations = (LinearLayout) findViewById(R.id.lloptions);

        edTextResult = (EditText) findViewById(R.id.etresult);
        llResultContainer = (LinearLayout) findViewById(R.id.llresultcontainer);
        pbOCRReconizing = (ProgressBar) findViewById(R.id.pbocrrecognizing);

        CreateLocalImageFolder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hodClient == null)
            hodClient = new HODClient("your-api-key", this);
        if (parser == null)
            parser = new HODResponseParser();
    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override public void onBackPressed() {
        if (llResultContainer.getVisibility() == View.VISIBLE) {
            llResultContainer.setVisibility(View.GONE);
            ivSelectedImg.setVisibility(View.VISIBLE);
            return;
        } else
            finish();
    }

    @Override
    public void requestCompletedWithJobID(String response){
        String jobID = parser.ParseJobID(response);
        if (jobID.length() > 0)
            hodClient.GetJobResult(jobID);
    }

    @Override
    public void requestCompletedWithContent(String response){
        pbOCRReconizing.setVisibility(View.GONE);
        OCRDocumentResponse resp = parser.ParseOCRDocumentResponse(response);
        if (resp != null) {
            ivSelectedImg.setVisibility(View.GONE);
            llResultContainer.setVisibility(View.VISIBLE);
            String text = "";
            for (OCRDocumentResponse.TextBlock block : resp.text_block) {
                text += block.text;
            }
            edTextResult.setText(text);
        }
    }

    @Override
    public void onErrorOccurred(String errorMessage){
        pbOCRReconizing.setVisibility(View.VISIBLE);
    }

    public void DoStartOCR(View v) {
        pbOCRReconizing.setVisibility(View.VISIBLE);
        if (!mImageFullPathAndName.isEmpty()){
            Map<String,Object> map =  new HashMap<>();
            map.put("file", mImageFullPathAndName);
            map.put("mode", "document_photo");
            hodClient.PostRequest(map, HODApps.OCR_DOCUMENT, HODClient.REQ_MODE.ASYNC);
        } else
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_LONG).show();
    }

    public void DoCloseResult(View v) {
        ivSelectedImg.setVisibility(View.VISIBLE);
        llResultContainer.setVisibility(View.GONE);
    }
    public void CreateLocalImageFolder()
    {
        if (localImagePath.length() == 0)
        {
            localImagePath = getFilesDir().getAbsolutePath() + "/orc/";
            File folder = new File(localImagePath);
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (!success)
                Toast.makeText(this, "Cannot create local folder", Toast.LENGTH_LONG).show();
        }
    }
    public Bitmap decodeFile(File file) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;
        int mImageRealWidth = options.outWidth;
        int mImageRealHeight = options.outHeight;
        Bitmap pic = null;
        try {
            pic = BitmapFactory.decodeFile(file.getPath(), options);
        } catch (Exception ex) {
            Log.e("MainActivity", ex.getMessage());
        }
        return pic;
    }
    public Bitmap rescaleBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    private Bitmap rotateBitmap(Bitmap pic, int deg) {
        Matrix rotate90DegAntiClock = new Matrix();
        rotate90DegAntiClock.preRotate(deg);
        Bitmap newPic = Bitmap.createBitmap(pic, 0, 0, pic.getWidth(), pic.getHeight(), rotate90DegAntiClock, true);
        return newPic;
    }

    private String SaveImage(Bitmap image)
    {
        String fileName = localImagePath + "imagetoocr.jpg";
        try {

            File file = new File(fileName);
            FileOutputStream fileStream = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.JPEG, 100, fileStream);
            try {
                fileStream.flush();
                fileStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;
    }
    public void DoRotateImage(View view) {
        File imageFile = new File(mImageFullPathAndName);
        Bitmap bitmap = decodeFile(imageFile);
        bitmap = rotateBitmap(bitmap, 90);
        SaveImage(bitmap);
        ivSelectedImg.setImageBitmap(bitmap);
    }
    public void DoTakePhoto(View view) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, TAKE_PICTURE);
    }

    public void DoShowSelectImage(View v) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE || requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mImageFullPathAndName = cursor.getString(columnIndex);
                cursor.close();
                File file = new File(mImageFullPathAndName);
                Bitmap mCurrentSelectedBitmap = decodeFile(file);

                if (mCurrentSelectedBitmap != null) {
                    // display the full size image
                    ivSelectedImg.setImageBitmap(mCurrentSelectedBitmap);
                    // scale the image
                    // let check the resolution of the image.
                    // If it's too large, we can optimize it
                    int w = mCurrentSelectedBitmap.getWidth();
                    int h = mCurrentSelectedBitmap.getHeight();

                    int length = (w > h) ? w : h;
                    if (length > OPTIMIZED_LENGTH) {
                        // let's resize the image
                        float ratio = (float) w / h;
                        int newW, newH = 0;

                        if (ratio > 1.0) {
                            newW = OPTIMIZED_LENGTH;
                            newH = (int) (OPTIMIZED_LENGTH / ratio);
                        } else {
                            newH = OPTIMIZED_LENGTH;
                            newW = (int) (OPTIMIZED_LENGTH * ratio);
                        }
                        mCurrentSelectedBitmap = rescaleBitmap(mCurrentSelectedBitmap, newW, newH);
                    }
                    // let save the new image to our local folder
                    mImageFullPathAndName = SaveImage(mCurrentSelectedBitmap);
                    LinearLayout buttonpanel = (LinearLayout) findViewById(R.id.llcamerabuttons);
                    buttonpanel.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
