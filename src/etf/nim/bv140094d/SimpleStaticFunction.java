package etf.nim.bv140094d;

public class SimpleStaticFunction extends StaticFunction {
	
	@Override
	public int evaluate(Heaps heaps) {
		int ret = 0;
		for (int i = 0; i < heaps.getNumOfHeaps(); i++)
			ret ^= heaps.getCheckersInHeap(i);
		return ret;
	}
}
