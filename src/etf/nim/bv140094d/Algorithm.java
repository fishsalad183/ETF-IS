package etf.nim.bv140094d;

public abstract class Algorithm {
	private Node bestNode = null;
	protected final int maxDepth;
	protected final StaticFunction staticFunction;

	public Algorithm(int depth, StaticFunction function) throws IllegalArgumentException {
		if (depth <= 0)
			throw new IllegalArgumentException("Search depth must be 1 or more.");
		maxDepth = depth;
		staticFunction = function;
	}
	
	protected void reset() {
		bestNode = null;
	}
	
	public Node getBestNode() {
		return bestNode;
	}
	
	protected void setBestNode(Node n) {
		bestNode = n;
	}
	
	public StaticFunction getStaticFunction() {
		return staticFunction;
	}

	public final int[] search(Heaps heaps) {
		searchImpl(heaps);
		
		Node n = getBestNode();
		Node prev = n.getPrevious();
		while (prev.getPrevious() != null) {
			n = prev;
			prev = prev.getPrevious();
		}
		reset();
		return new int[] { n.getRemovedFrom(), n.getCheckersRemoved() };
	}
	
	protected abstract int searchImpl(Heaps heaps);
}
