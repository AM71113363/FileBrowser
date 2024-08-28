package sss.am71113363.filebrowser;
//Copyright AM71113363
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import java.io.*;

public abstract class DATABASE
{
	private static String home="storage/emulated/0";
	public static List<String> history=new ArrayList<String>();
	
	public static List<BAG> list= new ArrayList<BAG>();

	//history
	public static void addIfEmpty(String entry)
	{
		if(history.size()==0)
		{
		    history.add(entry);
		}
	}
	public static boolean RemoveLastHistory()
	{
		int len=history.size();
		
		if(len<=1) //only 1,its the root
			return false;//removing root is not allowed
		
		history.remove(len-1);
	   return true;
	}
	public static void add(String entry)
	{
		history.add(entry);
	}
	public static List<String> GetHistory()
	{
		return history;
	}
	public static String GetHistory(int index)
	{
		return history.get(index);
	}
	public static int GetHistoryLen()
	{
		return history.size();
	}
	public static String GetLastHistory()
	{
		int len=history.size();
		if(len == 0)
		{
			history.add(home);
			return home;
		}
		return history.get(len-1);
	}
	
	//list
	public static void add(BAG entry)
	{
		list.add(entry);
	}
	public static List<BAG> GetList()
	{
		return list;
	}
	public static int GetListLen()
	{
		return list.size();
	}
	public static BAG GetList(int index)
	{
		return list.get(index);
	}
	
	//reset
	public static void ResetList()
	{
		list.clear();
	}
	public static void ResetHistory()
	{
		history.clear();
	}
	
}
