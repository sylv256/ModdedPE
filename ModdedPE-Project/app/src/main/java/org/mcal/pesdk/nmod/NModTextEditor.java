package org.mcal.pesdk.nmod;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class NModTextEditor
{
    private NMod mTargetNMod;
    private NModFilePathManager mManager;
    private File[] mParents;

    public NModTextEditor(Context context, NMod nmod, File[] parents)
    {
        mTargetNMod = nmod;
        mManager = new NModFilePathManager(context);
        mParents = parents;
    }

    public File edit()throws IOException
    {
        File dir = mManager.getNModTextDir();
        dir.mkdirs();
        File file = mManager.getNModTextPath(mTargetNMod);
        file.createNewFile();
        ZipOutputStream zipOutPut = new ZipOutputStream(new FileOutputStream(file));
        zipOutPut.putNextEntry(new ZipEntry("AndroidManifest.xml"));
        zipOutPut.closeEntry();
        for (NMod.NModTextEditBean textEdit:mTargetNMod.getInfo().text_edit)
        {
            String src = readTextFromParents(textEdit.path);
            String srcThis = readTextFromThis(textEdit.path);
            if (textEdit.mode.equals(NMod.NModTextEditBean.MODE_REPLACE))
            {
                zipOutPut.putNextEntry(new ZipEntry("assets" + File.separator + textEdit.path));
                zipOutPut.write(srcThis.getBytes());
                zipOutPut.closeEntry();
            }
            else if (textEdit.mode.equals(NMod.NModTextEditBean.MODE_APPEND))
            {
                String result = src + srcThis;
                zipOutPut.putNextEntry(new ZipEntry("assets" + File.separator + textEdit.path));
                zipOutPut.write(result.getBytes());
                zipOutPut.closeEntry();
            }
            else if (textEdit.mode.equals(NMod.NModTextEditBean.MODE_PREPEND))
            {
                String result = srcThis + src;
                zipOutPut.putNextEntry(new ZipEntry("assets" + File.separator + textEdit.path));
                zipOutPut.write(result.getBytes());
                zipOutPut.closeEntry();
            }
        }
        zipOutPut.flush();
        zipOutPut.close();
        return file;
    }

    private String readTextFromParents(String path)throws IOException
    {
        for (int index = mParents.length - 1;index>=0;--index)
        {
            File parentItem = mParents[index];
            ZipFile zipFile = new ZipFile(parentItem);
            ZipEntry entry = zipFile.getEntry("assets" + File.separator + path);
            if (entry == null)
                continue;
            InputStream input = zipFile.getInputStream(entry);
            int byteRead ;
            byte[] buffer = new byte[1024];
            String tmp = "";
            while( (byteRead = input.read(buffer))>0)
            {
                tmp += new String(buffer,0,byteRead);
            }
            return tmp;
        }
        throw new FileNotFoundException(path);
    }

    private String readTextFromThis(String path)throws IOException
    {
        InputStream input = mTargetNMod.getAssets().open(path);
        int byteRead ;
        byte[] buffer = new byte[1024];
        String tmp = "";
        while( (byteRead = input.read(buffer))>0)
        {
            tmp += new String(buffer,0,byteRead);
        }
        return tmp;
    }
}
