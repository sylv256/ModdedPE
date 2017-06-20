package org.mcal.moddedpe.utils;
import android.app.*;
import android.content.*;
import java.util.*;
import org.mcal.moddedpe.app.*;
import android.content.res.*;

public class I18n
{
	public static void setLanguage(Activity context)
	{
		int type = new UtilsSettings(context).getLanguageType();

		Locale defaultLocale = context.getResources().getConfiguration().locale;
		Configuration config = context.getResources().getConfiguration();

		switch (type)
		{
			case 0:
			default:
				config.locale = Locale.getDefault();
				break;
			case 1:
				config.locale = Locale.ENGLISH;
				break;
			case 2:
				config.locale = Locale.SIMPLIFIED_CHINESE;
				break;
			case 3:
				config.locale = Locale.JAPANESE;
				break;
			case 4:
				config.locale = new Locale("ru", "RU");
				break;
			case 5:
				config.locale = Locale.TRADITIONAL_CHINESE;
				break;
			case 6:
				config.locale = new Locale("tu");
				break;
			case 7:
				config.locale = new Locale("pt");
				break;
		}
		if (!defaultLocale.equals(config.locale))
			context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

	}
}
