package etf.nim.bv140094d;

import java.util.*;

public class AlphaBeta extends Algorithm {

	public AlphaBeta(int depth, StaticFunction function) throws IllegalArgumentException {
		super(depth, function);
	}

	@Override
	public int searchImpl(Heaps heaps) {
		return alphabeta(new Node(heaps), 0, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
	}

	private int alphabeta(Node node, int currentDepth, int alpha, int beta, boolean maximizing) {
		List<Node> list;
		if (currentDepth == maxDepth || (list = node.expand()).size() == 0) {
			if (staticFunction instanceof BetterStaticFunction)
				((BetterStaticFunction) staticFunction).setTurn(currentDepth % 2 == 0);
			return staticFunction.evaluate(node.getHeaps());
		}

		int bestValue;
		if (maximizing == true) {
			bestValue = Integer.MIN_VALUE;
			for (Node n : list) {
				int currentValue = alphabeta(n, currentDepth + 1, alpha, beta, false);
				if (currentValue > bestValue) {
					bestValue = currentValue;
					if (bestValue >= beta)
						return bestValue;
					if (currentDepth == 0)
						setBestNode(n);
					if (bestValue > alpha)
						alpha = bestValue;
				}
			}
		} else {
			bestValue = Integer.MAX_VALUE;
			for (Node n : list) {
				int currentValue = alphabeta(n, currentDepth + 1, alpha, beta, true);
				if (currentValue < bestValue) {
					bestValue = currentValue;
					if (bestValue <= alpha)
						return bestValue;
					if (currentDepth == 0)
						setBestNode(n);
					if (bestValue < beta)
						beta = bestValue;
				}
			}
		}

		return bestValue;
	}

}
