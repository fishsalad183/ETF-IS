package etf.nim.bv140094d;

public abstract class Player {
	protected Commands commands;
	
	public Player(Commands c) {
		commands = c;
	}
	
	protected abstract int[] selectHeapAndAmount() throws InterruptedException;
	
	public void playMove() throws InterruptedException {
		while (true) {
			int[] heapAndAmount = selectHeapAndAmount();
			try {
				commands.getHeaps().removeCheckers(heapAndAmount[0], heapAndAmount[1]);
				return;
			} catch (IllegalArgumentException | HeapException e) {
				e.printStackTrace();
			}
		}
	}
}
