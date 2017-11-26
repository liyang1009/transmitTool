package com.example.liyang.transmittool;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Build;

import cloudstorage.QiNiuUpload;

public class mainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        String renderString ="/sdcard/Huawei/MagazineUnlock/"; //findSpecifyFolder() + "/100CANON/";
        File imageDirectory = new File(renderString);
        File[] images = imageDirectory.listFiles();
        if (images != null) {
            for (File item : images
                    ) {
                QiNiuUpload upload = new QiNiuUpload();
                upload.execute(renderString+item.getName());
            }

        }


        TextView renderContainer = (TextView) this.findViewById(R.id.Text);
        renderContainer.setText(renderString);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String findSpecifyFolder() {
        StringBuffer detail = new StringBuffer("");
        String rootPath = extractUsbMount();
        if (rootPath != null) {
            File rootFile = new File(rootPath);
            File[] children = rootFile.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.getName().indexOf("DCIM") > -1)
                        return child.getAbsolutePath();
                }


            }

        }
        return null;


    }

    /**
     * @return
     */
    private String extractUsbMount() {

        String commandResult = null;
        String usb = null;
        String usbRootPath = null;
        boolean isFind = false;
        int loopCount = 0;

        while (true) {

            commandResult = this.executeCommand();
            //
            if ((commandResult.indexOf("/mnt/media_rw") > -1)) {
                isFind = true;
            }
            if (isFind || loopCount == 4) {
                break;
            }
            try {
                Thread.sleep(2500);
                loopCount++;
            } catch (InterruptedException e) {

            }

        }
        if (isFind) {
            String[] filesystem = commandResult.split("\n");
            String filesystemLastItem = filesystem[filesystem.length - 1];
            String[] escapeSplitItems = filesystemLastItem.split(" ");
            return escapeSplitItems[escapeSplitItems.length - 1];
        } else {
            return null;
        }


    }

    /**
     * 执行df命令查看系统的mount信息
     *
     * @return
     */
    public String executeCommand() {
        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec("df");

            // Reads stdout.
            // NOTE: You can write to stdin of the command using
            //       process.getOutputStream().
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();
            String printStr = output.toString();
            System.out.println(printStr);
            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.liyang.transmittool/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.liyang.transmittool/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
