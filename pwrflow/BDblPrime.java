package com.powerdata.openpa.pwrflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;
import com.powerdata.openpa.ACBranch;
import com.powerdata.openpa.ACBranchList;
import com.powerdata.openpa.BusList;
import com.powerdata.openpa.BusRefIndex;
import com.powerdata.openpa.FixedShunt;
import com.powerdata.openpa.FixedShuntListIfc;
import com.powerdata.openpa.PAModel;
import com.powerdata.openpa.PAModelException;
import com.powerdata.openpa.PflowModelBuilder;
import com.powerdata.openpa.SubLists;
import com.powerdata.openpa.pwrflow.ACBranchExtList.ACBranchExt;
import com.powerdata.openpa.tools.PAMath;
import com.powerdata.openpa.tools.SpSymFltMatrix;


/**
 * Create a B'' matrix.  
 * 
 * TODO:  Further research is needed to apply SVC's to b''.  The EPRI OTS appears to adjust
 * b'' based on SVC state.  However, no other implementations reviewed to date appear
 * concerned.  For now, it's ignored.
 * 
 * @author chris@powerdata.com
 *
 * @param <T>
 *            An ACBranchExt or subclass that contains admittance values (avoids
 *            a lot of recalculations)
 */

public class BDblPrime extends SpSymFltMatrix
{
	public static class MatrixElementBuilder extends
		ACBranchAdjacencies.MatrixElementBuilder
	{
		public MatrixElementBuilder(int nbus, int nbranch)
		{
			super(nbus, nbranch);
		}

		@Override
		public void build(ACBranchExt brx, int branchIndex, int frombus, int tobus)
			throws PAModelException
		{
			ACBranch br = brx.getBranch();
			float a = br.getFromTap();
			float b = br.getToTap();
			float bm = br.getBmag();
			float y = -brx.getY().im();
			_bdiag[frombus] += y/(b*b) - bm - br.getFromBchg();
			_bdiag[tobus] += y/(a*a) - bm - br.getToBchg();
			_boffdiag[branchIndex] -= y/(a*b);
		}
		
		/**
		 * Convenience method to add all the fixed shunts to b''
		 * @param fsh Collection of fixed shunt lists
		 * @param bri Bus reference to handle single-bus topology 
		 * @param sbase system MVA base
		 * @throws PAModelException
		 */
		public void addFixedShunts(Collection<? extends FixedShuntListIfc<? extends FixedShunt>> fsh,
			BusRefIndex bri, float sbase) throws PAModelException
		{
			BusList buses = bri.getBuses();
			for(FixedShuntListIfc<? extends FixedShunt> list : fsh)
			{
				for(FixedShunt sh : list)
				{
					_bdiag[buses.getByBus(sh.getBus()).getIndex()] -= 
						PAMath.mva2pu(sh.getB(), sbase);
				}
			}
		}
	}

	/**
	 * Constructor for sharing of adjacencies & external element building
	 * 
	 * @param adj branch adjacency matrix
	 * @param bldr class to build elements for the matrix
	 */

	public BDblPrime(ACBranchAdjacencies adj, MatrixElementBuilder bldr)
	{
		construct(adj, bldr);
	}

	protected void construct(ACBranchAdjacencies adj, MatrixElementBuilder bldr)
	{
		setAdjacencies(adj);
		setBDiag(bldr.getBDiag());
		setBOffDiag(bldr.getBOffDiag());
	}
	
	public BDblPrime(PAModel model) throws PAModelException
	{
		BusRefIndex bri = BusRefIndex.CreateFromSingleBuses(model);
		BusList buses = bri.getBuses();
		Set<ACBranchList> branches = SubLists.getBranchInsvc(model.getACBranches());
		MatrixElementBuilder bldr = new MatrixElementBuilder(buses.size(), 
			branches.stream().mapToInt(i -> i.size()).sum());
		ACBranchAdjacencies adj = new ACBranchAdjacencies(
			ACBranchExtList.LoadExtension(branches, bri),buses, bldr);
		bldr.addFixedShunts(SubLists.getFixedShuntInsvc(model.getFixedShunts()), bri, 100f);
		construct(adj, bldr);
		
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
		PflowModelBuilder bldr = PflowModelBuilder.Create(uri);
		bldr.setLeastX(0.0001f);
		PAModel m = bldr.load();
		BDblPrime bp = new BDblPrime(m);
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "bpp.csv"))));
		bp.dump(m.getSingleBus().getName(), pw);
		pw.flush();
		pw.close();
	}
	
}



//public class BDblPrime<T extends ACBranchExt> extends BPrime
//{
//	BusList _buses;
//	float _sbase = 100f;
////	SVCCalcList _svc;
////	float[] _lastsvc;
//	
//	public BDblPrime(ACBranchAdjacencies<T> adj,
//			Collection<FixedShuntListIfc<? extends FixedShunt>> fsh, 
//			SVCCalcList svc, BusRefIndex bri)
//			throws PAModelException
//	{
//		super(adj, new ACBranchBppElemBldr(adj));
//		_buses = bri.getBuses();
//		addFixedShunts(fsh);
////		_svc = svc;
////		_lastsvc = new float[_svc.size()];
////		addSVCs(m.getSVCs(), bri);
//		//TODO:  dynamically adjust B'' for SVC's that are at their reactive limits
//	}
//
//	void addFixedShunts(Collection<? extends List<? extends FixedShunt>> lists)
//		throws PAModelException
//	{
//		for(List<? extends FixedShunt> fshlist : lists)
//		{
//			for(FixedShunt fsh : fshlist)
//			{
//				_bdiag[_buses.getByBus(fsh.getBus()).getIndex()] -= 
//					PAMath.mva2pu(fsh.getB(), _sbase);
//			}
//		}
//	}
////	public void adjustSVC() throws PAModelException
////	{
////		for (SVCCalc c : _svc)
////		{
////			int ix = c.getIndex();
////			float last = _lastsvc[ix];
////			int bidx = c.getBus().getIndex();
////			_bdiag[bidx] += last;
////			last = c.getBpp();
////			_bdiag[bidx] -= last;
////		}
////	}
//	public static void main(String...args) throws Exception
//	{
//		String uri = null;
//		File poutdir = new File(System.getProperty("user.dir"));
//		for(int i=0; i < args.length;)
//		{
//			String s = args[i++].toLowerCase();
//			int ssx = 1;
//			if (s.startsWith("--")) ++ssx;
//			switch(s.substring(ssx))
//			{
//				case "uri":
//					uri = args[i++];
//					break;
//				case "outdir":
//					poutdir = new File(args[i++]);
//					break;
//			}
//		}
//		if (uri == null)
//		{
//			System.err.format("Usage: -uri model_uri "
//					+ "[ --outdir output_directory (deft to $CWD ]\n");
//			System.exit(1);
//		}
//		final File outdir = poutdir;
//		if (!outdir.exists()) outdir.mkdirs();
//		PflowModelBuilder bldr = PflowModelBuilder.Create(uri);
//		bldr.enableFlatVoltage(true);
//		bldr.setLeastX(0.0001f);
//		bldr.setUnitRegOverride(false);
////		bldr.enableRCorrection(true);
//		PAModel m = bldr.load();
//		
//		BusRefIndex bri = BusRefIndex.CreateFromSingleBuses(m);
//		// create extended list that computes Y and accounts for out-of-service branches
//		ArrayList<ACBranchExtList<ACBranchExt>> acy = new ArrayList<>();
//		for(ACBranchList list : m.getACBranches())
//			acy.add(new ACBranchExtListI<ACBranchExt>(list, bri));
//		ACBranchAdjacencies<ACBranchExt> adj = new ACBranchAdjacencies<>(acy, bri.getBuses().size());
//		BDblPrime<ACBranchExt> bpp = new BDblPrime<ACBranchExt>(adj, ACPowerCalc.setupFixedShunts(m), null, bri);
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "bpp.csv"))));
//		bpp.dump(bri.getBuses().getName(), pw);
//		pw.flush();
//		pw.close();
//	}
//}
