package sps.java.test;

import java.util.ArrayList;
import java.util.Collections;

import sps.java.sort.StringComparator;



/**
 * @author 陈威
 * @version added in [ 1, 1.0.0 ]
 */
public class StringComparatorTest
{
	public static void main( String [] args )
	{
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add( "路飞" );
		nameList.add( "Monkey•D•Luffy" );
		nameList.add( "索罗" );
		nameList.add( "Roronoa Zoro" );
		nameList.add( "娜美" );
		nameList.add( "Nami" );
		nameList.add( "乌索普" );
		nameList.add( "Usopp" );
		nameList.add( "山治" );
		nameList.add( "Sanji" );
		nameList.add( "乔巴" );
		nameList.add( "Tony Tony Chopper" );
		nameList.add( "罗宾" );
		nameList.add( "Nico•Robin" );
		nameList.add( "弗兰奇" );
		nameList.add( "Franky" );
		nameList.add( "布鲁克" );
		nameList.add( "Brook" );

		System.out.println( "Before Sort >> " + nameList );
		
		StringComparator strComparator = new StringComparator();
		//strComparator.setSortType( SORT_TYPE.DESCEND );
		Collections.sort( nameList, strComparator );
		
		System.out.println( "After Sort >> " + nameList );
	}
	
}
