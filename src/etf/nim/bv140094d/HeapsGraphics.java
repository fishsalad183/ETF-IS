package etf.nim.bv140094d;

import java.awt.*;

public class HeapsGraphics extends Heaps {
	private HeapCanvas[] canvases;

	public HeapsGraphics(int maxCheckersPerHeap, int[] heaps, Panel p) throws IllegalArgumentException, HeapException {
		super(maxCheckersPerHeap, heaps);
		
		p.removeAll();
		p.setLayout(new GridLayout(1, 0));

		canvases = new HeapCanvas[heaps.length];
		for (int i = 0; i < heaps.length; i++) {
			canvases[i] = new HeapCanvas(i);
			p.add(canvases[i]);
		}
	}

	private class HeapCanvas extends Canvas {
		private int heapNumber;

		public HeapCanvas(int i) {
			super();
			heapNumber = i;
		}

		@Override
		public void paint(Graphics g) {
			int a = (getWidth() - 1) / 2;
			int b = (getHeight() - 1 - 2 * (getMaxCheckersPerHeap() + 1)) / (getMaxCheckersPerHeap() + 1);
			g.setColor(Color.BLACK);
			for (int i = 0; i < getCheckersInHeap(heapNumber); i++) {
				g.fillRect((getWidth() - a) / 2, getHeight() - ((i + 1) * (b + 1)), a, b);
			}
			g.drawString("HEAP " + (heapNumber + 1), (getWidth() - a) / 2, b);
		}
	}
	
	@Override
	public boolean removeCheckers(int heapNumber, int checkers) throws IllegalArgumentException, HeapException {
		boolean ret = super.removeCheckers(heapNumber, checkers);
		canvases[heapNumber].repaint();
		return ret;
	}
}
