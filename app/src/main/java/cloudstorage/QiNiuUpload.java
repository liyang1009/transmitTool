package cloudstorage;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.liyang.transmittool.R;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import controller.ApplicationController;


/**
 * Created by liyang on 11/26/17.
 */
public class QiNiuUpload extends UploadCloud {
    private static int marginHeight = 100;
    private static Configuration config = new Configuration.Builder().zone(Zone.zone0).build();
    private static UploadManager uploadManager = new UploadManager(config);
    private static String GET_TOKEN_URL = "http://121.43.193.160:8888/getToken?key=";
    private static String SEND_DATA_NOTIFICATION_URL = "http://121.43.193.160:8888/sendLogNotification?logData=";
    public LinkedList<UploadTask> tasks = new LinkedList<>();


    public QiNiuUpload(ApplicationController applicationController) {
        super(applicationController);
        this.createProgressBar(marginHeight += 100);
    }

    public void stopTasks(ApplicationController controller) {
        for (UploadTask uploadTask : tasks
                ) {
            if (uploadTask != null && uploadTask.getStatus() != AsyncTask.Status.FINISHED) {

                uploadTask.cancel(true);
                callBack.get().removeTransmitReord(uploadTask.uploadFile.getAbsolutePath());
            }

        }
    }

    public void uoloadFile(File file) {
        // 重用uploadManager。一般地，只需要创建一个uploadManager对象
        UploadTask uploadTask = new UploadTask(file);
        uploadTask.execute();
        tasks.add(uploadTask);

    }

    public void sendLogNotification(String logData) {
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(SEND_DATA_NOTIFICATION_URL + logData);
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public String getToken(String key) {

        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(GET_TOKEN_URL + key);

            urlConnection = (HttpURLConnection) url
                    .openConnection();
            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            //InputStreamReader isw = new InputStreamReader(in);
            char[] buffer = new char[4096];
            int data;
            StringBuffer dataBuffer = new StringBuffer();
            while ((data = reader.read(buffer)) != -1) {
                dataBuffer.append(buffer, 0, data);
            }
            return dataBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * 显示进度条
     *
     * @param percent
     */
    public void showProgressBarPercent(int percent) {


//        progressBar.setProgress(percent);
    }

    public class UploadTask extends AsyncTask<File, Integer, Long> {

        File uploadFile;

        public UploadTask(File uploadFile) {
            this.uploadFile = uploadFile;
        }

        @Override
        protected Long doInBackground(File... params) {
            //final File uploadFile = params[0];
            String filePath = uploadFile.getAbsolutePath();
            String key = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            String token = getToken(key);
            byte[] data = QiNiuUpload.this.getCompressedFileBytes(uploadFile);
            uploadManager.put(data, key, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            int status = 1;
                            //res包含hash、key等信息，具体字段取决于上传策略的设置
                            if (info.isOK()) {

                                if (callBack.get().getActivity() != null) {
//                                    TextView text = (TextView) callBack.get().getActivity().findViewById(R.id.Text);
//                                    text.setText(text.getText() + "\r\n" + uploadFile.getAbsolutePath() + "successful");
                                    Log.i("qiniu", "Upload Success");
                                }

                            } else {
                                sendLogNotification(info + ",\r\n " + res + "\r\n");
                                status = 0;
                                Log.i("qiniu", "Upload Fail");
                                //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            }
                            //DBHelper.modifyTransmitRecord(QiNiuUpload.this);
                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                        }
                    }, new UploadOptions(null, null, false,
                            new UpProgressHandler() {
                                public void progress(String key, double percent) {
                                    Log.i("qiniu", key + ": " + percent);
                                    callBack.get().showProgressBar((int) percent * 100);
                                    //showProgressBarPercent((int) (percent * 100));
                                }
                            }, null));
            return null;
        }
    }


}
