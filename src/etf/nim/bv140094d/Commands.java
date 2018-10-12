package etf.nim.bv140094d;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigInteger;

public class Commands {
	private Panel panel;
	private Heaps heaps;
	private HeapNumber heapNumber;
	private Choice checkers;
	private PlayMoveButton playMoveButton;
	private int[] commandParameters = new int[2];
	private Label turnLabel, checkersLabel, diagnosticsLabel;
	private BigInteger totalComputerTime = new BigInteger(Integer.toString(0));
	private int computerMoves = 0;

	public Commands(Panel p, Heaps h) {
		panel = p;
		heaps = h;

		panel.removeAll();

		panel.setLayout(new GridLayout(1, 2));
		panel.setEnabled(false);
		Panel p1 = new Panel();
		Panel p2 = new Panel();
		p1.setLayout(new GridLayout(0, 1));
		p2.setLayout(new GridLayout(0, 1));
		panel.add(p1);
		panel.add(p2);

		turnLabel = new Label("", Label.CENTER);
		turnLabel.setSize(p1.getWidth(), turnLabel.getHeight());
		p1.add(turnLabel);
		checkersLabel = new Label("", Label.CENTER);
		checkersLabel.setSize(p1.getWidth(), checkersLabel.getHeight());
		p1.add(checkersLabel);
		diagnosticsLabel = new Label("", Label.CENTER);
		diagnosticsLabel.setSize(p1.getWidth(), checkersLabel.getHeight());
		p1.add(diagnosticsLabel);

		heapNumber = new HeapNumber();
		Panel auxPanel = new Panel();
		auxPanel.add(new Label("Choose heap to remove from:"));
		auxPanel.add(heapNumber);
		p2.add(auxPanel);

		checkers = new Choice();
		auxPanel = new Panel();
		auxPanel.add(new Label("How many checkers to remove:"));
		auxPanel.add(checkers);
		p2.add(auxPanel);

		playMoveButton = new PlayMoveButton();
		playMoveButton.setSize(p2.getWidth() / 2, playMoveButton.getHeight());
		p2.add(playMoveButton);
	}

	private class HeapNumber extends Choice {

		public HeapNumber() {
			add("");
			select("");
			for (int i = 1; i <= heaps.getNumOfHeaps(); i++) {
				add(Integer.toString(i));
			}
			addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent event) {
					if (!getSelectedItem().equals("")) {
						refreshCheckers();
						playMoveButton.setEnabled(checkers.getItemCount() > 0);
					}
					else playMoveButton.setEnabled(false);
				}
			});
		}

		public void refreshCheckers() {
			checkers.removeAll();
			if (heapNumber.getItemCount() > 0 && !heapNumber.getSelectedItem().equals("")) { // in
																								// case
																								// there
																								// are
																								// no
																								// heaps
				int h = Integer.parseInt(getSelectedItem()) - 1;
				for (int c : heaps.allowedToRemoveFromHeap(h))
					checkers.add(Integer.toString(c));
			}
		}
	}

	private class PlayMoveButton extends Button {
		public PlayMoveButton() {
			super("Play move");
			setEnabled(false);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					commandParameters[0] = Integer.parseInt(heapNumber.getSelectedItem()) - 1;
					commandParameters[1] = Integer.parseInt(checkers.getSelectedItem());
					notifySynchronized();
				}
			});
		}
	}

	private void refreshHeapNumber() {
		heapNumber.select("");
		playMoveButton.setEnabled(false);
		for (int i = 0; i < heaps.getNumOfHeaps(); i++)
			try {	// removing a choice throws an exception if it does not exist
				if (heaps.getCheckersInHeap(i) == 0)
					heapNumber.remove(Integer.toString(i + 1));
			} catch (IllegalArgumentException e) {
			}
	}

	public synchronized int[] getCommandParameters() throws InterruptedException {
		refreshHeapNumber();
		panel.setEnabled(true);
		wait();
		panel.setEnabled(false);
		return commandParameters;
	}

	private synchronized void notifySynchronized() {
		notify();
	}

	// Some more efficient way?
	public void setTurnLabel(int i) {
		turnLabel.setText("Turn: " + (i + 1));
	}

	public void setWinnerLabel(String s) {
		turnLabel.setText("Winner: " + s);
		checkersLabel.setText("");
	}

	public void setCheckersLabel(int c) {
		checkersLabel.setText("Last number of checkers removed: " + c);
	}

	public void setTurnAndCheckersLabels(int i, int c) {
		setTurnLabel(i);
		setCheckersLabel(c);
	}
	
	public void setDiagnosticsLabel(long millis) {
		totalComputerTime = totalComputerTime.add(new BigInteger(Long.toString(millis)));
		computerMoves++;
		diagnosticsLabel.setText("Average computation time per move: " + totalComputerTime.divide(new BigInteger(Integer.toString(computerMoves))) + " miliseconds. Moves: " + computerMoves);
	}

	public Heaps getHeaps() {
		return heaps;
	}
}
