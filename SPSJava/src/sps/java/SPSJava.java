package sps.java;

/**
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]<br><br>
 * 版本列表
 * <ol>
 * 		<li>[ 1, 1.0.0 ]</li>
 * </ol>
 * 
 */
public class SPSJava
{
	/**
	 * 版本号
	 * @version added in [ 1, 1.0.0 ]
	 */
	public static final int VERSION_CODE = 1;
	
	/**
	 * 版本名称
	 * @version added in [ 1, 1.0.0 ]
	 */
	public static final String VERSION_NAME = "1.0.0";
	
	/**
	 * 版本信息
	 * @version added in [ 1, 1.0.0 ]
	 */
	public static String get_version_infor()
	{
		return "[ " + VERSION_CODE + ", " + VERSION_NAME + " ]";
	}
}
