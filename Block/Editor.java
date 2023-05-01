package Block;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import util.IconButton;

public class Editor extends JPanel implements ActionListener{
	    JPanel panel;
	    JButton addButton;
	    JButton removeButton;
	    JButton submitButton;
	    LinkedList<InputBlock> textFields;
	    Consumer<int[]> consumer;

	    public Editor() {
	        panel = new JPanel();
			panel.setOpaque(false);
	        addButton = new IconButton("../assets/m-add.png");
	        removeButton = new IconButton("../assets/m-remove.png");
	        submitButton = new IconButton("../assets/m-play.png");
	        textFields = new LinkedList<InputBlock>();
	        // set up the panel
	        panel.setLayout(new FlowLayout());
	        add(panel, BorderLayout.CENTER);
	        // add the buttons
	        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
	        buttonPanel.add(addButton);
	        buttonPanel.add(removeButton);
	        buttonPanel.add(submitButton);

	        add(buttonPanel, BorderLayout.SOUTH);
	        
	        addButton.addActionListener(this);
	        removeButton.addActionListener(this);
	        submitButton.addActionListener(this);
			addField();
			addField();
	    }
	    
		// add a new JTextField to the panel
		public void addField() {
			InputBlock input = new InputBlock();
			panel.add(input);
			textFields.add(input);
		}
	    public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == addButton) {
	           addField();
	        } else if (e.getSource() == removeButton && textFields.size() > 0) {
	            // remove the last JTextField from the panel
	            panel.remove(textFields.pollLast());
	        } else if (e.getSource() == submitButton) {
	            consumer.accept(makeInts());
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
