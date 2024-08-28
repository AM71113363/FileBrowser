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
import java.time.*;
import android.util.*;
import android.text.*;
import android.graphics.drawable.*;
import android.graphics.drawable.shapes.*;
import android.transition.*;
//remove some of the imports above,I forgot to remove them,
//this interface had more functions,but the app was becoming more
//complex to understand and I decided to remove them,but forgot the imports
//Even the Lib was compiled with them.
//Usually I get warning for unused imports/objects/etc...
//I don't know how I missed this
public interface FCallback
{
	public boolean isFileBlur(String name);
	public char getFileSeparator();
	public void DONE(String path,boolean isCancel);
}
