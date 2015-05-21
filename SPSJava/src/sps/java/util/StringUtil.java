package sps.java.util;

import java.text.Collator;
import java.util.Locale;



/**
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 */
public class StringUtil
{
	/**
	 * 字符串比较
	 * @param strContent1
	 * @param strContent2
	 * @return
	 */
	public static int compare( Locale locale, String strContent1, String strContent2 )
	{
		Collator collator = Collator.getInstance( locale );
		return collator.compare( strContent1, strContent2 );
	}
}
