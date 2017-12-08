package cloudstorage;

import android.graphics.Bitmap;
import android.widget.ProgressBar;

import com.example.liyang.transmittool.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import controller.ApplicationController;
import id.zelory.compressor.Compressor;

/**
 * Created by liyang on 11/25/17.
 * upload the local file to  Cloud storage
 * base page implement the asynctask's ability
 */
public abstract class UploadCloud {

    //WeakReference<Activity> mWeakActivity;
   // ProgressBar progressBar;
    WeakReference<ApplicationController> callBack;

    public UploadCloud(ApplicationController applicationController) {
        this.callBack = new WeakReference<ApplicationController>(applicationController);
    }

    public abstract void uoloadFile(File file);


    public void stopTasks() {
    }

    protected byte[] getCompressedFileBytes(File uploadFile) {
        Bitmap compressedImageBitmap = null;
        try {
            compressedImageBitmap = new Compressor(callBack.get().getActivity()).compressToBitmap(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        compressedImageBitmap.getRowBytes();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        compressedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     * dynamically add the progress to the relativelayout component in  the activity
     *
     * @param marginHeight the height of the will be created progressbar
     */
    public void createProgressBar(int marginHeight) {

        //progressBar = (ProgressBar) callBack.get().getActivity().findViewById(R.id.determinateBar);
        // progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

//        RelativeLayout relativelayout = (RelativeLayout) mWeakActivity.get().findViewById(R.id.viewcontent);
//        progressBar = new ProgressBar(mWeakActivity.get(), null, android.R.attr.progressBarStyleHorizontal);
//        RelativeLayout.LayoutParams layoutparams = new RelativeLayout.LayoutParams(700, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        marginHeight += 20;
//        layoutparams.setMargins(0, marginHeight, 0, 0);
//        progressBar.setLayoutParams(layoutparams);
//        RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        progressBar.setScaleY(3f);
//        relativelayout.addView(progressBar);
//        TextView textView = new TextView(mWeakActivity.get());
//        textView.setLayoutParams(lparams);
//
//        textView.setText(this.uploadFile.getName());
//        relativelayout.addView(textView);


    }
}
