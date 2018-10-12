package etf.nim.bv140094d;

public class Human extends Player {

	public Human(Commands c) {
		super(c);
	}

	@Override
	protected int[] selectHeapAndAmount() throws InterruptedException {
		return commands.getCommandParameters();
	}

}
