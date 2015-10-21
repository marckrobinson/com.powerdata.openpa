package com.powerdata.openpa.tools;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Set;
import com.powerdata.openpa.ACBranch;
import com.powerdata.openpa.ACBranchList;
import com.powerdata.openpa.PAModel;
import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.PflowModelBuilder;

public class TestLinkNet
{
	PAModel _m;
	LinkNet _lnet;
	long _ms = 0;
	long _cnt = 0;
	
	public TestLinkNet(PAModel m) throws PAModelException
	{
		_m = m;
		Set<ACBranchList> branches = _m.getACBranches();
		int nbus = _m.getBuses().size();
		int nbranch = branches.stream().mapToInt(l -> l.size()).sum();
		_lnet = new LinkNet();
		_lnet.ensureCapacity(nbus-1, nbranch);
		for(ACBranchList list : branches)
		{
			for(ACBranch b : list)
				_lnet.addBranch(b.getFromBus().getIndex(), b.getToBus().getIndex());
		}
		
	}
	
	public void test(PrintWriter pw)
	{
		_lnet.eliminateBranch(0, true);
		_lnet.eliminateBranch(0, false);
		long start = System.nanoTime();
		pw.println(_lnet.findGroups());
		_ms += (System.nanoTime() - start);
		++_cnt;
	}
	
	public double getAvgTime()
	{
		double rv = ((double) _ms / (((double)_cnt)*1000000.0));
		_ms = 0;
		_cnt = 0;
		return rv;
	}
	
	public static void main(String...args) throws Exception
	{
		String uri = null;
		File poutdir = new File(System.getProperty("user.dir"));
		for(int i=0; i < args.length;)
		{
			String s = args[i++].toLowerCase();
			int ssx = 1;
			if (s.startsWith("--")) ++ssx;
			switch(s.substring(ssx))
			{
				case "uri":
					uri = args[i++];
					break;
				case "outdir":
					poutdir = new File(args[i++]);
					break;
			}
		}
		if (uri == null)
		{
			System.err.format("Usage: -uri model_uri "
					+ "[ --outdir output_directory (deft to $CWD ]\n");
			System.exit(1);
		}
		final File outdir = poutdir;
		if (!outdir.exists()) outdir.mkdirs();
		TestLinkNet tln = new TestLinkNet(PflowModelBuilder.Create(uri).load());
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/dev/null")));
		
		int niter = 10000;
		for(int i=0; i < niter; ++i)
		{
			tln.test(out);
		}
		
		System.out.format("% d iterations in %f ms\n", niter, tln.getAvgTime());
	}
}
