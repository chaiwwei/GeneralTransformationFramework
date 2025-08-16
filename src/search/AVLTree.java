package search;

public class AVLTree {
	private AVLNode root;

	void updateHeight(AVLNode n) {
		n.height = 1 + Math.max(height(n.left), height(n.right));
	}

	int height(AVLNode n) {
		return n == null ? -1 : n.height;
	}

	int getBalance(AVLNode n) {
		return (n == null) ? 0 : height(n.right) - height(n.left);
	}

	AVLNode rotateRight(AVLNode y) {
		AVLNode x = y.left;
		AVLNode z = x.right;
		x.right = y;
		y.left = z;
		updateHeight(y);
		updateHeight(x);
		return x;
	}

	AVLNode rotateLeft(AVLNode y) {
		AVLNode x = y.right;
		AVLNode z = x.left;
		x.left = y;
		y.right = z;
		updateHeight(y);
		updateHeight(x);
		return x;
	}

	AVLNode rebalance(AVLNode z) {
		updateHeight(z);
		int balance = getBalance(z);
		if (balance > 1) {
			if (height(z.right.right) > height(z.right.left)) {
				z = rotateLeft(z);
			} else {
				z.right = rotateRight(z.right);
				z = rotateLeft(z);
			}
		} else if (balance < -1) {
			if (height(z.left.left) > height(z.left.right))
				z = rotateRight(z);
			else {
				z.left = rotateLeft(z.left);
				z = rotateRight(z);
			}
		}
		return z;
	}

	public AVLNode insert(AVLNode node, int pos, int len) {
		if (node == null) {
			return new AVLNode(pos, len);
		} else if (node.pos > pos) {
			node.pos = node.pos + len;
			node.left = insert(node.left, pos, len);
		} else if (node.pos + node.len < pos) {
			node.right = insert(node.right, pos, len);
		} else {

		}
		return rebalance(node);
	}

	/*
	 * public AVLNode delete(AVLNode node, int key) { if (node == null) { return
	 * node; } else if (node.key > key) { node.left = delete(node.left, key); } else
	 * if (node.key < key) { node.right = delete(node.right, key); } else { if
	 * (node.left == null || node.right == null) { node = (node.left == null) ?
	 * node.right : node.left; } else { AVLNode mostLeftChild =
	 * mostLeftChild(node.right); node.key = mostLeftChild.key; node.right =
	 * delete(node.right, node.key); } } if (node != null) { node = rebalance(node);
	 * } return node; }
	 */
	public AVLNode find(int pos) {
		AVLNode current = root;
		while (current != null) {
			if (current.pos == pos) {
				break;
			} else if (current.pos + current.len < pos) {
				current = current.right;
			} else {
				int len = pos - current.pos;
				AVLNode newNode = new AVLNode(pos, len);

				break;
			}
		}
		return current;
	}

	public void split(AVLNode node, int len) {
		AVLNode newNode = new AVLNode(node.pos + node.len, len);
		//node.right= this.insert(node,newNode);
	}

}
