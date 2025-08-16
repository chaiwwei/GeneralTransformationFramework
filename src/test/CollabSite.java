package test;
import java.util.ArrayList;

import com.GTAlgorithm;
import com.StringOperation;
import com.TextDocumentV2;
import com.TimeStampedOpString;

public class CollabSite {
	GTAlgorithm gt;
	TextDocumentV2 doc;
	Server server;
	public int sid;

	public CollabSite(int sid, GTAlgorithm gt, TextDocumentV2 doc, Server server) {
		this.sid = sid;
		this.gt = gt;
		this.doc = doc;
		this.server = server;
	}

	public long start(int ratio, int len, int opNum) {
		long totalTime = 0;
		long startTime = 0;

		totalTime = 0;
		for (int j = 0; j < opNum; j++) {
			StringOperation sop = Tool.generateOperation(ratio, len, this.doc);
			startTime = System.nanoTime();
			String aop = this.gt.LOH(sop);
			totalTime = totalTime + (System.nanoTime() - startTime) / 1000;
			this.server.recvOperations(this.sid, aop);
		}
		return totalTime / opNum;
	}

	public long handleRemote(ArrayList<TimeStampedOpString> ops) {
		long startTime = 0;
		long totalTime = 0;
		int size = ops.size();
		for (int i = 0; i < size; i++) {
			TimeStampedOpString tstr = ops.get(i);
			if (tstr.sid != this.sid) {
				startTime = System.nanoTime();
				StringOperation sop = this.gt.ROH(tstr);
				totalTime = totalTime + (System.nanoTime() - startTime) / 1000;
				this.doc.execute(sop);
			}else
			{
				this.gt.receiveLocal(tstr);
			}
		}
		return totalTime / size;
	}
}
