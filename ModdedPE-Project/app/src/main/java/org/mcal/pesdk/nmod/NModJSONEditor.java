package org.mcal.pesdk.nmod;

import android.content.Context;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class NModJSONEditor
{
    private NMod mTargetNMod;
    private NModFilePathManager mManager;
    private File[] mParents;

    public NModJSONEditor(Context context, NMod nmod, File[] parents)
    {
        mTargetNMod = nmod;
        mManager = new NModFilePathManager(context);
        mParents = parents;
    }

    public File edit()throws IOException,JSONException
    {
        File dir = mManager.getNModJsonDir();
        dir.mkdirs();
        File file = mManager.getNModJsonPath(mTargetNMod);
        file.createNewFile();
        ZipOutputStream zipOutPut = new ZipOutputStream(new FileOutputStream(file));
        zipOutPut.putNextEntry(new ZipEntry("AndroidManifest.xml"));
        zipOutPut.closeEntry();
        for (NMod.NModJsonEditBean jsonEdit:mTargetNMod.getInfo().json_edit)
        {
            String src = readJsonFromParents(jsonEdit.path);
            String srcThis = readJsonFromThis(jsonEdit.path);
            if (jsonEdit.mode.equals(NMod.NModJsonEditBean.MODE_REPLACE))
            {
                zipOutPut.putNextEntry(new ZipEntry("assets" + File.separator + jsonEdit.path));
                zipOutPut.write(srcThis.getBytes());
                zipOutPut.closeEntry();
            }
            else if (jsonEdit.mode.equals(NMod.NModJsonEditBean.MODE_MERGE))
			{
                JSONMerger merger = new JSONMerger(src, srcThis);
                String result = merger.merge();
                zipOutPut.putNextEntry(new ZipEntry("assets" + File.separator + jsonEdit.path));
                zipOutPut.write(result.getBytes());
                zipOutPut.closeEntry();
            }
        }
        zipOutPut.close();
        return file;
    }

    private String readJsonFromParents(String path)throws IOException
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

    private String readJsonFromThis(String path)throws IOException
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
