package test;

import java.util.ArrayList;
import java.util.List;

import com.GTAlgorithm;
import com.TextDocument;

import ast.AST;
import ast.ASTStringObjectSequence;
import ot.FalseTieStringTransformer;
import ot.POT;
import ot.POTFTF;
import ot.StringTransformer;

public class TestComparisonASTAndPOTFTF {
	public static void main(String[] args) {

		int siteNum = Integer.parseInt(args[0]);
		int opStrLen = Integer.parseInt(args[1]);
		int opPerSite = Integer.parseInt(args[2]);
		int ratio = Integer.parseInt(args[3]);
		int round = Integer.parseInt(args[4]);

		String iniDocState = "abc";

		Parameters paras = new Parameters(siteNum, opStrLen, ratio, round, opPerSite);

		/*
		 * Parameters paras=new Parameters(); paras.numOfSite = 3; paras.opPerSite = 2;
		 * paras.opStrLen = 2; paras.ratio = 80; paras.round = 1;
		 */

		ArrayList<GTAlgorithm> algs = new ArrayList<GTAlgorithm>();
		List<TextDocument> docs = new ArrayList<TextDocument>();

		for (int i = 0; i < 2; i++) {
			AST ast = new AST(i, paras.numOfSite, new ASTStringObjectSequence(iniDocState));
			algs.add(ast);
			docs.add(new TextDocument(iniDocState));
		}

		for (int i = 2; i < paras.numOfSite; i++) {
			FalseTieStringTransformer transformer = new FalseTieStringTransformer();
			POTFTF pot = new POTFTF(i, paras.numOfSite, transformer, true);
			algs.add(pot);
			docs.add(new TextDocument(iniDocState));
		}

		// Tester.testNSiteAndTrace( paras, algs, docs);
		// Tester.test3(algs, docs);
		Tester.test4("ASTAndPOTFTF", paras, algs, docs);

	}
}
