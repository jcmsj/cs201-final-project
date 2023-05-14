package Block;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import Style.Style;
import util.IconButton;
import util.KPanel;

import javax.swing.BorderFactory;
import javax.swing.Box;

public class Editor extends JPanel implements ActionListener {
	JPanel blocksPanel;
	JButton adder;
	JButton remover;
	public JButton submitter;
	public LinkedList<InputBlock> textFields;
	private JPanel removerRow;
	public PButton resetter;
	int MIN_TEXT_FIELDS = 2;
	Consumer<int[]> consumer;

	public static class PButton extends IconButton {
		final static Border focused = BorderFactory.createLineBorder(Style.yellowish);

		public PButton(String path, ACTIONS a, String toolTip) {
			super(path);
			setFocusedBorder(focused);
			setActionCommand(a.toString());
			setToolTipText(toolTip);
		}
	}

	static class BlocksPanel extends JPanel {
		public BlocksPanel() {
			setOpaque(false);
			var flow = new FlowLayout();
			flow.setHgap(30);
			setLayout(flow);
			setOpaque(false);
		}
	}

	private int xModifier = 3;

	public void moveRemover(InputBlock b) {
		int mid = Math.round((b.getWidth() - remover.getWidth()) / 2f);
		remover.setLocation(b.getX() + mid + xModifier, remover.getY());
		repaint();
		revalidate();
	}

	public Editor() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		setBorder(Row.DEFAULT_BORDER);
		adder = new GreenBlock();
		adder.setActionCommand(ACTIONS.ADD.toString());
		adder.setToolTipText("Add a block");
		remover = new PButton("assets/x.png", ACTIONS.REM, "Remove above block");
		remover.setBounds(0, 0, 50, 50);
		submitter = new PButton("assets/play.png", ACTIONS.PLAY, "Start sorting!");
		resetter = new PButton("assets/reset.png", ACTIONS.RESET, "Clear blocks");
		textFields = new LinkedList<InputBlock>();
		// set up the panel, arrange blocks horizontally
		var inner = new JPanel();
		inner.setOpaque(false);
		blocksPanel = new BlocksPanel();
		inner.add(blocksPanel);
		inner.add(adder);
		add(inner);

		{ // Put remover into its own row where it'll be able to move freely
			removerRow = new JPanel(null);
			removerRow.setPreferredSize(new Dimension(1, 100));
			removerRow.add(remover);
			removerRow.setOpaque(false);

			add(removerRow);
		}
		KPanel btnGroup = new KPanel();
		btnGroup.setLayout(new GridLayout(1, 3));
		btnGroup.add(resetter);
		btnGroup.add(submitter);
		add(Box.createVerticalStrut(Row.X_GAP));
		add(btnGroup);
		// Prevent buttonPanels from resizing
		btnGroup.preventResize();
		adder.addActionListener(this);
		remover.addActionListener(this);
		submitter.addActionListener(this);
		resetter.addActionListener(this);
		// By default add 2 boxes
		addField().edit(); // And focus first block
		addField();
	}

	// add a new JTextField to the panel
	InputBlock deletable;

	public InputBlock addField() {
		InputBlock block = new InputBlock();
		block.input.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				deletable = block;
				moveRemover(block);
			}

			@Override
			public void focusLost(FocusEvent e) {
				// Pass
			}

		});
		blocksPanel.add(block);
		textFields.offer(block);
		return block;
	}

	public void actionPerformed(ActionEvent e) {
		doAction(ACTIONS.valueOf(e.getActionCommand()));
	}

	public void resetFields() {
		// Clear all references of old objects
		blocksPanel.removeAll();
		textFields.clear();
		deletable = null;
		addField().edit();
		addField();
	}

	public void doAction(ACTIONS act) {
		switch (act) {
			case ADD:
				addField().edit();
				break;
			case PLAY:
				consumer.accept(makeInts());
				break;
			case REM:
				removeSelected();
				break;
			case RESET:
				resetFields();
				break;
			default:
				break;
		}
		revalidate();
		repaint();
	}

	public void removeSelected() {
		if (deletable == null || textFields.size() <= MIN_TEXT_FIELDS) {
			return;
		}
		InputBlock nextDeletable;
		// select the last child
		if (textFields.peekLast() == deletable) {
			textFields.removeLast();
			nextDeletable = textFields.peekLast();
		// select the first child
		} else if (textFields.peekFirst() == deletable) {
			textFields.removeFirst();
			nextDeletable = textFields.peekFirst();
		// select the next sibling
		} else {
			nextDeletable = textFields.get(textFields.indexOf(deletable) + 1);
			textFields.remove(deletable);
		}
		blocksPanel.remove(deletable);
		nextDeletable.edit();
	}

	public int[] makeInts() {
		int[] ints = new int[textFields.size()];
		for (int i = 0; i < textFields.size(); i++) {
			String string = textFields.get(i).input.getText().trim();
			try {
				ints[i] = Integer.parseInt(string);
			} catch (NumberFormatException e) {
				showErrorThenFocus(string.length() == 0, i);
				throw e;
			}
		}
		return ints;
	}

	public void showErrorThenFocus(boolean isEmpty, int errorIndex) {
		final String msg = isEmpty ? "A block can't be empty!": "Please input only integer values in the block!";
		JOptionPane.showMessageDialog(this, msg, "Invalid Input", JOptionPane.ERROR_MESSAGE);
		textFields.get(errorIndex).edit();
	}
	public void setConsumer(Consumer<int[]> consumer) {
		this.consumer = consumer;
	}
}
