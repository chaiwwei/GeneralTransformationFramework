package search;

public class AVLNode {
    public int pos;
    public int len;
    public int height;
    public AVLNode left;
    public AVLNode right;
    
    
    public AVLNode(int pos, int len)
    {
    	this.pos=pos;
    	this.len=len;
    	this.height=1;
    	this.left=null;
    	this.right=null;
    			
    }
}
