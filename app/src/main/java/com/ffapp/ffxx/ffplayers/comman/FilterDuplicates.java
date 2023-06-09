package com.ffapp.ffxx.ffplayers.comman;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterDuplicates {
    private static MessageDigest messageDigest;
    private static String TAG = "FindDuplicates";
    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("cannot initialize SHA-512 hash function", e);
        }
    }
    public static void findDuplicateFiles(Map<String, List<String>> filesList, File directory, Context context, ArrayList<File> files) {


        for (File dirChild : files) {
            // Iterate all file sub directories recursively
            if (dirChild.isDirectory()) {
                findDuplicateFiles(filesList, dirChild, context, files);
            } else {
                try {
                    // Read file as bytes

                    FileInputStream fileInput = new FileInputStream(dirChild);
                    byte fileData[] = new byte[(int) dirChild.length()];
                    fileInput.read(fileData);
                    fileInput.close();
                    // Create unique hash for current file
                    String uniqueFileHash = new BigInteger(1, messageDigest.digest(fileData)).toString(16);
                    Log.e(TAG, "findDuplicateFiles: uniqueFileHash" + uniqueFileHash);
                    List<String> identicalList = filesList.get(uniqueFileHash);
                    if (identicalList == null) {
                        identicalList = new LinkedList<String>();
                    }
                    // Add path to list
                    identicalList.add(dirChild.getAbsolutePath());
                    // push updated list to Hash table
                    filesList.put(uniqueFileHash, identicalList);


                } catch (IOException e) {
                    throw new RuntimeException("cannot read file " + dirChild.getAbsolutePath(), e);
                } catch (OutOfMemoryError e) {
                    Log.e(TAG, "findDuplicateFiles: " + e.getMessage());
                    throw new RuntimeException(e.getMessage());

                }
            }
        }
    }
}
