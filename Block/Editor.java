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
import javax.swing.border.Border;
import Style.Style;
import util.IconButton;
import javax.swing.BorderFactory;
import javax.swing.Box;

public class Editor extends JPanel implements ActionListener {
	JPanel blocksPanel;
	JButton adder;
	JButton remover;
	JButton submitter;
	LinkedList<InputBlock> textFields;
	int MIN_TEXT_FIELDS = 2;
	Consumer<int[]> consumer;

	public enum ACTIONS {
		ADD,
		REM,
		PLAY,
		CLEAR
	}

	class PButton extends IconButton {
		final static Border focused = BorderFactory.createLineBorder(Style.yellowish);
		public PButton(String path, ACTIONS a, String toolTip) {
			super(path);
			setFocusedBorder(focused);
			setActionCommand(a.toString());
			setToolTipText(toolTip);
		}
	}

	class BlocksPanel extends JPanel {
		public BlocksPanel() {
			var flow = new FlowLayout();
			flow.setHgap(30);
			setLayout(flow);
			setOpaque(false);
		}
	}
	public Editor() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		adder = new PButton("../assets/plus.png", ACTIONS.ADD, "Add a block");
		remover = new PButton("../assets/minus.png", ACTIONS.REM, "Remove last block");
		submitter = new PButton("../assets/play.png", ACTIONS.PLAY, "Start sorting!");
		textFields = new LinkedList<InputBlock>();
		// set up the panel, arrange blocks horizontally
		blocksPanel = new BlocksPanel();
		add(blocksPanel);
		// add the buttons
		JPanel btnGroup = new JPanel(new GridLayout(1, 3));
		btnGroup.add(remover);
		btnGroup.add(adder);
		btnGroup.add(submitter);
		add(Box.createVerticalStrut(Row.X_GAP));
		add(btnGroup);
		//Pprevent buttonPanels from resizing
		EventQueue.invokeLater(() -> {
			btnGroup.setMaximumSize(btnGroup.getSize());
			btnGroup.setMinimumSize(btnGroup.getSize());
			btnGroup.setPreferredSize(btnGroup.getSize());
		});
		adder.addActionListener(this);
		remover.addActionListener(this);
		submitter.addActionListener(this);
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
				if (textFields.size() > MIN_TEXT_FIELDS) {
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
