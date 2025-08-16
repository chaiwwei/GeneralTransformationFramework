package crdt;

import java.util.LinkedList;

import com.OpType;

public class RGADelOperation extends RGAOperation {
    public LinkedList<RGADelPara> idList;

    public RGADelOperation( RGAId id) {
        super(OpType.del, id);
        // TODO Auto-generated constructor stub
        idList=new LinkedList<RGADelPara>();
    }
    
    public void add(RGADelPara para)
    {
    	this.idList.add(para);
    }

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("del|");
		sb.append(this.id.toString());
		
		for(RGADelPara rgaDel : this.idList) {
			sb.append("|");
			sb.append(rgaDel.toString());
		}
		
		return sb.toString();
	}
}