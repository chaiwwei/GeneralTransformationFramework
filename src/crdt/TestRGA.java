package crdt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.GTAlgorithm;
import com.StringOperation;
import com.TextDocument;

public class TestRGA {
	static int siteNum = 3;
	static Random rand = new Random(10);
	static String S = "abcdefghij1234567890";

	public static void main(String[] args) throws Exception {

		String initialDoc = generate(10);

		ArrayList<GTAlgorithm> algs = new ArrayList<GTAlgorithm>();

		for (int i = 0; i < 3; i++) {
			algs.add(new RGAS(i, 3, new RGAStringObjectSequence(initialDoc)));
		}

		List<TextDocument> docs = new ArrayList<TextDocument>();
		for (int i = 0; i < 3; i++) {
			docs.add(new TextDocument(initialDoc));
		}
		test(algs, docs);
		System.out.println("running");
	}

	public static void test(List<GTAlgorithm> algList, List<TextDocument> replicas) {

		long startTime = 0;
		long totalTime = 0;
		List<Long> localTimes = new ArrayList<Long>();
		List<Long> remoteTimes = new ArrayList<Long>();

		for (int k = 0; k < 30000; k++) {
			// 站点0产生本地操作
			GTAlgorithm gt0 = algList.get(0);
			TextDocument doc0 = replicas.get(0);
			ArrayList<String> site0RemoteOps = new ArrayList<String>();

			totalTime = 0;
			for (int j = 0; j < 2; j++) {

				StringOperation sop = generateOperation(60, 3, doc0);
				startTime = System.nanoTime();
				String aop = gt0.LOH(sop);
				totalTime = totalTime + (System.nanoTime() - startTime) / 1000;
				site0RemoteOps.add(aop);
			}

			// 站点1产生本地操作-----------------------------//
			GTAlgorithm gt1 = algList.get(1);
			TextDocument doc1 = replicas.get(1);
			ArrayList<String> site1RemoteOps = new ArrayList<String>();
			for (int j = 0; j < 2; j++) {
				StringOperation sop = generateOperation(60, 3, doc1);
				site1RemoteOps.add(gt1.LOH(sop));
			}

			// 站点2产生本地操作-------------------------------//
			GTAlgorithm gt2 = algList.get(2);
			TextDocument doc2 = replicas.get(2);
			ArrayList<String> site2RemoteOps = new ArrayList<String>();
			for (int j = 0; j < 2; j++) {
				StringOperation sop = generateOperation(60, 3, doc2);
				site2RemoteOps.add(gt2.LOH(sop));
			}

			// 站点0处理远程操作（来自站点1和2）--------------------//
			// copy
			totalTime = 0;

			for (int i = 0; i < 2; i++) {
				String aop1 = site1RemoteOps.get(i);
				startTime = System.nanoTime();
				StringOperation sop = gt0.ROH(aop1);
				totalTime = totalTime + (System.nanoTime() - startTime) / 1000;
				doc0.execute(sop);
			}

			for (int i = 0; i < 2; i++) {
				String aop2 = site2RemoteOps.get(i);

				startTime = System.nanoTime();
				StringOperation sop = gt0.ROH(aop2);
				totalTime = totalTime + (System.nanoTime() - startTime) / 1000;
				doc0.execute(sop);
			}

			for (int i = 0; i < 2; i++) {
				String aop = site0RemoteOps.get(i);

				StringOperation sop = gt1.ROH(aop);
				doc1.execute(sop);
			}

			for (int i = 0; i < 2; i++) {
				String aop = site2RemoteOps.get(i);

				StringOperation sop = gt1.ROH(aop);
				doc1.execute(sop);
			}

			// 站点2处理远程操作（来自站点0和1）

			for (int i = 0; i < 2; i++) {
				String aop = site0RemoteOps.get(i);
				StringOperation sop = gt2.ROH(aop);
				doc2.execute(sop);
			}

			for (int i = 0; i < 2; i++) {
				String aop = site1RemoteOps.get(i);
				StringOperation sop = gt2.ROH(aop);
				doc2.execute(sop);
			}
			// logTime(paras.toString() + "_remoteTime.txt", time);
		}

		return;
	}

	public static StringOperation generateOperation(int ratio, int opStrLen, TextDocument doc) {
		int chance = rand.nextInt(100);
		StringOperation sop = null;
		if (chance < ratio) {
			int len = doc.getLength();
			int insPos = rand.nextInt(len + 1);
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
				int insPos = rand.nextInt(len + 1);
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
