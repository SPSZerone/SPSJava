package sps.java.test;

import java.util.Locale;

import sps.java.util.StringUtil;



/**
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 */
public class StringUtilTest
{
	public static void main( String [] args )
	{
		System.out.println( StringUtil.compare( Locale.CHINA, "路飞", "路飞" ) );
		System.out.println( StringUtil.compare( Locale.CHINA, "路飞", "索罗" ) );
		System.out.println( StringUtil.compare( Locale.CHINA, "路飞", "布鲁克" ) );
	}
}
