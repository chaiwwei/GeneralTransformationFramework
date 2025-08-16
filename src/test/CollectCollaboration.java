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
import com.TextDocumentV2;

public class CollectCollaboration {

	/**
	 * @param args the command line arguments
	 */
	static int siteNum = 3;
	static Random rand = new Random(10);
	static String S = "abcdefghij1234567890";

	public static void main(String[] args) throws Exception {
		int siteOpNum = Integer.parseInt(args[0]);
		int opStrLen = Integer.parseInt(args[1]);
		int ratio = Integer.parseInt(args[2]);
		int turn = 3;
		int iniSize = Integer.parseInt(args[3]);
		String algName = args[4];
		String fileNumber = args[5];
		String folderName = args[6];

		Parameters paras = new Parameters(siteOpNum, opStrLen, ratio, turn, iniSize, algName);
		// test();
		System.out.println("Testing " + paras.toString());

		String initialDoc = Tool.generate(iniSize);

		for (int i = 0; i < 3; i++) {
			GTAlgorithm ga = new GTAlgorithm(initialDoc, 3);
			CollabSite site = new CollabSite();
			docs.add(new TextDocumentV2(initialDoc));
		}
		test(folderName, fileNumber, paras, algs, docs);
		System.out.println("running");
	}

	public static void logTime(String folderName, String fileName, List<Long> localTimes, List<Long> remoteTimes,
			List<Integer> memorySize) {
		PrintWriter writerL;
		PrintWriter writerR;
		PrintWriter writerM;
		Path path;
		try {
			path = Paths.get(folderName + "//Local//");
			Files.createDirectories(path);
			writerL = new PrintWriter(new FileWriter(folderName + "//Local//" + fileName, true));
			for (Long time : localTimes) {
				writerL.println(time);
			}
			writerL.flush();
			writerL.close();

			path = Paths.get(folderName + "//Remote//");
			Files.createDirectories(path);
			writerR = new PrintWriter(new FileWriter(folderName + "//Remote//" + fileName, true));
			for (Long time : remoteTimes) {
				writerR.println(time);
			}
			writerR.flush();
			writerR.close();

			path = Paths.get(folderName + "//Memory//");
			Files.createDirectories(path);

			writerM = new PrintWriter(new FileWriter(folderName + "//Memory//" + fileName, true));
			for (Integer m : memorySize) {
				writerM.println(m);
			}
			writerM.flush();
			writerM.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void test(String folderName, String fileNumber, Parameters paras, List<GTAlgorithm> algList,
			List<TextDocumentV2> replicas) {

		long startTime = 0;
		long totalTime = 0;
		List<Long> localTimes = new ArrayList<Long>();
		List<Long> remoteTimes = new ArrayList<Long>();
		List<Integer> memorySize = new ArrayList<Integer>();

		for (int k = 0; k < paras.turn; k++) {
			// վ��0�������ز���

			// logTime(paras.toString() + "_remoteTime.txt", time);
		}
		logTime(folderName, fileNumber + "_" + paras.toString() + ".txt", localTimes, remoteTimes, memorySize);
		return;
	}

}