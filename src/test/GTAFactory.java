package test;

import java.util.ArrayList;

import com.GTAlgorithm;

import ast.AST;
import ast.ASTCharObjectSequence;
import ast.ASTStringObjectSequence;
import ast.BorderObjectSequence;
import crdt.RGA;
import crdt.RGAS;
import crdt.RGAStringObjectSequence;
import ot.AbsorbStringTransformer;
import ot.CharTransformer;
import ot.FalseTieStringTransformer;
import ot.POT;
import ot.POTFTF;
import ot.StringTransformer;
import ttf.TTF;

public class GTAFactory {
	public static ArrayList<GTAlgorithm> create(String content, int num, String algName) {
		ArrayList<GTAlgorithm> algs = new ArrayList<GTAlgorithm>();

		if (algName.equals("ASTS")) {
			for (int i = 0; i < num; i++) {
				algs.add(new AST(i, num, new BorderObjectSequence(content)));
			}
		} else if (algName.equals("RGASHash")) {
			for (int i = 0; i < num; i++) {
				algs.add(new RGAS(i, num, new RGAStringObjectSequence(content)));
			}
		} else if (algName.equals("OTChar")) {
			for (int i = 0; i < num; i++) {
				algs.add(new POT(i, num, new CharTransformer(), false));
			}
		} else if (algName.equals("POTFTF")) {
			for (int i = 0; i < num; i++) {
				FalseTieStringTransformer transformer = new FalseTieStringTransformer();
				POTFTF pot = new POTFTF(i, num, transformer,false);
				algs.add(pot);
			}
		} else if (algName.equals("TTF")) {
			for (int i = 0; i < num; i++) {
				algs.add(new TTF(i, num, content));
			}
		} else if (algName.equals("ASTO")) {
			for (int i = 0; i < num; i++) {
				algs.add(new AST(i, num, new ASTStringObjectSequence(content)));
			}
		} else if (algName.equals("RGA")) {
			for (int i = 0; i < num; i++) {
				algs.add(new RGA(i, num, content));
			}
		}

		return algs;
	}
}