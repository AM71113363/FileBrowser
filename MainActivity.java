package sss.am71113363.testFb;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import android.content.*;
import android.view.*;


public class MainActivity extends Activity
{
	public final int CODE_PICK_FOLDER = 1000;
	public final int CODE_PICK_FILE   = 1001;
	public final int CODE_PICK_CUSTOM =  1002;
	
	public final int RESULT_CODE      = 2222;
	
	
	
    public String path=null;
	public String ext=null;
    public EditText hEdit;
	
	private void createIntent(boolean isFile,String extFile)
	{
		try
		{
			Intent inten=new Intent("F_BROWSER");
			inten.putExtra("HOME","storage/emulated/0");
			inten.putExtra("ISFILE",isFile);
			if(extFile!=null)
				inten.putExtra("EXT",extFile);
			startActivityForResult(inten,RESULT_CODE);

		}catch( Throwable e)
		{
			Toast.makeText(
				this,
				e.toString(),
				Toast.LENGTH_SHORT)
				.show();
		}
		
	}
	
	private Handler hnd=new Handler()
	{
		@Override
		public void handleMessage(Message ms)
		{

			switch(ms.what)
			{
				case CODE_PICK_FOLDER:
				{
					createIntent(false,null);
				}break;
				case CODE_PICK_FILE:
				{
					createIntent(true,null);
				}break;
				case CODE_PICK_CUSTOM:
				{
					createIntent(true,ext);
				}break;
				
			    case RESULT_CODE:
				{
				 hEdit.setText(path);
				}break;
	        }
		}
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			if(data!=null)
			{
				Bundle b= data.getExtras();
				if(b!=null)
				{
					Object u=data.getExtras().get("PATH");
			        if(u!=null)
			        {
				       path=u.toString();
					   hnd.sendEmptyMessage(RESULT_CODE);
			        }
			    }
			
			}
		}
		else if(resultCode == RESULT_CANCELED)
		{
			path="OP Canceled";
			hnd.sendEmptyMessage(RESULT_CODE);
		}
			
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inf=getMenuInflater();
		inf.inflate(R.menu.options_menu,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.pickFolder:{
				hnd.sendEmptyMessage(CODE_PICK_FOLDER);
			  return true;
			}
			case R.id.pickFile:{
				hnd.sendEmptyMessage(CODE_PICK_FILE);
				return true;
			}
			case R.id.pickMp3:{
				ext=".mp3";
				hnd.sendEmptyMessage(CODE_PICK_CUSTOM);
				return true;
			}
			case R.id.pickTxt:{
				ext=".txt";
				hnd.sendEmptyMessage(CODE_PICK_CUSTOM);
				return true;
			}
			case R.id.pickApk:{
				ext=".apk";
				hnd.sendEmptyMessage(CODE_PICK_CUSTOM);
				return true;
			}
			case R.id.pickJpg:{
				ext=".jpg";
				hnd.sendEmptyMessage(CODE_PICK_CUSTOM);
				return true;
			}
			
		}
		return false;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		LinearLayout ln=new LinearLayout(this);
		LinearLayout.LayoutParams prm=new LinearLayout.LayoutParams(-1,-1);
		hEdit=new EditText(this);
		hEdit.setText("Use Menu Options\r\n[ Note: Check if App has Storage Permissions ]");
		hEdit.setGravity(Gravity.CENTER|Gravity.TOP);
	    ln.addView(hEdit,prm);
		setContentView(ln);
    }

	
}
