package com;

public class TextNode {
	public String str;
	public TextNode next;

	public TextNode(String str) {
		this.str = str;
		this.next = null;
	}

	public void split(int num) {
		String text = this.str.substring(num);
		this.str = this.str.substring(0, num);
		TextNode newText = new TextNode(text);
		newText.next = this.next;
		this.next = newText;
	}
}
