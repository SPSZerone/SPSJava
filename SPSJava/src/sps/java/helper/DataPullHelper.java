package sps.java.helper;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * 数据获取
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 *
 */
public class DataPullHelper
{
	/**
	 * 下载文件时的buffer
	 * @version added in [ 1, 1.0.0 ]
	 */
	public static final int BUFFER_SIZE = 8192;
	/**
	 * 数据获取模式
	 * @version added in [ 1, 1.0.0 ]
	 */
	public enum PullMode
	{
		PULL_DATA_ONLY,
		DOWNLOAD,
	}
	
	/**
	 * 执行回调，每次Read一次进行一次回调
	 * @version added in [ 1, 1.0.0 ]
	 */
	// CBC : CALL BACK CODE
	public enum ExeCBCRead
	{
		/**
		 * 返回该值，将终止数据抓取
		 */
		BREAK,
		
		/**
		 * 返回该值，将继续数据抓取
		 */
		CONTINUE,
	}

	/**
	 * 数据获取结果
	 * @version added in [ 1, 1.0.0 ]
	 */
	public enum ExeResult
	{		
		OK,

		UNKNOWN,
		
		UNKNOWN_PULL_MODE,
		
		INTERRUPT_BY_CALLBACK,
		
		URL_EMPTY,
		DES_FILE_EMPTY,
		DES_FILE_MAKE_DIR_FAIL,
		EXCEPTION,
		EXCEPTION_IO,
		EXCEPTION_MALFORMED_URL,
	}
	

	/**
	 * 数据获取回调
	 * @version added in [ 1, 1.0.0 ]
	 */
	protected DataPullHelperListener m_Listener = null;
	/**
	 * 数据获取模式
	 * @version added in [ 1, 1.0.0 ]
	 */
	protected PullMode m_ePullMode = PullMode.PULL_DATA_ONLY;	
	/**
	 * 数据获取URL
	 * @version added in [ 1, 1.0.0 ]
	 */
	protected String m_strUrl = null;
	/**
	 * 数据下载模式时保存到本地的文件
	 * @version added in [ 1, 1.0.0 ]
	 * @see #m_ePullMode
	 */
	protected String m_strDesFile = null; 
	/**
	 * 数据下载模式时的buffer
	 * @version added in [ 1, 1.0.0 ]
	 * @see #m_ePullMode
	 */
	protected int m_nBufferSize = BUFFER_SIZE;
	/**
	 * 数据下载模式时的当前下载的文件的大小（字节）
	 * @version added in [ 1, 1.0.0 ]
	 * @see #m_ePullMode
	 */
	protected long m_nDataSize = 0;
	/**
	 * 数据下载模式时文件已下载的大小
	 * @version added in [ 1, 1.0.0 ]
	 * @see #m_ePullMode
	 */
	protected long m_nDownloadedSize = 0;
	/**
	 * 数据下载模式时文件已下载的进度[ 0.0f ~ 1.0f ]
	 * @version added in [ 1, 1.0.0 ]
	 * @see #m_ePullMode
	 */
	protected float m_fProgress = 0.0f;
	
	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public DataPullHelper( PullMode ePullMode )
	{
		m_ePullMode = ePullMode;
	}
	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public DataPullHelper( DataPullHelperListener listener, PullMode ePullMode, String strUrl )
	{
		m_Listener = listener;
		m_ePullMode = ePullMode;
		m_strUrl = strUrl;
	}

	/**
	 * 执行
	 * @version added in [ 1, 1.0.0 ]
	 */
	public ExeResult Execute()
	{
		if ( m_ePullMode == PullMode.PULL_DATA_ONLY )
		{
			return Execute_PULL_DATA_ONLY();
		}

		if ( m_ePullMode == PullMode.DOWNLOAD )
		{
			return Execute_DOWNLOAD();
		}
		
		return ExeResult.UNKNOWN_PULL_MODE;
	}

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	@SuppressWarnings("deprecation")
	protected ExeResult Execute_PULL_DATA_ONLY()
	{
		if ( IsUrlEmpty() )
		{
			return ExeResult.URL_EMPTY;
		}
		
		ExeResult eResultCode = ExeResult.UNKNOWN;
		
		HttpURLConnection httpUrl = null;
		URL url = null;
		DataInputStream dis = null;

		try
		{
			url = new URL( m_strUrl );
			httpUrl = ( HttpURLConnection ) url.openConnection();
			httpUrl.setConnectTimeout(5000);
			httpUrl.connect();
			dis = new DataInputStream(httpUrl.getInputStream());
			
			String strReadLine = null;
			boolean isBreakByCB = false;
			while ( ( strReadLine = dis.readLine() ) != null )
			{
				if ( m_Listener == null )
				{
					continue;
				}
				
				if ( m_Listener.DataPullHelper_Version_ReadLine( this, strReadLine ) == ExeCBCRead.BREAK )
				{
					isBreakByCB = true;
					break;
				}
			}

			if ( isBreakByCB )
			{
				eResultCode = ExeResult.INTERRUPT_BY_CALLBACK;
			}
			else
			{
				eResultCode = ExeResult.OK;	
			}
		}
		catch (MalformedURLException e)
		{
			eResultCode = ExeResult.EXCEPTION_MALFORMED_URL;
		}
		catch (IOException e)
		{
			eResultCode = ExeResult.EXCEPTION_IO;
		}
		catch (Exception e)
		{
			eResultCode = ExeResult.EXCEPTION;
		}
		finally
		{
			if ( httpUrl != null )
			{
				httpUrl.disconnect();	
			}
			
			try
			{
				if ( dis != null )
				{
					dis.close();	
				}
			} catch (IOException e)
			{
			}
		}
		
		return eResultCode;
	}

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	protected ExeResult Execute_DOWNLOAD()
	{
		if ( IsUrlEmpty() )
		{
			return ExeResult.URL_EMPTY;
		}
		
		if ( IsDesFileEmpty() )
		{
			return ExeResult.DES_FILE_EMPTY;
		}
		
		
		FixBufferSize();		
		m_nDataSize = 0;
    	m_nDownloadedSize = 0;
    	m_fProgress = 0.0f;
		

		ExeResult eResultCode = ExeResult.UNKNOWN;
				
		
		File desFile = new File( m_strDesFile );
		desFile.getParentFile().mkdir();


		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		HttpURLConnection httpUrl = null;
		URL url = null;
		byte[] buffer = new byte[ m_nBufferSize ];
		int size = 0;


		try
		{
			url = new URL( m_strUrl );
			httpUrl = ( HttpURLConnection ) url.openConnection();
			
			m_nDataSize = httpUrl.getContentLength();
			
			httpUrl.connect();

			bis = new BufferedInputStream( httpUrl.getInputStream() );
			fos = new FileOutputStream( m_strDesFile );

			boolean isBreakByCB = false;
			while ( (size = bis.read( buffer ) ) != -1 )
			{
				fos.write( buffer, 0, size );
				
				m_nDownloadedSize += size;
				if ( m_nDataSize > 0 )
				{
					m_fProgress = (float)(1.0 * m_nDownloadedSize / m_nDataSize);
				}
				
				if ( m_Listener == null )
				{
					continue;
				}

				if ( m_Listener.DataPullHelper_Download_Read( this, buffer ) == ExeCBCRead.BREAK )
				{
					isBreakByCB = true;
					break;
				}
			}

			if ( isBreakByCB )
			{
				eResultCode = ExeResult.INTERRUPT_BY_CALLBACK;
			}
			else
			{
				eResultCode = ExeResult.OK;	
			}
		}
		catch (MalformedURLException e)
		{
			eResultCode = ExeResult.EXCEPTION_MALFORMED_URL;
		}
		catch (IOException e)
		{
			eResultCode = ExeResult.EXCEPTION_IO;
		}
		catch (Exception e)
		{
			eResultCode = ExeResult.EXCEPTION;
		}
		finally
		{
			if ( httpUrl != null )
			{
				httpUrl.disconnect();
			}
			try
			{
				if ( fos != null )
				{
					fos.close();	
				}
				if ( bis != null )
				{
					bis.close();	
				}
			} catch (IOException e)
			{

			}
		}


		return eResultCode;
	}
	

	/**
	 * 获取当前数据获取模式
	 * @version added in [ 1, 1.0.0 ]
	 */
	public PullMode GetPullMode()
	{
		return m_ePullMode;
	}

	/**
	 * 设置当前数据获取模式
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetPullMode(PullMode ePullMode)
	{
		m_ePullMode = ePullMode;
	}

	/**
	 * 设置Listener
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetListener( DataPullHelperListener listener )
	{
		m_Listener = listener;
	}

	/**
	 * 获取Listener
	 * @version added in [ 1, 1.0.0 ]
	 */
	public DataPullHelperListener GetListener()
	{
		return m_Listener;
	}

	/**
	 * 设置URL
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetUrl( String strUrl )
	{
		m_strUrl = strUrl;
	}

	/**
	 * 获取URL
	 * @version added in [ 1, 1.0.0 ]
	 */
	public String GetUrl()
	{
		return m_strUrl;	
	}

	/**
	 * URL是否为空
	 * @version added in [ 1, 1.0.0 ]
	 */
	public boolean IsUrlEmpty()
	{
		if ( m_strUrl == null )
		{
			return true;
		}
		
		return m_strUrl.length() <= 0;
	}

	/**
	 * 获取下载到本地的文件
	 * @version added in [ 1, 1.0.0 ]
	 */
	public String GetDesFile()
	{
		return m_strDesFile;
	}

	/**
	 * 设置下载到本地的文件
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetDesFile(String strDesFile)
	{
		m_strDesFile = strDesFile;
	}

	/**
	 * 下载到本地的文件是否为空
	 * @version added in [ 1, 1.0.0 ]
	 */
	public boolean IsDesFileEmpty()
	{
		if ( m_strDesFile == null )
		{
			return true;
		}
		
		return m_strDesFile.length() <= 0;
	}

	/**
	 * 获取下载文件时的buffer
	 * @version added in [ 1, 1.0.0 ]
	 */
	public int GetBufferSize()
	{
		return m_nBufferSize;
	}

	/**
	 * 设置下载文件时的buffer
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetBufferSize(int nBufferSize)
	{
		m_nBufferSize = nBufferSize;
	}

	/**
	 * 修正下载文件时的buffer
	 * @version added in [ 1, 1.0.0 ]
	 */
	public int FixBufferSize()
	{
		if ( m_nBufferSize > 0 )
		{
			return 1;
		}
		m_nBufferSize = BUFFER_SIZE;
		return 0;
	}

	/**
	 * 获取文件下载进度
	 * @version added in [ 1, 1.0.0 ]
	 */
	public float GetProgress()
	{
		return m_fProgress;
	}

	/**
	 * 获取下载的文件总大小（字节）
	 * @version added in [ 1, 1.0.0 ]
	 */
	public long GetDataSize()
	{
		return m_nDataSize;
	}

	/**
	 * 获取文件已下载的大小（字节）
	 * @version added in [ 1, 1.0.0 ]
	 */
	public long GetDownloadedSize()
	{
		return m_nDownloadedSize;
	}
}
