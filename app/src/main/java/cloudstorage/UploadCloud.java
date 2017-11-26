package cloudstorage;

import android.os.AsyncTask;

/**
 * Created by liyang on 11/25/17.
 * upload the local file to  Cloud storage
 */
public abstract  class UploadCloud extends AsyncTask<String, Integer, Long> {

    abstract void uoloadFile(String filePath);
    @Override
    protected Long doInBackground(String... params) {
        uoloadFile(params[0]);
        return null;
    }
}
