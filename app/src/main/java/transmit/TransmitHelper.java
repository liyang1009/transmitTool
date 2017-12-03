package transmit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by liyang on 11/27/17.
 */
public class TransmitHelper {


    private static int SCAN_MOUNT_INTERVAL = 2500;
    private static int SCAN_FAIL_RETRY_COUNT = 4;
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
            if ((commandResult.indexOf("/mnt/media_rw") > -1)) {
                isFind = true;
            }
            if (isFind || loopCount == SCAN_FAIL_RETRY_COUNT) {
                break;
            }
            try {
                Thread.sleep(SCAN_MOUNT_INTERVAL);
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
}
