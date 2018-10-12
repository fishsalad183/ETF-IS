package etf.nim.bv140094d;

import java.util.*;

public class Minimax extends Algorithm {

	public Minimax(int depth, StaticFunction function) throws IllegalArgumentException {
		super(depth, function);
	}

	@Override
	public int searchImpl(Heaps heaps) {
		return minimax(new Node(heaps), 0, true);
	}

	private int minimax(Node node, int currentDepth, boolean maximizing) {
		List<Node> list;
		if (currentDepth == maxDepth || (list = node.expand()).size() == 0) {
			return staticFunction.evaluate(node.getHeaps());
		}

		int bestValue;
		if (maximizing == true) {
			bestValue = Integer.MIN_VALUE;
			for (Node n : list) {
				int currentValue = minimax(n, currentDepth + 1, false);
				if (currentValue > bestValue) {
					bestValue = currentValue;
					if (currentDepth == 0)
						setBestNode(n);
				}
			}
		} else {
			bestValue = Integer.MAX_VALUE;
			for (Node n : list) {
				int currentValue = minimax(n, currentDepth + 1, true);
				if (currentValue < bestValue) {
					bestValue = currentValue;
					if (currentDepth == 0)
						setBestNode(n);
				}
			}
		}

		return bestValue;
	}

}
