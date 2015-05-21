package sps.java.sort;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;



/**
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 */
public class StringComparator implements Comparator<String>
{
	public enum SortType
	{
		ASCEND, 					// 升序
		DESCEND					// 降序
	}
	

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	private Locale m_locale = Locale.CHINA;
	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	private SortType m_eSortType = SortType.ASCEND;

	
	
	@Override
	public int compare( String strContent1, String strContent2 )
	{
		Collator collator = Collator.getInstance( m_locale );

		if ( m_eSortType == SortType.DESCEND )
		{
			return collator.compare( strContent2, strContent1 );
		}
		
		// 默认升序
		return collator.compare( strContent1, strContent2 );
	}


	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetLocale( Locale locale )
	{
		m_locale = locale;
	}

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public Locale GetLocale()
	{
		return m_locale;
	}
	

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public void SetSortType( SortType eSortType )
	{
		m_eSortType = eSortType;
	}

	/**
	 * @version added in [ 1, 1.0.0 ]
	 */
	public SortType GetSortType()
	{
		return m_eSortType;
	}
}
