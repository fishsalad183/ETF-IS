package etf.nim.bv140094d;

public class Game extends Thread {
	private Heaps heaps;
	private Player[] players;
	int turn;
	private Commands commands;

	public Game(Heaps h, Player p1, Player p2, Commands c) {
		players = new Player[2];
		players[0] = p1;
		players[1] = p2;
		turn = 0;

		heaps = h;

		commands = c;

		start();
	}

	public Heaps getHeaps() {
		return heaps;
	}

	public Commands getCommands() {
		return commands;
	}

	public boolean isOver() {
		if (getWinner().equals(""))
			return false;
		return true;
	}

	public String getWinner() {
		if (!heaps.legalMoveExists()) // should cover all heaps having 0
										// checkers
			return Integer.toString(2 - turn); // (2 - turn) because player
												// number
												// is 1 or 2, not 0 or 1
		return "";
	}

	@Override
	public void run() {
		String w = "";
		commands.setTurnLabel(turn);
		try {
			while (!interrupted() && w.equals("")) {
				players[turn].playMove();
				turn = 1 - turn;
				commands.setTurnAndCheckersLabels(turn, heaps.getLastNumOfCheckersRemoved());
				w = getWinner();
			}
		} catch (InterruptedException e) {
		}
		commands.setWinnerLabel(w);
	}
}
