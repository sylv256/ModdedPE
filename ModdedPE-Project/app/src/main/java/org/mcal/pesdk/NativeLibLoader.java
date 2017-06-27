package org.mcal.pesdk;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NativeLibLoader
{
    private static final String DIR_LIBS = "app_native_libs";

    public static void load(Context context,String filePath)throws IOException
    {
        File dir = new File(context.getFilesDir(),DIR_LIBS);
        dir.mkdirs();
        File file = new File(dir,new File(filePath).getName());
        file.createNewFile();
        FileInputStream input = new FileInputStream(filePath);
        FileOutputStream output = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int byteRead = -1;
        while( (byteRead = input.read(buffer)) > 0)
        {
            output.write(buffer,0,byteRead);
        }
        input.close();
        output.close();
        System.load(file.getAbsolutePath());
    }
}