package sss.am71113363.testFb;

import android.app.*;
import android.content.*;
import android.os.*;
import sss.am71113363.filebrowser.*;

public class FolderExplore extends Activity implements FCallback
{
	
	FileBrowser exp=null;
	private String extFile=null;
	
	private void replyIntent(String path,boolean isCancel)
	{
         Intent reply=new Intent();
		 if(isCancel==false)
		 {
			 reply.putExtra("PATH",path);
		     setResult(RESULT_OK,reply);
		 }
		 else
		 {
		     setResult(RESULT_CANCELED);
		 }
		 finish();
	}
	@Override
	public char getFileSeparator()
	{
		//return '\\';
		return '/';
	}

	@Override
	public void DONE(String path, boolean isCancel)
	{
		replyIntent(path,isCancel);
		finish();
	}

	@Override
	public boolean isFileBlur(String name)
	{
		if(extFile!=null)
		{
             if(name.toLowerCase().endsWith(extFile))
		     return false;
		 return true;
		}
		return false;
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		String home=null;
		boolean selectFiles=false;;
		Intent intent = getIntent();
		if(intent != null)
		{
	       home=intent.getStringExtra("HOME");
		   selectFiles=intent.getBooleanExtra("ISFILE",false);
		   if(selectFiles==true)
		      extFile=intent.getStringExtra("EXT");
		}
		exp=new FileBrowser(this,this);
		setContentView(exp.create());
		exp.RUN(home,selectFiles);
	
    }

	@Override
	public void onBackPressed()
	{
		if(exp!=null)
		{
			if(exp.onBackPressed() == false)
			  return;
		}
	
		super.onBackPressed();
	}
	
	
}
