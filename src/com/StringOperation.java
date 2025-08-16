package com;

import java.util.LinkedList;

public class StringOperation {

	public String str;
	public int pos; // position in the document
	public OpType type;
	public LinkedList<DelParas> subOps;
	public int len;

	private StringOperation(LinkedList<DelParas> subOps) {
		this.pos = 0;
		this.str = null;
		this.type = OpType.del;
		this.subOps = subOps;
		this.len = -1;
	}

	private StringOperation(int pos, int len) {
		this.pos = pos;
		this.str = null;
		this.type = OpType.del;
		this.subOps = null;
		this.len = len;
	}

	private StringOperation(int position, String content) {
		this.pos = position;
		this.str = content;
		this.type = OpType.ins;
		this.subOps = null;
		this.len = content.length();
	}

	public static StringOperation from(String str) {

		StringOperation newOp = null;

		String typeStr = str.substring(0, 3);
		int index1 = str.indexOf(",", 4);
		int index2 = str.indexOf(")", index1);
		String posStr = str.substring(4, index1);
		if (typeStr.equals("ins")) {
			newOp = new StringOperation(Integer.valueOf(posStr), str.substring(index1 + 1, index2));
		} else {
			newOp = new StringOperation(Integer.valueOf(posStr), Integer.valueOf(str.substring(index1 + 1, index2)));
		}

		return newOp;
	}

	public static StringOperation createInsOperation(int position, String content) {
		return new StringOperation(position, content);
	}

	public static StringOperation createDelOperation(LinkedList<DelParas> paras) {
		return new StringOperation(paras);
	}

	public static StringOperation createDelOperation(int position, int len) {
		return new StringOperation(position, len);
	}

	/*
	 * Construction of an insert operation
	 */
	public StringOperation clone() {
		if (this.type == OpType.del) {
			if (this.subOps != null) {
				LinkedList<DelParas> ops = new LinkedList<DelParas>();
				for (int i = 0; i < this.subOps.size(); i++) {
					ops.add(new DelParas(this.subOps.get(i).pos, this.subOps.get(i).len));
				}
				StringOperation sop = new StringOperation(ops);
				sop.len = this.len;
				sop.pos = this.pos;
				return sop;
			} else {
				return new StringOperation(pos, len);
			}

		} else {
			return new StringOperation(this.pos, this.str);
		}
	}

	public void add(DelParas paras) {
		if (this.subOps == null) {
			this.subOps = new LinkedList<>();
			this.subOps.add(paras);
		}
	}

	public OpType getType() {
		return this.type;
	}

	public int getPosition() {
		return pos;
	}

	public String getContent() {
		return this.str;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.getType() == OpType.ins) {
			sb.append("ins(");
			sb.append(this.pos);
			sb.append(",");
			sb.append(this.str);
			sb.append(")");
		} else {
			if (this.subOps != null) {
				sb.append("del(");
				for (int i = 0; i < this.subOps.size(); i++) {
					sb.append(" [");
					sb.append(this.subOps.get(i).pos);
					sb.append(",");
					sb.append(this.subOps.get(i).len);
					sb.append("] ");
				}
				sb.append(")");
			} else {
				sb.append("del(");
				sb.append(this.pos);
				sb.append(",");
				sb.append(this.len);
				sb.append(")");
			}

		}
		return sb.toString();
	}
}