package crdt;

import com.OpType;

public abstract class RGAOperation {
	public OpType type;
	public RGAId id;

	public RGAOperation(OpType type, RGAId id) {
		this.type = type;
		this.id = id;
	}

	public abstract String toString();

	public static RGAOperation fromString(String opStr, RGAId id) {
		//System.out.println("opStr:"+opStr);
		String[] paras = opStr.split("\\|");
		if (paras[0].equals("ins")) {
			String[] prevIdParas = paras[1].split(",");
			RGAId prevId = RGAId.fromString(prevIdParas[0], prevIdParas[1], prevIdParas[2]);
			int prevOffset = Integer.parseInt(paras[2]);
			//String[] newIdParas = paras[3].split(",");
			//RGAId newId = RGAId.fromString(newIdParas[0], newIdParas[1], newIdParas[2]);
			//VectorClock vc = VectorClock.fromString(paras[5].split(","));
			//RGAOperation op = new RGAInsOperation(prevId, prevOffset, newId, paras[4], vc);
			RGAOperation op = new RGAInsOperation(prevId, prevOffset, id, paras[3]);
			return op;
		} else {
			RGADelOperation op = new RGADelOperation(id);
			int length = paras.length;
			for (int i = 1; i < length; i++) {
				//System.out.println("dels:"+paras[i]);
				String[] delIdParas = paras[i].split(",");
				RGAId delId = RGAId.fromString(delIdParas[ 0], delIdParas[1], delIdParas[ 2]);
				int offset = Integer.parseInt(delIdParas[3]);
				int len = Integer.parseInt(delIdParas[ 4]);
				op.add(new RGADelPara(delId, offset, len));
			}
			return op;
		}
	}
}
