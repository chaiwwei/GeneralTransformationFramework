package ttf;

public class TTFObject {
	public TTFObject next;
	public boolean visible;
	
	public TTFObject(TTFObject next)
	{
		this.next=next;
		this.visible=true;
	}
}
