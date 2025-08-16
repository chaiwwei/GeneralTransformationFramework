package test;

import java.util.ArrayList;
import java.util.List;

import com.GTAlgorithm;
import com.TextDocument;
import ot.FalseTieStringTransformer;
import ot.POT;
import ot.POTFTF;
import ot.StringTransformer;

public class TestComparisonPOTNTFAndPOTFTF {
	public static void main(String[] args) {
		String iniDocState = "abc";
		int siteOpNum = Integer.parseInt(args[0]);
		int opStrLen = Integer.parseInt(args[1]);
		int opPerSite = Integer.parseInt(args[2]);
		int ratio = Integer.parseInt(args[3]);
		int round = Integer.parseInt(args[4]);
		Parameters paras = new Parameters(siteOpNum, opStrLen, ratio, round, opPerSite);
		/*
		 * paras.numOfSite = 6; paras.opPerSite = 2; paras.opStrLen = 2; paras.ratio =
		 * 80; paras.round = 10;
		 */

		ArrayList<GTAlgorithm> algs1 = new ArrayList<GTAlgorithm>();
		List<TextDocument> docs1 = new ArrayList<TextDocument>();

		for (int i = 0; i < paras.numOfSite; i++) {
			FalseTieStringTransformer transformer = new FalseTieStringTransformer();
			POTFTF pot = new POTFTF(i, paras.numOfSite, transformer, true);
			algs1.add(pot);
			docs1.add(new TextDocument(iniDocState));
		}

		ArrayList<GTAlgorithm> algs2 = new ArrayList<GTAlgorithm>();
		List<TextDocument> docs2 = new ArrayList<TextDocument>();
		for (int i = 0; i < paras.numOfSite; i++) {
			StringTransformer transformer = new StringTransformer();
			POT pot = new POT(i, paras.numOfSite, transformer, true);
			algs2.add(pot);
			docs2.add(new TextDocument(iniDocState));
		}
		
		Tester.test4("POTNTF", paras, algs2, docs2);
		Tester.test4("POTFTF", paras, algs1, docs1);

		//Tester.test3(algs, docs);
	}
}
