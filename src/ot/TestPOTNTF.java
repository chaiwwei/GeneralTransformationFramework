package ot;

import java.util.ArrayList;
import java.util.List;

import com.GTAlgorithm;
import com.TextDocument;

import test.Parameters;
import test.Tester;

public class TestPOTNTF {
	
	public static void main(String[] args) {

		String iniDocState = "abc";
		
		Parameters paras = new Parameters();
		paras.numOfSite = 3;
		paras.opPerSite = 2;
		paras.opStrLen = 2;
		paras.ratio = 70;
		paras.round = 200;
		ArrayList<GTAlgorithm> algs = new ArrayList<GTAlgorithm>();
		List<TextDocument> docs = new ArrayList<TextDocument>();

		for (int i = 0; i < paras.numOfSite; i++) {
			StringTransformer transformer = new StringTransformer();
			POT pot = new POT(i, paras.numOfSite, transformer, true);
			algs.add(pot);
			docs.add(new TextDocument(iniDocState));

		}

		//Tester.test4("POTNTF", paras, algs, docs);
		Tester.test3Site( paras, algs, docs);

	}
}
