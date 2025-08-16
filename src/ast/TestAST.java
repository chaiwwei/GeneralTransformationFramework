package ast;

import java.util.ArrayList;
import java.util.List;

import com.GTAlgorithm;
import com.TextDocument;

import test.GTAFactory;
import test.Tester;

public class TestAST {

	public static void main(String[] args) {
		String iniDocState = "1234567890";
		ArrayList<GTAlgorithm> algs = GTAFactory.create(iniDocState, 3, "AST");
		List<TextDocument> docs = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			docs.add(new TextDocument("1234567890"));
		}
		Tester.test3(algs, docs);

	}

}
