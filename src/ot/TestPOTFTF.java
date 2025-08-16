package ot;

import java.util.ArrayList;
import java.util.List;

import com.GTAlgorithm;
import com.TextDocument;
import test.Parameters;
import test.Tester;

public class TestPOTFTF {
	public static void main(String[] args) {
		String iniDocState = "abc";

		Parameters paras = new Parameters();
		paras.numOfSite = 4;
		paras.opPerSite = 2;
		paras.opStrLen = 2;
		paras.ratio = 70;
		paras.round = 10;

		ArrayList<GTAlgorithm> algs = new ArrayList<GTAlgorithm>();
		List<TextDocument> docs = new ArrayList<TextDocument>();

		{
			FalseTieStringTransformer transformer = new FalseTieStringTransformer();
			POTFTF pot = new POTFTF(0, paras.numOfSite, transformer, true);
			algs.add(pot);
			docs.add(new TextDocument(iniDocState));

		}
		
		for (int i = 1; i < paras.numOfSite; i++) {
			FalseTieStringTransformer transformer = new FalseTieStringTransformer();
			POTFTF pot = new POTFTF(i, paras.numOfSite, transformer, true);
			algs.add(pot);
			docs.add(new TextDocument(iniDocState));

		}

		//Tester.test4("POTFTF", paras, algs, docs);
		Tester.testNSiteAndTrace( paras, algs, docs);
	}
}
