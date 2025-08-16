package com;

public class TextDocumentV2 {
	private StringBuilder sb;
	int length;

	public TextDocumentV2(String content) {
		this.sb = new StringBuilder(content);
		this.length = content.length();
	}

	public void insert(int pos, String str) {
		this.sb.insert(pos, str);
		this.length = this.length + str.length();
	}

	public void delete(int pos, int len) {
		// TODO Auto-generated method stub
		this.sb.delete(pos, pos + len);
		this.length = this.length - len;
	}

	public void execute(StringOperation op) {
		try {
			if (op.type == OpType.ins) {
				if (op.pos >= 0) {
					this.insert(op.pos, op.str);
				}
			} else {
				if (op.subOps == null) {
					if (op.len > 0) {
						this.delete(op.pos, op.len);
					}
				} else {
					int size = op.subOps.size();
					for (int i = 0; i < size; i++) {
						DelParas subOp = op.subOps.get(i);
						this.delete(subOp.pos, subOp.len);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("doc length:" + this.length);
			ex.printStackTrace();
		}
	}

	public int getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}
}
