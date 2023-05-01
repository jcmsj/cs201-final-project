package Block;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import util.IconButton;
import javax.swing.Box;

public class Editor extends JPanel implements ActionListener {
	JPanel blocksPanel;
	JButton addButton;
	JButton removeButton;
	JButton submitButton;
	LinkedList<InputBlock> textFields;
	Consumer<int[]> consumer;

	public enum ACTIONS {
		ADD,
		REM,
		PLAY,
		CLEAR
	}

	public Editor() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		addButton = new IconButton("../assets/plus.png");
		addButton.setActionCommand(ACTIONS.ADD.toString());
		addButton.setToolTipText("Add a block");
		removeButton = new IconButton("../assets/minus.png");
		removeButton.setActionCommand(ACTIONS.REM.toString());
		removeButton.setToolTipText("Remove last block");
		submitButton = new IconButton("../assets/play.png");
		submitButton.setActionCommand(ACTIONS.PLAY.toString());
		submitButton.setToolTipText("Start sorting!");
		textFields = new LinkedList<InputBlock>();
		// set up the panel, arrange blocks horizontally
		blocksPanel = new JPanel();
		var flow = new FlowLayout();
		flow.setHgap(30);
		blocksPanel.setLayout(flow);
		blocksPanel.setOpaque(false);
		add(blocksPanel);
		// add the buttons
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		buttonPanel.add(removeButton);
		buttonPanel.add(addButton);
		buttonPanel.add(submitButton);
		add(Box.createVerticalStrut(Row.X_GAP));
		add(buttonPanel);
		EventQueue.invokeLater(() -> {
			buttonPanel.setMaximumSize(buttonPanel.getSize());
		});
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		submitButton.addActionListener(this);
		// By default add 2 boxes
		addField();
		addField();
	}

	// add a new JTextField to the panel
	public void addField() {
		InputBlock input = new InputBlock();
		blocksPanel.add(input);
		textFields.add(input);
	}

	public void actionPerformed(ActionEvent e) {
		doAction(ACTIONS.valueOf(e.getActionCommand()));
	}
	public void doAction(ACTIONS act) {
		switch (act) {
			case ADD:
				addField();
				break;
			case PLAY:
				consumer.accept(makeInts());
				break;
			case REM:
			// remove the last JTextField from the panel
				if (textFields.size() > 0) {
					blocksPanel.remove(textFields.pollLast());
				}
				break;
			default:
				break;
		}
		revalidate();
		repaint();
	}

	public int[] makeInts() {
		int[] ints = new int[textFields.size()];
		for (int i = 0; i < textFields.size(); i++) {
			String string = textFields.get(i).input.getText();
			ints[i] = Integer.parseInt(string);
		}
		return ints;
	}

	public void setConsumer(Consumer<int[]> consumer) {
		this.consumer = consumer;
	}
}
