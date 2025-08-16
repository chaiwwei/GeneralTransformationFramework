package test;
import java.util.Random;

import com.StringOperation;
import com.TextDocumentV2;

public class Tool {

	static Random rand = new Random(20);
	static String S = "abcdefghij1234567890";

	public static StringOperation generateOperation(int ratio, int opStrLen, TextDocumentV2 doc) {
		int chance = rand.nextInt(100);
		StringOperation sop = null;
		if (chance < ratio) {
			int len = doc.getLength();
			int insPos = rand.nextInt(len);
			String str = generate(opStrLen);
			doc.insert(insPos, str);
			sop = StringOperation.createInsOperation(insPos, str);
		} else {
			int len = doc.getLength();
			if (len > opStrLen) {
				int delPos = rand.nextInt(len - opStrLen);
				doc.delete(delPos, opStrLen);
				sop = StringOperation.createDelOperation(delPos, opStrLen);
			} else {
				int insPos = rand.nextInt(len);
				String str = generate(opStrLen);
				doc.insert(insPos, str);
				sop = StringOperation.createInsOperation(insPos, str);
			}
		}

		return sop;
	}

	public static String generate(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(S.charAt(rand.nextInt(20)));
		}
		return sb.toString();
	}

}
