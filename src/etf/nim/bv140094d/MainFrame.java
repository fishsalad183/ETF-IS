package etf.nim.bv140094d;

import java.awt.*;
import java.awt.event.*;

import etf.nim.bv140094d.Computer.Level;

public class MainFrame extends Frame {
	public static final int MAX_HEAPS_AND_CHECKERS_PER_HEAP = 10;
	private HeapsGraphics heaps;
	private Game game;
	private Panel graphicsPanel;
	private Panel commandPanel;
	private NewGameDialog newGameDialog;

	private MainFrame() {
		super("Nim");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		fillFrame();
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				end();
			}
		});

		heaps = null;
		game = null;
	}

	private void end() {
		if (game != null)
			game.interrupt();
		dispose();
	}

	private void fillFrame() {
		setLayout(new BorderLayout());
		setMenuBar(new MyMenuBar());
		newGameDialog = new NewGameDialog(this);
		commandPanel = new Panel();
		add(commandPanel, "South");
		graphicsPanel = new Panel();
		add(graphicsPanel, "Center");
	}

	private class MyMenuBar extends MenuBar {
		public MyMenuBar() {
			Menu menu = new Menu("Menu");
			add(menu);

			MenuItem item = new MenuItem("New game");
			menu.add(item);
			item.setShortcut(new MenuShortcut('N'));
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					newGameDialog.setVisible(true);
				}
			});

			item = new MenuItem("Exit");
			menu.add(item);
			item.setShortcut(new MenuShortcut('E'));
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					end();
				}
			});
		}
	}

	private class NewGameDialog extends Dialog {
		public static final int DEFAULT_DEPTH = 5;
		private CheckboxGroup mode, level;
		private Checkbox hh, hc, ch, cc;
		private Checkbox simple, simple_ab, competitive;
		private NumberChoice numberOfHeaps;
		private NumberChoice[] checkersPerHeap;
		private ButtonOK buttonOK;
		private Panel centerPanel;
		private Panel northPanel;
		private TextField warning;
		private TextField depthField;

		public NewGameDialog(Frame parent) {
			super(parent, true);
			setLayout(new BorderLayout());
			setSize(700, 500);
			setLocationRelativeTo(MainFrame.this);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent windowEvent) {
					dispose();
				}
			});

			createWestPanel();

			northPanel = new Panel();
			warning = new TextField(80);
			warning.setFont(new Font(null, Font.BOLD, 12));
			warning.setEditable(false);
			northPanel.add(warning);
			add(northPanel, "North");

			Panel choicePanel = new Panel();
			choicePanel.setLayout(new GridLayout(0, 1));
			choicePanel.add(new Label("Checkers per heap:", Label.CENTER));
			checkersPerHeap = new NumberChoice[MAX_HEAPS_AND_CHECKERS_PER_HEAP];
			for (int i = 0; i < checkersPerHeap.length; i++) {
				checkersPerHeap[i] = new NumberChoice();
				checkersPerHeap[i].select(Integer.toString(i + 1));
				choicePanel.add(checkersPerHeap[i]);
				checkersPerHeap[i].setEnabled(false);
				checkersPerHeap[i].addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent event) {
						if (Heaps.firstMoveExists(MAX_HEAPS_AND_CHECKERS_PER_HEAP, readHeaps())) {
							buttonOK.setEnabled(true);
							warning.setText("");
						} else {
							buttonOK.setEnabled(false);
							warning.setText(
									"First player has no legal move or some heaps have the same number of checkers.");
						}
					}
				});
			}
			add(choicePanel, "East");

			centerPanel = new Panel();
			centerPanel.setLayout(new GridLayout(0, 1));
			centerPanel.add(new Label("Number of heaps:", Label.CENTER));
			numberOfHeaps = new NumberChoice();
			numberOfHeaps.setSize(centerPanel.getWidth() / 2, numberOfHeaps.getHeight());
			numberOfHeaps.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent event) {
					int numHeaps = Integer.parseInt(numberOfHeaps.getSelectedItem());
					for (int i = 0; i < MAX_HEAPS_AND_CHECKERS_PER_HEAP; i++) {
						if (i < numHeaps)
							checkersPerHeap[i].setEnabled(true);
						else
							checkersPerHeap[i].setEnabled(false);
					}
					if (Heaps.firstMoveExists(MAX_HEAPS_AND_CHECKERS_PER_HEAP, readHeaps())) {
						buttonOK.setEnabled(true);
						warning.setText("");
					} else {
						buttonOK.setEnabled(false);
						warning.setText(
								"First player has no legal move or some heaps have the same number of checkers.");
					}
				}
			});
			centerPanel.add(numberOfHeaps);
			add(centerPanel, "Center");

			Panel panel;
			panel = new Panel();
			buttonOK = new ButtonOK();
			panel.add(buttonOK);
			buttonOK.setEnabled(false);
			Button button = new Button("Cancel");
			panel.add(button);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					NewGameDialog.this.setVisible(false);
				}
			});
			add(panel, "South");
		}

		private class NumberChoice extends Choice {
			public NumberChoice() {
				for (int i = 1; i <= MAX_HEAPS_AND_CHECKERS_PER_HEAP; i++)
					add(Integer.toString(i));
			}
		}

		private int[] readHeaps() {
			int numHeaps = Integer.parseInt(numberOfHeaps.getSelectedItem());
			int h[] = new int[numHeaps];
			for (int j = 0; j < h.length; j++)
				h[j] = Integer.parseInt(checkersPerHeap[j].getSelectedItem());
			return h;
		}

		private class ButtonOK extends Button {
			public ButtonOK() {
				super("OK");
				addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						int depth;
						try {
							depth = Integer.parseInt(depthField.getText());
						} catch (NumberFormatException e) {
							warning.setText("Computer search depth not a number.");
							return;
						}
						if (game != null)
							game.interrupt();
						try {
							heaps = new HeapsGraphics(MAX_HEAPS_AND_CHECKERS_PER_HEAP, readHeaps(), graphicsPanel);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (HeapException e) {
							e.printStackTrace();
						}
						Commands c = new Commands(commandPanel, heaps);
						Player p1;
						Player p2;
						if (mode.getSelectedCheckbox() == hh) {
							p1 = new Human(c);
							p2 = new Human(c);
						} else {
							Computer.Level l;
							Checkbox levelCheckbox = level.getSelectedCheckbox();
							if (levelCheckbox == simple)
								l = Computer.Level.SIMPLE;
							else if (levelCheckbox == simple_ab)
								l = Computer.Level.SIMPLE_ALPHA_BETA;
							else
								l = Computer.Level.COMPETITIVE_ALPHA_BETA;
							if (mode.getSelectedCheckbox() == hc) {
								p1 = new Human(c);
								p2 = new Computer(c, l, depth);
							} else if (mode.getSelectedCheckbox() == ch) {
								p1 = new Computer(c, l, depth);
								p2 = new Human(c);
							} else {
								p1 = new Computer(c, l, depth);
								p2 = new Computer(c, l, depth);
							}
						}
						if (game != null)
							game.interrupt();
						game = new Game(heaps, p1, p2, c);
						NewGameDialog.this.setVisible(false);
						MainFrame.this.setVisible(true); // to display canvases
															// and commands
					}
				});
			}
		}

		private void createWestPanel() {
			Panel panel = new Panel();
			panel.setLayout(new GridLayout(0, 1));

			panel.add(new Label("Player mode:", Label.CENTER));
			mode = new CheckboxGroup();
			hh = new Checkbox("Human vs human", mode, true);
			hc = new Checkbox("Human vs computer", mode, false);
			ch = new Checkbox("Computer vs human", mode, false);
			cc = new Checkbox("Computer vs computer", mode, false);
			panel.add(hh);
			panel.add(hc);
			panel.add(ch);
			panel.add(cc);

			panel.add(new Label("Computer level:", Label.CENTER));
			level = new CheckboxGroup();
			simple = new Checkbox("Simple", level, true);
			simple_ab = new Checkbox("Simple with alpha-beta", level, false);
			competitive = new Checkbox("Competitive", level, false);
			panel.add(simple);
			panel.add(simple_ab);
			panel.add(competitive);

			panel.add(new Label("Computer search depth:", Label.CENTER));
			depthField = new TextField(Integer.toString(DEFAULT_DEPTH));
			panel.add(depthField);

			add(panel, "West");
		}
	}

	public static void main(String[] args) {
		new MainFrame();

//		int[] array = { 10, 2, 9, 5, 6, 7, 8, 3, 1 };
//		int d = 3;
//		final int START = 2, END = 8, JUMP = 1, MATCHES = 20;
//		final int START_C1 = 3, START_C2 = 3, START_C3 = 3;
//		String msg = "";
//		int maxWins = 0;
//		for (int c1 = START_C1; c1 <= END; c1 += JUMP)
//			for (int c2 = START_C2; c2 <= END; c2 += JUMP)
//				for (int c3 = START_C3; c3 <= END; c3 += JUMP) {
//					if (c1 == BetterStaticFunction.COEFF1_DEFAULT && c2 == BetterStaticFunction.COEFF2_DEFAULT && c3 == BetterStaticFunction.COEFF3_DEFAULT)
//						continue;
//					int wins = 0;
//					for (int i = 0; i < MATCHES; i++) {
//						Heaps h = null;
//						try {
//							h = new Heaps(MainFrame.MAX_HEAPS_AND_CHECKERS_PER_HEAP, array);
//						} catch (IllegalArgumentException | HeapException e) {
//							e.printStackTrace();
//						}
//						Commands c = new Commands(new Panel(), h);
//						Computer p1 = new Computer(c, Level.COMPETITIVE_ALPHA_BETA, d);
//						Computer p2 = new Computer(c, Level.COMPETITIVE_ALPHA_BETA, d);
//						p1.setCoeffs(c1, c2, c3);
//						Game g = new Game(h, p1, p2, c);
//						try {
//							g.join();
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						if (Integer.parseInt(g.getWinner()) == 1) wins++;
//					}
//					if (wins >= maxWins) {
//						String msgTemp = "" + c1 + " " + c2 + " " + c3 + " " + wins;
//						System.out.println(msgTemp);
//						if (wins > maxWins) {
//							maxWins = wins;
//							msg = msgTemp;
//						}
//					}
//				}
//		System.out.println(msg);
	}

}
