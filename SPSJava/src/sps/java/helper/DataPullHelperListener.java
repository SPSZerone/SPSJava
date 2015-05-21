package sps.java.helper;

import sps.java.helper.DataPullHelper.ExeCBCRead;



/**
 * DataPullHelper数据抓取回调
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 *
 */
public interface DataPullHelperListener
{
	/**
	 * 版本数据获取回调
	 * @version added in [ 1, 1.0.0 ]
	 * @param dataPullHelper 当前的DataPullHelper
	 * @param strData 当前读取的数据（一行）
	 */
	public ExeCBCRead DataPullHelper_Version_ReadLine( DataPullHelper dataPullHelper, String strData );
	
	/**
	 * 下载文件回调
	 * @version added in [ 1, 1.0.0 ]
	 * @param dataPullHelper 当前的DataPullHelper
	 * @param datas 当前读取的数据
	 */
	public ExeCBCRead DataPullHelper_Download_Read( DataPullHelper dataPullHelper, byte [] datas );
}
