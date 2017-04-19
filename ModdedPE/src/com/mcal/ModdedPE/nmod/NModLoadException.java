package com.mcal.ModdedPE.nmod;
import android.content.res.*;
import com.mcal.ModdedPE.*;
import com.mcal.ModdedPE.nativeapi.*;

public abstract class NModLoadException extends Exception
{
	public static final int UNKNOW=0;
	public static final int BAD_JNIONLOAD_RETURN_VALUE=1;
	public static final int CANNOT_LOCATE_SYMBOL=2;
	public static final int CANNOT_DLOPEN_LIB=3;
	public static final int CANNOT_FIND_LIB=4;
	public static final int NO_FILE_FOUND=5;
	public static final int BAD_MANIFEST_GRAMMAR=6;
	public static final int BAD_JSON_GRAMMAR=7;
	public static final int BAD_IMAGE_SIZE=8;
	public static final int IMAGE_DECODE=9;
	
	protected int type;
	protected Throwable defaultMsg;
	protected Resources resources;
	
	protected NModLoadException(int type,Resources r)
	{
		this.type=type;
		this.resources=r;
	}
	
	protected NModLoadException(int type,Throwable msg,Resources r)
	{
		this.type=type;
		this.defaultMsg=msg;
		this.resources=r;
	}

	public static NModLoadException getImageDecode(Throwable throwable, Resources r, String version_description_image)
	{
		return new ImageDecodeException(throwable,r,version_description_image);
	}

	public static NModLoadException getBadJsonGrammar(Throwable throwable, Resources r,String jsonName)
	{
		return new BadJsonGrammarException(throwable,r,jsonName);
	}
	
	public static NModLoadException getBadJNIReturnValue(Throwable throwable,Resources r)
	{
		return new BadOnLoadReturnValueException(throwable,r);
	}
	
	public static NModLoadException getBadManifestGrammar(Throwable throwable,Resources r)
	{
		return new BadManifestGrammarException(throwable,r);
	}
	
	public static NModLoadException getCannotLocateSmbol(Throwable throwable,Resources r)
	{
		return new SymbolNotFoundException(throwable,r);
	}
	
	public static NModLoadException getBadImageSize(Resources r)
	{
		return new BadImageSizeException(null,r);
	}
	
	public static NModLoadException getUnknow(Throwable throwable,Resources r)
	{
		return new UnknowException(throwable,r);
	}
	
	public static NModLoadException getFileNotFound(Throwable throwable,Resources r,String fileName)
	{
		return new FileNotFoundException(throwable,r,fileName);
	}
	
	public static NModLoadException getDlopenFail(Throwable throwable,Resources r)
	{
		return new DlopenFailException(throwable,r);
	}
	
	public static NModLoadException getLibNotFound(Throwable throwable,Resources r)
	{
		return new ElfFileNotFoundException(throwable,r);
	}
	
	public static NModLoadException getLoadElfFail(Throwable throwable,Resources r)
	{
		if(throwable.toString().indexOf("could not load library")!=-1)
			return getDlopenFail(throwable,r);
		if(throwable.toString().indexOf("cannot locate symbol")!=-1)
			return getCannotLocateSmbol(throwable,r);
		if(throwable.toString().indexOf("dlopen failed: library \"")!=-1)
			return getLibNotFound(throwable,r);
		return getUnknow(throwable,r);
	}
	
	public int getType()
	{
		return type;
	}

	@Override
	public String toString()
	{
		if(defaultMsg!=null)
			return defaultMsg.toString();
		return super.toString();
	}
	
	abstract public String getBugMessage();
	abstract public String getDealMessage();
	
	
	public static class SymbolNotFoundException extends NModLoadException
	{
		private String symbol_mangled;
		private String symbol_demangled;
		
		public SymbolNotFoundException(Throwable t,Resources r)
		{
			super(NModLoadException.CANNOT_LOCATE_SYMBOL,t,r);
			
			String msg=t.toString();
			try
			{
				symbol_mangled=msg.toString().split("\"")[1];
				symbol_demangled=Utils.nativeDemangle(symbol_mangled);
			}
			catch(Throwable t2)
			{
				symbol_mangled=symbol_demangled=new String();
			}
		}
		
		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_symbol_not_found,new String[]{symbol_mangled,symbol_demangled});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_symbol_not_found_d,new String[]{symbol_mangled,symbol_demangled});
		}
	}
	
	public static class BadManifestGrammarException extends NModLoadException
	{
		public BadManifestGrammarException(Throwable t,Resources r)
		{
			super(NModLoadException.BAD_MANIFEST_GRAMMAR,t,r);
		}
		
		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_bad_manifest_grammar);
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_bad_manifest_grammar_d);
		}
	}
	
	public static class BadJsonGrammarException extends NModLoadException
	{
		private String jsonName;
		
		public BadJsonGrammarException(Throwable t,Resources r,String jsonName)
		{
			super(NModLoadException.BAD_JSON_GRAMMAR,t,r);
			
			this.jsonName=jsonName;
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_bad_json_grammar,new String[]{jsonName});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_bad_json_grammar_d,new String[]{jsonName});
		}
	}
	
	public static class ImageDecodeException extends NModLoadException
	{
		private String name;

		public ImageDecodeException(Throwable t,Resources r,String name)
		{
			super(NModLoadException.IMAGE_DECODE,t,r);

			this.name=name;
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_image_decode,new String[]{name});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_image_decode_d,new String[]{name});
		}
	}
	
	public static class BadOnLoadReturnValueException extends NModLoadException
	{
		public BadOnLoadReturnValueException(Throwable t,Resources r)
		{
			super(NModLoadException.BAD_JNIONLOAD_RETURN_VALUE,t,r);
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_bad_jni_return_value);
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_bad_jni_return_value_d);
		}
	}
	
	public static class UnknowException extends NModLoadException
	{
		public UnknowException(Throwable t,Resources r)
		{
			super(NModLoadException.UNKNOW,t,r);
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_unknow);
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_unknow_d);
		}
	}
	
	public static class BadImageSizeException extends NModLoadException
	{
		public BadImageSizeException(Throwable t,Resources r)
		{
			super(NModLoadException.UNKNOW,t,r);
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_bad_image_size);
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_bad_image_size_d);
		}
	}
	
	public static class ElfFileNotFoundException extends NModLoadException
	{
		private String elfFileName;
		
		public ElfFileNotFoundException(Throwable t,Resources r)
		{
			super(NModLoadException.CANNOT_DLOPEN_LIB,t,r);
			
			try
			{
				elfFileName=t.toString().split("\"")[1];
			}
			catch(Throwable t1)
			{
				elfFileName=new String();
			}
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_native_lib_not_found,new String[]{elfFileName});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_native_lib_not_found_d,new String[]{elfFileName});
		}
	}
	
	public static class DlopenFailException extends NModLoadException
	{
		private String elfFileName;
		private String neededElfName;

		public DlopenFailException(Throwable t,Resources r)
		{
			super(NModLoadException.CANNOT_DLOPEN_LIB,t,r);

			try
			{
				elfFileName=t.toString().split("\"")[1];
				neededElfName=t.toString().split("\"")[3];
			}
			catch(Throwable t1)
			{
				elfFileName=neededElfName=new String();
			}
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_dlopen_fail,new String[]{elfFileName,neededElfName});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_dlopen_fail_d,new String[]{elfFileName,neededElfName});
		}
	}
	
	public static class FileNotFoundException extends NModLoadException
	{
		private String fileName;
		
		public FileNotFoundException(Throwable t,Resources r,String fileName)
		{
			super(NModLoadException.NO_FILE_FOUND,t,r);
			
			this.fileName=fileName;
		}

		public String getBugMessage()
		{
			return resources.getString(R.string.load_exception_file_not_found,new String[]{fileName});
		}
		public String getDealMessage()
		{
			return resources.getString(R.string.load_exception_file_not_found_d,new String[]{fileName});
		}
	}
}
