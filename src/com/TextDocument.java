package com;

import java.util.ArrayList;
import java.util.LinkedList;

public class TextDocument {
	TextNode head = new TextNode("#");
	TextNode tail = new TextNode("%");
	int length = 0;

	public TextDocument(String ini) {

		if (ini != null && ini.length() > 0) {
			TextNode newText = new TextNode(ini);
			this.head.next = newText;
			newText.next = this.tail;
			this.length = ini.length();
		} else {
			this.head.next = this.tail;
		}
	}

	public void insert(int pos, String str) {
		int num = 0;
		TextNode cur = this.head.next;
		TextNode prev = this.head;
		while (cur != this.tail) {
			if (num == pos) {
				break;
			} else if (num + cur.str.length() > pos) {
				cur.split(pos - num);
			}
			num = num + cur.str.length();
			prev = cur;
			cur = cur.next;
		}
		TextNode newText = new TextNode(str);
		newText.next = prev.next;
		prev.next = newText;
		this.length = this.length + str.length();
	}

	public void delete(int pos, int len) {
		int num = 0;
		TextNode cur = this.head.next;
		TextNode prev = this.head;
		while (true) {
			if (num == pos) {
				break;
			} else if (num + cur.str.length() > pos) {
				cur.split(pos - num);
			}
			num = num + cur.str.length();
			prev = cur;
			cur = cur.next;
		}

		num = 0;
		while (true) {
			if (num + cur.str.length() == len) {
				prev.next = cur.next;
				break;
			} else if (num + cur.str.length() > len) {
				cur.split(len - num);
				prev.next = cur.next;
				break;
			} else {
				num = num + cur.str.length();
				prev.next = cur.next;
				cur = cur.next;
			}
		}
		this.length = this.length - len;
	}

	public void execute(StringOperation op) {
		try {
			if (op.type == OpType.ins) {
				if (op.pos != -1) {
					this.insert(op.pos, op.str);
				}
			} else {
				if (op.len == 0) {
					return;
				} else if (op.subOps == null) {
					this.delete(op.pos, op.len);
				} else {
					int size = op.subOps.size();
					for (int i = 0; i < size; i++) {
						DelParas subOp = op.subOps.get(i);
						this.delete(subOp.pos, subOp.len);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println("execute op: " + op.toString());
			ex.printStackTrace();
		}
	}

	public int getLength() {
		// TODO Auto-generated method stub
		return this.length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		TextNode cur = this.head.next;
		while (cur != this.tail) {
			sb.append(cur.str);
			cur = cur.next;
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		TextDocument doc = new TextDocument("abc");
		StringOperation op = StringOperation.createDelOperation(0, 2);
		doc.execute(op);
		System.out.println(doc.toString());
		op = StringOperation.createInsOperation(0, "ab");
		doc.execute(op);
		System.out.println(doc.toString());

		op = StringOperation.createInsOperation(2,"bc");
		doc.execute(op);
		System.out.println(doc.toString());
		
		
		op = StringOperation.createInsOperation(4,"12");
		doc.execute(op);
		System.out.println(doc.toString());
		
		op = StringOperation.createInsOperation(4,"78");
		doc.execute(op);
		System.out.println(doc.toString());
		
		op = StringOperation.createInsOperation(9,"fg");
		doc.execute(op);
		System.out.println(doc.toString());
		
		op = StringOperation.createInsOperation(8,"23");
		doc.execute(op);
		System.out.println(doc.toString());
	}

}