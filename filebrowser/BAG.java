package sss.am71113363.filebrowser;
//Copyright AM71113363
public class BAG
{
    private String name;  //fileName (not a path)
    private boolean isFile; //false for Dir,true for File
    private boolean isBlur;
    //@Override
    public BAG(String name)
    {
        this.name=name;
	isFile=false;
	isBlur = false;
    }
    public String getName()
    {
        return name;
    }
    public boolean isFile() { return isFile; }
    public boolean isBlur() { return isBlur; }

    public void isFile(boolean b)  { isFile=b;  }
    public void isBlur(boolean b)  { isBlur=b;  }
}
