package etf.nim.bv140094d;

import java.util.*;

public class Node {
	private Heaps heaps;
	private int removedFrom;
	private int checkers;
	private Node previous = null;
	private int depth;
	private boolean leaf = false;
	
	private Node(Heaps h, int rf, int c) {
		heaps = h;
		removedFrom = rf;
		checkers = c;
		depth = 0;
	}
	
	public Node(Heaps h, int rf, int c, Node prev) {
		this(h, rf, c);
		previous = prev;
		depth = prev.depth + 1;
	}
	
	public Node(Heaps h) {
		this(h, -1, -1);
	}
	
	public int getRemovedFrom() {
		return removedFrom;
	}
	
	public int getCheckersRemoved() {
		return checkers;
	}
	
	public Heaps getHeaps() {
		return heaps;
	}
	
	public Node getPrevious() {
		return previous;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setLeaf(boolean l) {
		leaf = l;
	}
	
	public boolean isLeaf() {
		return leaf;
	}
	
	public List<Node> expand() {
		List<Node> ret = new LinkedList<Node>();
		for (int i = 0; i < heaps.getNumOfHeaps(); i++) {
			for (int c: heaps.allowedToRemoveFromHeap(i)) {
				Heaps h = heaps.copy();
				try {
					h.removeCheckers(i, c);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (HeapException e) {
					e.printStackTrace();
				}
				ret.add(new Node(h, i, c, this));
			}
		}
		return ret;
	}
}
