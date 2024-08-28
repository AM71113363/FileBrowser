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

public interface FCallback
{
	public boolean isFileBlur(String name);
	public char getFileSeparator();
	public void DONE(String path,boolean isCancel);
}
