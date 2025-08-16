package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.GTAlgorithm;
import com.StringOperation;
import com.TextDocument;
import com.Timer;
import com.TimestampedOperation;

public class Tester {

	static Random rand = new Random();
	static String S = "abcdefghij1234567890";
	static int globalOrder = 0;
	static boolean turnOnConsole = false;

	public static void dispatch(TimestampedOperation top) {
		globalOrder++;
		top.setOrder(globalOrder);
	}

	public static void log(StringBuilder sb, String s) {

		if (!turnOnConsole) {
			sb.append(s);
			sb.append("\n");
		} else {
			System.out.println(s);
		}

	}

	public static void test4(String algName, Parameters paras, List<GTAlgorithm> algList, List<TextDocument> replicas) {

		int ratio = paras.ratio;
		int opStrLen = paras.opStrLen;
		int opPerSite = paras.opPerSite;
		int round = paras.round;
		int numOfSite = algList.size();

		List<Long> rtimeList = new ArrayList<Long>();
		List<Long> ltimeList = new ArrayList<Long>();

		List<Integer> memoryList = new ArrayList<Integer>();

		Timer timer = new Timer();

		for (int k = 0; k < round; k++) {
			List<ArrayList<TimestampedOperation>> opList = new ArrayList<ArrayList<TimestampedOperation>>();
			// Generation Phase
			for (int r = 0; r < numOfSite; r++) {
				GTAlgorithm gtr = algList.get(r);
				TextDocument docr = replicas.get(r);
				ArrayList<TimestampedOperation> siterRemoteOps = new ArrayList<>();
				for (int j = 0; j < opPerSite; j++) {
					StringOperation sop = generateOperation(ratio, opStrLen, docr);
					timer.start();
					TimestampedOperation top = gtr.LOH(sop);
					ltimeList.add(timer.elapsed());
					siterRemoteOps.add(top);
					dispatch(top);
				}
				opList.add(siterRemoteOps);

			}

			// Execution Phase;
			for (int r = 0; r < numOfSite; r++) {
				GTAlgorithm gtr = algList.get(r);
				TextDocument docr = replicas.get(r);
				for (int j = 0; j < numOfSite; j++) {
					if (r != j) {
						ArrayList<TimestampedOperation> remoteOps = opList.get(j);
						for (int i = 0; i < opPerSite; i++) {
							TimestampedOperation top = remoteOps.get(i);
							timer.start();
							StringOperation sop = gtr.ROH(top);
							rtimeList.add(timer.elapsed());
							docr.execute(sop);
						}
					}
				}
				memoryList.add(gtr.memorySize());
			}
			/*
			 * String s1 = replicas.get(0).toString(); for (int r = 1; r < numOfSite; r++) {
			 * 
			 * if (s1.equals(replicas.get(r).toString()) == false) {
			 * System.out.println("not consistent"); break; } }
			 */
		}
		write(ltimeList, algName + " " + paras.name(), "localTime" + System.currentTimeMillis() + ".txt");
		write(rtimeList, algName + " " + paras.name(), "remoteTime" + System.currentTimeMillis() + ".txt");
		write(memoryList, algName + " " + paras.name(), "memorySize" + System.currentTimeMillis() + ".txt");

	}

	public static void test3(List<GTAlgorithm> algList, List<TextDocument> replicas) {

		StringBuilder sb = new StringBuilder();

		GTAlgorithm gt0 = algList.get(0);
		TextDocument doc0 = replicas.get(0);
		ArrayList<TimestampedOperation> site0RemoteOps = new ArrayList<>();
		log(sb, "site 0 generate: ");
		{
			StringOperation sop01 = StringOperation.createInsOperation(1, "j1");
			log(sb, sop01.toString());
			doc0.execute(sop01);
			TimestampedOperation top01 = gt0.LOH(sop01);
			site0RemoteOps.add(top01);
			dispatch(top01);

			StringOperation sop02 = StringOperation.createInsOperation(4, "78");
			log(sb, sop02.toString());
			doc0.execute(sop02);
			TimestampedOperation top02 = gt0.LOH(sop02);
			site0RemoteOps.add(top02);
			dispatch(top02);
		}

		log(sb, "site 1 generate: ");
		GTAlgorithm gt1 = algList.get(1);
		TextDocument doc1 = replicas.get(1);
		ArrayList<TimestampedOperation> site1RemoteOps = new ArrayList<>();
		{
			StringOperation sop11 = StringOperation.createInsOperation(0, "hi");
			log(sb, sop11.toString());
			doc1.execute(sop11);
			TimestampedOperation top11 = gt1.LOH(sop11);
			site1RemoteOps.add(top11);
			dispatch(top11);

			StringOperation sop12 = StringOperation.createInsOperation(2, "cd");
			log(sb, sop12.toString());
			doc1.execute(sop12);
			TimestampedOperation top12 = gt1.LOH(sop12);
			site1RemoteOps.add(top12);
			dispatch(top12);
		}

		log(sb, "site 2 generate: ");
		GTAlgorithm gt2 = algList.get(2);
		TextDocument doc2 = replicas.get(2);
		ArrayList<TimestampedOperation> site2RemoteOps = new ArrayList<>();
		{
			StringOperation sop21 = StringOperation.createInsOperation(0, "ef");
			log(sb, sop21.toString());
			doc2.execute(sop21);
			TimestampedOperation top21 = gt2.LOH(sop21);
			site2RemoteOps.add(top21);
			dispatch(top21);

			StringOperation sop22 = StringOperation.createInsOperation(0, "de");
			log(sb, sop22.toString());
			doc2.execute(sop22);
			TimestampedOperation top22 = gt2.LOH(sop22);
			site2RemoteOps.add(top22);
			dispatch(top22);
		}

		// copy
		log(sb, "site 0 execute: ");

		for (int i = 0; i < 2; i++) {
			TimestampedOperation top1 = site1RemoteOps.get(i);
			StringOperation sop = gt0.ROH(top1);
			log(sb, sop.toString());
			doc0.execute(sop);
			System.out.println("result: " + doc0.toString());
		}

		for (int i = 0; i < 2; i++) {
			TimestampedOperation top2 = site2RemoteOps.get(i);
			StringOperation sop = gt0.ROH(top2);
			log(sb, sop.toString());
			doc0.execute(sop);
			System.out.println("result: " + doc0.toString());
		}

		log(sb, "site 1 execute: ");
		for (int i = 0; i < 2; i++) {
			TimestampedOperation top = site0RemoteOps.get(i);
			StringOperation sop = gt1.ROH(top);
			log(sb, sop.toString());
			doc1.execute(sop);
		}

		for (int i = 0; i < 2; i++) {
			TimestampedOperation top = site2RemoteOps.get(i);
			StringOperation sop = gt1.ROH(top);
			log(sb, sop.toString());
			doc1.execute(sop);
		}

		log(sb, "site 2 execute: ");
		for (int i = 0; i < 2; i++) {
			TimestampedOperation top = site0RemoteOps.get(i);
			StringOperation sop = gt2.ROH(top);
			log(sb, sop.toString());
			doc2.execute(sop);
		}

		for (int i = 0; i < 2; i++) {
			TimestampedOperation top = site1RemoteOps.get(i);
			StringOperation sop = gt2.ROH(top);
			log(sb, sop.toString());
			doc2.execute(sop);
		}

		String s1 = doc0.toString();
		String s2 = doc1.toString();
		String s3 = doc2.toString();

		if (s1.equals(s2) == false || s1.equals(s3) == false) {
			System.out.println("site 1: " + s1);
			System.out.println("site 2: " + s2);
			System.out.println("site 3: " + s3);
			System.out.println("not consistent");
			System.out.println(sb.toString());
		}

	}

	public static void testNSiteAndTrace(Parameters paras, List<GTAlgorithm> algList, List<TextDocument> replicas) {

		int ratio = paras.ratio;
		int opStrLen = paras.opStrLen;
		int opPerSite = paras.opPerSite;
		int round = paras.round;
		int numOfSite = algList.size();
		StringBuilder sb = new StringBuilder();

		for (int k = 0; k < round; k++) {
			log(sb, "run track :" + k);

			List<ArrayList<TimestampedOperation>> opList = new ArrayList<ArrayList<TimestampedOperation>>();
			// Generation Phase
			for (int r = 0; r < numOfSite; r++) {
				log(sb, "site " + r + " generate: ");

				GTAlgorithm gtr = algList.get(r);
				TextDocument docr = replicas.get(r);
				ArrayList<TimestampedOperation> siterRemoteOps = new ArrayList<>();
				for (int j = 0; j < opPerSite; j++) {
					StringOperation sop = generateOperation(ratio, opStrLen, docr);
					log(sb, sop.toString());
					TimestampedOperation top = gtr.LOH(sop);
					siterRemoteOps.add(top);
					dispatch(top);
				}
				opList.add(siterRemoteOps);
			}

			// Execution Phase;
			for (int r = 0; r < numOfSite; r++) {
				log(sb, "site " + r + " execute: ");

				GTAlgorithm gtr = algList.get(r);
				TextDocument docr = replicas.get(r);
				for (int j = 0; j < numOfSite; j++) {
					if (r != j) {
						ArrayList<TimestampedOperation> remoteOps = opList.get(j);
						for (int i = 0; i < opPerSite; i++) {
							TimestampedOperation top = remoteOps.get(i);
							StringOperation sop = gtr.ROH(top);
							log(sb, sop.toString());
							docr.execute(sop);
						}
					}
				}
			}

			String s1 = replicas.get(0).toString();

			for (int r = 1; r < numOfSite; r++) {
				String sr=replicas.get(r).toString();
				if (s1.equals(replicas.get(r).toString()) == false) {
					System.out.println("not consistent");
					System.out.println("site "+r +" :" + sr);
					
					
					break;
				}
			}

		}

	}

	public static void test3Site(Parameters paras, List<GTAlgorithm> algList, List<TextDocument> replicas) {

		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < paras.round; k++) {

			log(sb, "run track :" + k);

			GTAlgorithm gt0 = algList.get(0);
			TextDocument doc0 = replicas.get(0);
			ArrayList<TimestampedOperation> site0RemoteOps = new ArrayList<>();
			log(sb, "site 0 generate: ");
			for (int j = 0; j < paras.opPerSite; j++) {
				StringOperation sop = generateOperation(paras.ratio, paras.opStrLen, doc0);
				log(sb, sop.toString());
				TimestampedOperation top = gt0.LOH(sop);
				site0RemoteOps.add(top);
				dispatch(top);
			}

			log(sb, "site 1 generate: ");
			GTAlgorithm gt1 = algList.get(1);
			TextDocument doc1 = replicas.get(1);
			ArrayList<TimestampedOperation> site1RemoteOps = new ArrayList<>();
			for (int j = 0; j < paras.opPerSite; j++) {
				StringOperation sop = generateOperation(paras.ratio, paras.opStrLen, doc1);
				log(sb, sop.toString());
				TimestampedOperation top = gt1.LOH(sop);
				site1RemoteOps.add(top);
				dispatch(top);
			}

			log(sb, "site 2 generate: ");
			GTAlgorithm gt2 = algList.get(2);
			TextDocument doc2 = replicas.get(2);
			ArrayList<TimestampedOperation> site2RemoteOps = new ArrayList<>();
			for (int j = 0; j < paras.opPerSite; j++) {
				StringOperation sop = generateOperation(paras.ratio, paras.opStrLen, doc2);
				log(sb, sop.toString());
				TimestampedOperation top = gt2.LOH(sop);
				site2RemoteOps.add(top);
				dispatch(top);
			}

			log(sb, "site 0 execute: ");

			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt0.ROH(site1RemoteOps.get(i));
				log(sb, sop.toString());
				doc0.execute(sop);
			}

			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt0.ROH(site2RemoteOps.get(i));
				log(sb, sop.toString());
				doc0.execute(sop);
			}

			log(sb, "site 1 execute: ");
			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt1.ROH(site0RemoteOps.get(i));
				log(sb, sop.toString());
				doc1.execute(sop);
			}

			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt1.ROH(site2RemoteOps.get(i));
				log(sb, sop.toString());
				doc1.execute(sop);
			}

			log(sb, "site 2 execute: ");
			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt2.ROH(site0RemoteOps.get(i));
				log(sb, sop.toString());
				doc2.execute(sop);
			}

			for (int i = 0; i < paras.opPerSite; i++) {
				StringOperation sop = gt2.ROH(site1RemoteOps.get(i));
				log(sb, sop.toString());
				doc2.execute(sop);
			}

			String s1 = doc0.toString();
			String s2 = doc1.toString();
			String s3 = doc2.toString();

			if (s1.equals(s2) == false || s1.equals(s3) == false) {
				System.out.println("not consistent");
				System.out.println("site 0:" + s1);
				System.out.println("site 1:" + s2);
				System.out.println("site 3:" + s3);

				System.out.println(sb.toString());
				break;
			}
		}

	}

	public static StringOperation generateOperation(int ratio, int opStrLen, TextDocument doc) {
		int chance = rand.nextInt(100);
		StringOperation sop = null;
		if (chance < ratio) {
			int len = doc.getLength();
			int insPos = rand.nextInt(len + 1);
			String str = randomizeString(opStrLen);
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
				String str = randomizeString(opStrLen);
				doc.insert(insPos, str);
				sop = StringOperation.createInsOperation(insPos, str);
			}
		}

		return sop;
	}

	public static String randomizeString(int num) {
		int index = rand.nextInt(20 - num);
		return S.substring(index, index + num);
	}

	public static <T> void write(List<T> list, String folderName, String fileName) {
		PrintWriter writerR;
		Path path;
		try {

			path = Paths.get(folderName);
			Files.createDirectories(path);
			writerR = new PrintWriter(new FileWriter(folderName + "//" + fileName, true));
			for (T item : list) {
				writerR.println(item);
			}
			writerR.flush();
			writerR.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
