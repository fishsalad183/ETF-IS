package etf.nim.bv140094d;

public class Heaps implements Cloneable { // Heaps of checkers.
	private int maxCheckersPerHeap;
	private int[] heaps;

	private int lastNumOfCheckersRemoved;

	public int getNumOfHeaps() {
		return heaps.length;
	}

	public int getMaxCheckersPerHeap() {
		return maxCheckersPerHeap;
	}

	public int getCheckersInHeap(int i) {
		return heaps[i]; // error check?
	}

	public int getLastNumOfCheckersRemoved() {
		return lastNumOfCheckersRemoved;
	}

	public Heaps(int maxCheckersPerHeap, int[] heaps) throws IllegalArgumentException, HeapException {
		checkParameters(maxCheckersPerHeap, heaps); // throws
													// exceptions
		if (!Heaps.firstMoveExists(maxCheckersPerHeap, heaps))
			throw new HeapException("No legal move for first player.");

		this.maxCheckersPerHeap = maxCheckersPerHeap;
		this.heaps = new int[heaps.length];
		for (int i = 0; i < this.heaps.length; i++)
			this.heaps[i] = heaps[i]; // copies values

		lastNumOfCheckersRemoved = 0;
	}

	public static void checkParameters(int maxCheckersPerHeap, int[] heaps)
			throws IllegalArgumentException, HeapException {
		for (int i = 0; i < heaps.length; i++) {
			if (heaps[i] <= 0 || heaps[i] > maxCheckersPerHeap)
				throw new IllegalArgumentException("Number of checkers in heap " + i
						+ " is less than or equal to 0 or larger than maximum number.");
			for (int j = i + 1; j < heaps.length; j++) {
				if (heaps[i] == heaps[j])
					throw new HeapException("Heaps " + i + " and " + j + " have same number of checkers.");
			}
		}
	}

	public boolean removeCheckers(int heapNumber, int checkers) throws IllegalArgumentException, HeapException {
		if (!moveIsLegal(heapNumber, checkers))
			return false;

		heaps[heapNumber] -= checkers;
		lastNumOfCheckersRemoved = checkers;
		return true;
	}

	private boolean legalMoveExists(int[] heaps) { // has argument for checking
													// in
													// constructor
		int maxCheckersAllowed;
		if (lastNumOfCheckersRemoved <= 0) // Means that it is the first move.
			maxCheckersAllowed = maxCheckersPerHeap;
		else
			maxCheckersAllowed = 2 * lastNumOfCheckersRemoved;

		for (int i = 0; i < heaps.length; i++) {
			for (int c = 1; c <= maxCheckersAllowed && c <= heaps[i]; c++) {
				if (Heaps.heapStatesLegalAfterRemoval(heaps, i, c))
					return true;
			}
		}

		return false;
	}

	public boolean legalMoveExists() {
		return legalMoveExists(heaps);
	}

	public static boolean firstMoveExists(int maxCheckersPerHeap, int[] heaps) {
		try {
			checkParameters(maxCheckersPerHeap, heaps);
		} catch (HeapException e) {
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < heaps.length; i++) {
			for (int c = 1; c <= heaps[i]; c++) {
				if (Heaps.heapStatesLegalAfterRemoval(heaps, i, c))
					return true;
			}
		}
		return false;
	}

	private static boolean heapStatesLegalAfterRemoval(int[] heaps, int heapNumber, int checkersToRemove) {
		int checkersAfterRemoval = heaps[heapNumber] - checkersToRemove;
		if (checkersAfterRemoval > 0) {
			for (int i = 0; i < heaps.length; i++) {
				if (i == heapNumber)
					continue;
				if (checkersAfterRemoval == heaps[i])
					return false; // If there is any
									// heap with
									// same number
									// of checkers
									// after
									// removal, it
									// is illegal.
			}
			return true; // No heap has same number of checkers after
							// removal and that is legal.
		} else if (checkersAfterRemoval == 0)
			return true; // checkersAfterRemoval may always be 0
		else
			return false;
	}

	public boolean moveIsLegal(int heapNumber, int checkersToRemove) throws IllegalArgumentException, HeapException {
		if (checkersToRemove <= 0)
			throw new IllegalArgumentException(
					"Number of checkers removed must be 1 or more. Attempted to remove " + checkersToRemove);
		if (heaps[heapNumber] - checkersToRemove < 0)
			throw new HeapException("Cannot remove more checkers than already existing on heap " + heapNumber
					+ " (removing " + checkersToRemove + " checkers from " + heaps[heapNumber]);

		if (lastNumOfCheckersRemoved > 0 && checkersToRemove > 2 * lastNumOfCheckersRemoved)
			return false;

		if (!heapStatesLegalAfterRemoval(heaps, heapNumber, checkersToRemove))
			return false;
		return true;
	}

	public int[] allowedToRemoveFromHeap(int i) {
		Integer[] checkers = new Integer[MainFrame.MAX_HEAPS_AND_CHECKERS_PER_HEAP];
		int moves = 0;
		for (int c = 1; c <= heaps[i]; c++) {
			try {
				if (moveIsLegal(i, c))
					checkers[moves++] = c;
			} catch (IllegalArgumentException | HeapException e) {
				e.printStackTrace();
			}
		}
		int[] ret = new int[moves];
		int ind = 0;
		for (Integer c : checkers)
			if (c != null)
				ret[ind++] = c.intValue();
		return ret;
	}

	private Heaps(int m, int[] h, int l) { // A constructor that does not
											// perform any checks; used for
											// copying.
		maxCheckersPerHeap = m;
		heaps = new int[h.length];
		for (int i = 0; i < heaps.length; i++)
			heaps[i] = h[i];
		lastNumOfCheckersRemoved = l;
	}

	public Heaps copy() {
		return new Heaps(maxCheckersPerHeap, heaps, lastNumOfCheckersRemoved);
	}
}
