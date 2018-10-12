package etf.nim.bv140094d;

public class BetterStaticFunction extends StaticFunction {
	public static final double COEFF1_DEFAULT = 4, COEFF2_DEFAULT = 7, COEFF3_DEFAULT = 4;
	private double coeff1, coeff2, coeff3;
	private boolean myTurn = true;

	public BetterStaticFunction() {
		setCoefficients(COEFF1_DEFAULT, COEFF2_DEFAULT, COEFF3_DEFAULT);
	}

	public BetterStaticFunction(double c1, double c2, double c3) {
		setCoefficients(c1, c2, c3);
	}

	public void setCoefficients(double c1, double c2, double c3) {
		coeff1 = c1;
		coeff2 = c2;
		coeff3 = c3;
	}

	public void setTurn(boolean t) {
		myTurn = t;
	}

	@Override
	public int evaluate(Heaps heaps) {
		final double v1 = heaps.getMaxCheckersPerHeap();
		final double v2 = heaps.getNumOfHeaps();
		final double MAX_SCORE = coeff1 * v1 + coeff2 * v1 * v2 + coeff3 * v2;
		
		
		if (!heaps.legalMoveExists()) {
			if (myTurn == false)
				return (int) MAX_SCORE;
			else
				return (int) -MAX_SCORE;
		} else {
			double checkersRemoved = heaps.getLastNumOfCheckersRemoved();
			double totalPossibilities = 0;
			double numberOfEmptyHeaps = 0;
			for (int i = 0; i < heaps.getNumOfHeaps(); i++) {
				if (heaps.getCheckersInHeap(i) == 0)
					numberOfEmptyHeaps++;
				else
					totalPossibilities += heaps.allowedToRemoveFromHeap(i).length;
			}

			double c1 = coeff1 * checkersRemoved;
			double c2 = coeff2 * totalPossibilities;
			double c3 = coeff3 * numberOfEmptyHeaps;
			return (int) (c1 + c2 + c3);
		}
	}

}
