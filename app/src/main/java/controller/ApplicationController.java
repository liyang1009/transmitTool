package controller;

import android.app.Activity;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.liyang.transmittool.R;

import java.io.File;
import java.lang.ref.WeakReference;

import cloudstorage.QiNiuUpload;
import cloudstorage.UploadCloud;
import dao.DBService;
import transmit.TransmitHelper;

/**
 * Created by liyang on 11/27/17.
 */


public class ApplicationController {

    private WeakReference<Activity> mWeakActivity;
    TransmitHelper transmitHelper;
    Thread action;
    volatile boolean status = true;
    UploadCloud uploadCloud;
    DBService dbService;


    public ApplicationController(Activity activity) {
        transmitHelper = new TransmitHelper();
        this.mWeakActivity = new WeakReference<Activity>(activity);
        uploadCloud = new QiNiuUpload(this);
        dbService = new DBService(mWeakActivity.get().getApplicationContext());
    }

    /**
     * interval loop the specify directory find
     * have new file ready to send
     */
    public void start(Activity activity) throws InterruptedException {

        if (this.action == null && this.status) {
            action = new Thread(new UploadRunable());
            action.start();

        }


    }

    public Activity getActivity() {
        return mWeakActivity.get();
    }

    public void removeTransmitReord(String fileName) {
        dbService.removeTransmitRecord(fileName);
    }

    public void stop() {

        this.status = false;
        this.action = null;
    }

    public void dbResourceDestory() {
        this.dbService.destoryDbConnection();
    }

    /**
     * interval inspect the file sotrage state initialization is pengding(2)
     * and result either success(1) or fail (0)
     */
    void inspectStorageLoop() {
        DBService dbService = new DBService(mWeakActivity.get().getApplicationContext());
    }

    public void showProgressBar(int percent) {

        ProgressBar progressBar = (ProgressBar) this.mWeakActivity.get().findViewById(R.id.determinateBar);
        progressBar.setProgress(percent);
    }

    class UploadRunable implements Runnable {

        public void run() {
            //String rootPath = transmitHelper.findSpecifyFolder() + "/100CANON/";
            String rootPath = "/sdcard/Huawei/MagazineUnlock/";
            String childPath = null;
            if (rootPath != null) {
                File root = new File(rootPath);

                while (status && root.exists()) {
                    File[] children = root.listFiles();
                    for (File child : children) {
                        try {

                            if (!status) {
                                break;
                            }
                            childPath = child.getAbsolutePath();
                            if (!dbService.fileIsExists(new String[]{childPath})) {
                                dbService.addTransmitRecord(childPath);
                                uploadCloud.uoloadFile(child);

                            } else {

                                //Log.i("info", "file exists");
                            }
                        } catch (Exception e) {

                        }
                    }
                    try {

                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                uploadCloud.stopTasks();


            }
        }

    }


}
