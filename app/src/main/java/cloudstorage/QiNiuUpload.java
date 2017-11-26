package cloudstorage;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.common.Zone;

import org.json.JSONObject;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.AsyncTask;

/**
 * Created by liyang on 11/26/17.
 */
public class QiNiuUpload extends UploadCloud {
    private static Configuration config = new Configuration.Builder().zone(Zone.zone0).build();
    private static UploadManager uploadManager = new UploadManager(config);
    private static String GET_TOKEN_URL = "http://121.43.193.160:8888/getToken?key=";
    private static String SEND_DATA_NOTIFICATION_URL = "http://121.43.193.160:8888/sendLogNotification?logData=";

    public void uoloadFile(String path) {


// 重用uploadManager。一般地，只需要创建一个uploadManager对象

        File data = new File(path);
        String key = path.substring(path.lastIndexOf("/") + 1, path.length());
        String token = getToken(key);
        uploadManager.put(data, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                        } else {
                            sendLogNotification(info + ",\r\n " + res + "\r\n");
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
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
        //URLConnection urlConnection = new Url

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
                // data = isw.read();

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


}
