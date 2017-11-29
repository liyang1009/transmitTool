package bootstrap;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import cloudstorage.QiNiuUpload;
import dao.DBHelper;
import dao.DBService;
import transmit.TransmitHelper;

/**
 * Created by liyang on 11/27/17.
 */
public class Application {


    BlockingDeque<String> filePaths = new LinkedBlockingDeque<>();
    TransmitHelper transmitHelper;

    public Application() {
        transmitHelper = new TransmitHelper();
    }

    /**
     * interval loop the specify directory find
     * have new file ready to send
     */
    public void start(Activity activity) throws InterruptedException {
        String rootPath = transmitHelper.findSpecifyFolder() + "/100CANON/";
        String childPath = null;
        if (rootPath != null) {
            File root = new File(rootPath);
            DBService dbService = new DBService(activity.getApplicationContext());
            while (true) {
                File[] children = root.listFiles();
                for (File child : children
                        ) {
                    childPath = child.getAbsolutePath();

                    if (!dbService.fileIsExists(new String[]{childPath})) {
                        dbService.addTransmitRecord(childPath);
                        new QiNiuUpload(activity, child).execute(child);

                    }
                }
                Thread.sleep(1000);
            }
        }

    }

    /**
     * interval inspect the file sotrage state initialization is pengding(2)
     * and result either success(1) or fail (0)
     */
    void inspectStorageLoop() {

    }

}
