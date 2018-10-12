package etf.nim.bv140094d;

public class Computer extends Player {
	private Heaps heaps;
	private Algorithm algorithm;
	private Game game;

	public static enum Level {
		SIMPLE, SIMPLE_ALPHA_BETA, COMPETITIVE_ALPHA_BETA
	};

	public Computer(Commands c, Level l, int depth) { // Commands as argument?
		super(c);
		heaps = commands.getHeaps();

		if (l == Level.SIMPLE) {
			algorithm = new Minimax(depth, new SimpleStaticFunction());
		} else if (l == Level.SIMPLE_ALPHA_BETA) {
			algorithm = new AlphaBeta(depth, new SimpleStaticFunction());
		} else {
			algorithm = new AlphaBeta(depth, new BetterStaticFunction());
		}
	}

	public boolean setCoeffs(double c1, double c2, double c3) {
		StaticFunction fun = algorithm.getStaticFunction();
		if (fun instanceof BetterStaticFunction) {
			((BetterStaticFunction) fun).setCoefficients(c1, c2, c3);
			return true;
		} else
			return false;
	}

	private synchronized void pretendToThink() throws InterruptedException {
		wait(1000);
	}

	@Override
	protected int[] selectHeapAndAmount() throws InterruptedException {
		// pretendToThink();
		long startTime = System.currentTimeMillis();
		int[] ret = algorithm.search(heaps);
		commands.setDiagnosticsLabel(System.currentTimeMillis() - startTime);
		return ret;
	}

}
