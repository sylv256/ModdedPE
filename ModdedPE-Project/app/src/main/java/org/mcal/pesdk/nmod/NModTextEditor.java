package org.mcal.pesdk.nmod;

import android.content.*;
import java.io.*;
import java.util.zip.*;

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
                zipOutPut.putNextEntry(new ZipEntry(textEdit.path));
                zipOutPut.write(srcThis.getBytes());
                zipOutPut.closeEntry();
            }
            else if (textEdit.mode.equals(NMod.NModTextEditBean.MODE_APPEND))
            {
                String result = src + srcThis;
                zipOutPut.putNextEntry(new ZipEntry(textEdit.path));
                zipOutPut.write(result.getBytes());
                zipOutPut.closeEntry();
            }
            else if (textEdit.mode.equals(NMod.NModTextEditBean.MODE_PREPEND))
            {
                String result = srcThis + src;
                zipOutPut.putNextEntry(new ZipEntry(textEdit.path));
                zipOutPut.write(result.getBytes());
                zipOutPut.closeEntry();
            }
        }
        zipOutPut.close();
        return file;
    }

    private String readTextFromParents(String path)throws IOException
    {
        for (File parentItem:mParents)
        {
            ZipFile zipFile = new ZipFile(parentItem);
            ZipEntry entry = zipFile.getEntry("assets" + File.separator + path);
            if (entry == null)
                continue;
            InputStream input = zipFile.getInputStream(entry);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            String tmp = new String(buffer);
            return tmp;
        }
        throw new FileNotFoundException(path);
    }

    private String readTextFromThis(String path)throws IOException
    {
        InputStream input = mTargetNMod.getAssets().open(path);
        byte[] buffer = new byte[input.available()];
        input.read(buffer);
        String tmp = new String(buffer);
        return tmp;
    }
}
