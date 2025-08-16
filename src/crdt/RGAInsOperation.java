package crdt;

import com.OpType;

public class RGAInsOperation extends RGAOperation {
    RGAId prevId;
    int prevIdOffset;
    String content;

    public RGAInsOperation(RGAId prevId, int prevIdOffset, RGAId id, String content) {
        super(OpType.ins, id);
        this.prevId = prevId;
        this.prevIdOffset = prevIdOffset;
        this.content = content;
    }
    
    
    public String toString()
    {
    	StringBuilder sb=new StringBuilder();
    	sb.append("ins");
    	sb.append("|");
    	sb.append(this.prevId.toString());
    	sb.append("|");
    	sb.append(this.prevIdOffset);
    	sb.append("|");
    	sb.append(this.id.toString());
    	sb.append("|");
    	sb.append(this.content);
    	
		return sb.toString();
    }
    

}