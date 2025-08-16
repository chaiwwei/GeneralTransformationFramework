package com;

import java.util.ArrayList;

public class VectorClock extends ArrayList<Integer> {
	private static final long serialVersionUID = 1L;
	int size;

	public VectorClock(int num) {
		super();
		for (int i = 0; i < num; i++) {
			this.add(0);
		}
		this.size = num;
	}

	public VectorClock clone() {
		VectorClock new_clock = new VectorClock(size);
		for (int i = 0; i < size; i++) {
			new_clock.set(i, this.get(i));
		}
		return new_clock;
	}

	public int getSum() {
		int sum = 0;
		for (int i = 0; i < size; i++) {
			sum = sum + this.get(i);
		}
		return sum;
	}

	public static boolean equivalent(VectorClock vc1, VectorClock vc2) {
		boolean result = true;
		int size = vc1.size;

		for (int i = 0; i < size; i++) {

			if (vc1.get(i) != vc2.get(i)) {
				result = false;
				break;
			}

		}

		return result;

	}

	public static VectorClock union(VectorClock vc1, VectorClock vc2) {

		int size = vc1.size;

		VectorClock unionVC = new VectorClock(vc1.size);
		for (int i = 0; i < size; i++) {
			int seq1 = vc1.get(i);
			int seq2 = vc2.get(i);
			if (seq1 < seq2) {
				unionVC.add(seq2);
			} else {
				unionVC.add(seq1);
			}
		}

		return unionVC;
	}

	public static VectorClock intersect(VectorClock vc1, VectorClock vc2) {
		int size = vc1.size;

		VectorClock intersectVC = new VectorClock(vc1.size);
		for (int i = 0; i < size; i++) {
			int seq1 = vc1.get(i);
			int seq2 = vc2.get(i);
			if (seq1 < seq2) {
				intersectVC.add(seq1);
			} else {
				intersectVC.add(seq2);
			}
		}

		return intersectVC;
	}

	public static VectorClock fromString(String[] str) {
		VectorClock vc = new VectorClock(str.length);
		for (int i = 0; i < vc.size; i++) {
			vc.set(i, Integer.parseInt(str[i]));
		}
		return vc;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.size; i++) {
			sb.append(this.get(i));
			if (i < this.size - 1) {
				sb.append(",");
			}
		}

		return sb.toString();
	}
}
