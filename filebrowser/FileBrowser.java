package sss.am71113363.filebrowser;

// android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.util.*;
import java.io.*;
// android.util.*;
import android.text.*;
import android.graphics.drawable.*;
// android.graphics.drawable.shapes.*;




public class FileBrowser
{
	//callback
	private FCallback call=null;
	private boolean selectFileEnabled=false;
	

	final static int CODE_DONE  = 7777;
	final static int CODE_CANCEL= 7778;
	final static int CODE_EXPLORE_USE_BACKUP  = 8888;
	final static int CODE_EXPLORE_NO_BACKUP   = 8889;
  
	
	private static String home="storage/emulated/0";

	Context ctx;
	private ScrollView hScroll;
	private Button okBtn;
	private TextView viewPath;
	private static float dpi=2.625f;

	private int int2=2;
	private int int4=4;
	private int int10=10;
	private int int20=20;
	private int int150=150;
	private int int170=170;
	
	private int dpi2px(float px)
	{
		Float calc=Float.valueOf(Math.round(dpi*px));
		return calc.intValue();
	}
	private void loadInts()
	{
	    int2=dpi2px(   0.761f );
		int4=dpi2px(   1.523f );
	    int10=dpi2px(  3.809f );
		int20=dpi2px(  7.619f );
	    int150=dpi2px(57.142f );
		int170=dpi2px(64.761f );
	}
	public FileBrowser(Context ctx,FCallback call)
	{
		this.ctx=ctx;
		this.call=call;
		dpi=ctx.getResources().getDisplayMetrics().density;
	}
	
	
	public void RUN(String Home,boolean mSelecFile)
	{
	    if(Home != null)
			home=Home;
		selectFileEnabled=mSelecFile;
		DATABASE.addIfEmpty(home);

		if(mSelecFile)
		{
			okBtn.setText("Select a file");
			okBtn.setEnabled(false);
		}
		else
		{
			okBtn.setText("Select this folder");
	        okBtn.setOnClickListener(new OnClickListener()
		    {
			   @Override
			   public void onClick(View v)
			   {
				    hnd.sendEmptyMessage(CODE_DONE);
			   }
			});
		}
		loadInts();
		hnd.sendEmptyMessage(CODE_EXPLORE_USE_BACKUP);
	}

	

	////////////////////// start display //////////////////////

	public void urlPath(String s)
    {
		char[] txt=s.toCharArray();
		TextPaint tp=viewPath.getPaint();
		float si;
		int skip=0;
		float len=0.0f+ viewPath.getMeasuredWidth();
		for(skip=0;skip<txt.length;skip++)
		{
			si= tp.measureText(txt,skip,txt.length-skip);
			if(si<len)
				break;
		}
		viewPath.setText(new String(txt,skip,txt.length-skip));
    }
	private Drawable myShape(int width,int color,float radius)
	{
		GradientDrawable shape=new GradientDrawable();
		shape.setColor(Color.TRANSPARENT);
		shape.setCornerRadius(radius);
		shape.setStroke(width,color);

		return shape;
	}
	private void displayResults()
	{
		int i,len;
		//create temp container(this will replace the only child that hScroll has)
		LinearLayout tempContainer = new LinearLayout(ctx);
		tempContainer.setLayoutDirection(LinearLayout.VERTICAL);
		tempContainer.setBackgroundColor(Color.WHITE);
		tempContainer.setOrientation(LinearLayout.VERTICAL);

		len = DATABASE.GetListLen();
		for(i=0;i<len;i++)
		{
			BAG p=DATABASE.GetList(i);
			LinearLayout.LayoutParams Pm=new LinearLayout.LayoutParams(-1,-1);
			Pm.setMargins(0,0,0,int2);
			View hy=makeFrame(p.getName(),
			                 p.isFile(),
							 p.isBlur(),
							  i);
			tempContainer.addView(hy,Pm);
		}
		hScroll.removeAllViews();
		hScroll.addView(tempContainer);
	}
	////////////////////// end display //////////////////////

	////////////////////// step1 build the list //////////////////////
	private Comparator<BAG> comp=new Comparator<BAG>()
	{
		@Override
		public int compare(BAG p1, BAG p2)
		{
			if((p1.isFile() == false) && (p2.isFile() == true))
				return -1;
			if((p1.isFile() == true) && (p2.isFile() == false))
				return 1;
			return (p1.getName().compareToIgnoreCase(p2.getName()));
		}
	};
	private FileFilter onlyDir=new FileFilter()
	{
		@Override
		public boolean accept(File p1)
		{
			return p1.isDirectory();
		}
	};

	private boolean ReadDirectory(File path)
	{
		FileFilter filter=null;
		if(selectFileEnabled==false)
		{
			filter=onlyDir;
		}
		File[] dir=path.listFiles(filter);
		if(dir==null)
			return false;
		DATABASE.ResetList();
		//add back element
		BAG p=new BAG("../");
		p.isFile(false);
		p.isBlur(true);
		DATABASE.add(p);
		for(int i=0;i<dir.length;i++)
		{
		    p=new BAG(dir[i].getName());
			p.isFile(dir[i].isFile());
			if(dir[i].isFile() && selectFileEnabled)
			{
				p.isBlur(call.isFileBlur(dir[i].getName()));
			}
			DATABASE.add(p);
		}
		if(dir.length>1)
		{
			Collections.sort(DATABASE.GetList(),comp);
		}
		return true;
	}

	private void EXPLORE(boolean useBackup)
	{
		String path=DATABASE.GetLastHistory();
		if((useBackup == true) && (DATABASE.GetListLen() > 0))
		{
			urlPath(path);
			displayResults();
			return;
		}
		if(ReadDirectory(new File(path)) == true)
		{
		   urlPath(path);
		   displayResults();
		 return;
		}
		viewPath.setText("Need Storage Permission");
	}


    ////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////

	private void OnClickHelper(FrameLayout parent,int id)
	{
	    BAG p=DATABASE.GetList(id);
		if(p.isFile()==false && p.isBlur() == true)
		{
			if(onBackPressed() == true)
				hnd.sendEmptyMessage(CODE_CANCEL);
			return;
		}
		String fullPath=DATABASE.GetLastHistory();
		fullPath+=call.getFileSeparator()+p.getName();
		DATABASE.add(fullPath);
		
		if(p.isFile()==true)
		{
		    hnd.sendEmptyMessage(CODE_DONE);
			return;
		}
		parent.setAlpha(0.5f);
		hnd.sendEmptyMessage(CODE_EXPLORE_NO_BACKUP);
	
	}

	private FrameLayout makeFrame(String fileName,boolean isFile,boolean isBlur,final int id)
	{
		FrameLayout container;
		FrameLayout imgv; //ImageView imgv;
		TextView ext;
		Button name;

		container= new FrameLayout(ctx);

		FrameLayout profile=new FrameLayout(ctx);
		FrameLayout.LayoutParams profParam=new FrameLayout.LayoutParams(0,0);
		profParam.width=int150;
		profParam.height=int150;
		profParam.leftMargin=int20;
		profParam.topMargin=int20;
		profParam.rightMargin=int20;
		profParam.bottomMargin=int20;
		profParam.gravity=Gravity.LEFT;
		profile.setBackgroundColor(Color.TRANSPARENT);
		container.addView(profile,profParam);


		imgv= new FrameLayout(ctx); //new ImageView(ctx);
		FrameLayout.LayoutParams img1Param=new FrameLayout.LayoutParams(0,0);
		img1Param.width=-1;
		img1Param.height=-1;
		img1Param.topMargin=int10;
		img1Param.rightMargin=int10;
		img1Param.leftMargin=int10;
		img1Param.bottomMargin=int10;
		img1Param.gravity=Gravity.CENTER;
		//imgv.setBackgroundColor(Color.TRANSPARENT);
		profile.addView(imgv,img1Param);

		ext=new TextView(ctx);
		FrameLayout.LayoutParams ext1Param=new FrameLayout.LayoutParams(0,0);
		ext1Param.width=-1;
		ext1Param.height=-1;
		ext1Param.topMargin=int10;
		ext1Param.rightMargin=int10;
		ext1Param.leftMargin=int10;
		ext1Param.bottomMargin=int10;
		ext.setRotation(-50);
		ext.setBackgroundColor(Color.TRANSPARENT);
		ext.setTextColor(Color.BLUE);
		ext.setAlpha(0.3f);
		ext1Param.gravity=Gravity.CENTER;
		ext.setGravity(Gravity.CENTER);
		ext.setTypeface(null,Typeface.BOLD_ITALIC);
		profile.addView(ext,ext1Param);

		name=new Button(ctx);

		FrameLayout.LayoutParams btn1Param=new FrameLayout.LayoutParams(0,0);
		btn1Param.width=-1;
		btn1Param.height=int150;
		btn1Param.leftMargin=int10;
		btn1Param.rightMargin=int20;
		name.setBackgroundColor(Color.TRANSPARENT);
		name.setPadding(int170,int20,0,0);
	    name.setGravity(Gravity.LEFT);
        name.setTypeface(null,Typeface.ITALIC);
		container.addView(name,btn1Param);
		container.setBackground(myShape(int4,Color.rgb(0x80,0x80,0x80),70));

		name.setText(fileName);
		name.setId(id);
		
		if(isFile == true)
		{
			imgv.setBackgroundColor(Color.GREEN);
		    int index=fileName.lastIndexOf('.');
		    if(index>0 && (fileName.length() - index)<6)
		    {
				ext.setText(fileName.toUpperCase().substring(index+1,fileName.length()));
		    }
		}else{
		    imgv.setBackgroundColor(Color.YELLOW);
		}
		if(isFile && isBlur==true)
		{
			container.setAlpha(0.5f);
			return container;
		}
		name.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v){
					OnClickHelper((FrameLayout)(v.getParent()),v.getId());
				}});
		return container;
	}


	public boolean onBackPressed()
	{
		if(DATABASE.RemoveLastHistory() == false)
		{
			DATABASE.ResetHistory();
			DATABASE.ResetList();
			return true;
		}
		hnd.sendEmptyMessage(CODE_EXPLORE_NO_BACKUP);
	  return false;
	}
	
	
	private Handler hnd=new Handler()
	{
		@Override
		public void handleMessage(Message ms)
		{
			switch(ms.what)
			{
				case CODE_EXPLORE_NO_BACKUP:
					{
						EXPLORE(false);
					}break;
				case CODE_EXPLORE_USE_BACKUP:
					{
						EXPLORE(true);
					}break;
				case CODE_DONE:
					{
						call.DONE(DATABASE.GetLastHistory(),false);
						DATABASE.ResetHistory();
						DATABASE.ResetList();
					}break;
				case CODE_CANCEL:
					{
						call.DONE("",true);
					}break;
				default:
					break;
			}
		}
	};
	
	public FrameLayout create()
	{
		FrameLayout frame=new FrameLayout(ctx);
		frame.setBackgroundColor(Color.WHITE);
		okBtn=new Button(ctx);
		FrameLayout.LayoutParams btn1Param=new FrameLayout.LayoutParams(0,0);
		btn1Param.width=-1;
		btn1Param.height=dpi2px(38f);
		btn1Param.topMargin=dpi2px(3.8f);
		btn1Param.rightMargin=dpi2px(3.8f);
		btn1Param.leftMargin=dpi2px(3.8f);
		btn1Param.gravity=Gravity.CENTER_HORIZONTAL|Gravity.TOP;
		okBtn.setText("Select This Folder");
		frame.addView(okBtn,btn1Param);

		viewPath=new TextView(ctx);
		FrameLayout.LayoutParams vpParam=new FrameLayout.LayoutParams(0,0);
		vpParam.width=-1;
		vpParam.height=dpi2px(30.476f);
		vpParam.topMargin=dpi2px(34.285f);
		vpParam.rightMargin=dpi2px(3.8f);
		vpParam.leftMargin=dpi2px(3.8f);
		vpParam.gravity=Gravity.TOP|Gravity.LEFT;
		viewPath.setBackgroundColor(Color.TRANSPARENT);
		viewPath.setSingleLine(true);
		viewPath.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
		viewPath.setText("storate:h3");
		frame.addView(viewPath,vpParam);

		hScroll=new ScrollView(ctx);
		FrameLayout.LayoutParams scrollParam=new FrameLayout.LayoutParams(-1,-1);
		scrollParam.topMargin=dpi2px(64.761f);
		hScroll.setBackgroundColor(Color.TRANSPARENT);
		frame.addView(hScroll,scrollParam);
		return frame;
	}
	
	
	
	
}
