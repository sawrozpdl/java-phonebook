package phonebook;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablePane extends JScrollPane {
	
	private JTable table;
	private DefaultTableModel model;
	private boolean changed; // if the table has changed like user adds or remove stuffs, it sets this to true
	
	public TablePane() {
		table = new JTable();
		model = new DefaultTableModel();
		table.setModel(model);
		table.getTableHeader().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent evt) {
		        sort(table.columnAtPoint(evt.getPoint()));  //for sorting the data into ascending order
		    }
		});
		changed = false;
		setViewportView(table);
	}
	
	public TablePane(String[] columns) {
		this();
		model.setColumnIdentifiers(columns);
	}

	public void setBorder(String title) {
		setBorder(BorderFactory.createTitledBorder(title));
	}
	
	public void addRow(String[] data) {
		model.addRow(data);
		changed = true;
	}
	
	public void removeRow(int index) {
		model.removeRow(index);
		changed = true;
	}
	
	public void insertRow(int index, String[] data) {
		model.insertRow(index, data);
		changed = true;
	}
	
	public void selectRow(int index) {
		table.setRowSelectionInterval(index, index);
	}
	
	public void removeSelectedRow() {
		model.removeRow(table.getSelectedRow());
		changed = true;
	}

	public int getRowCount() {
		return model.getRowCount();
	}
	
	public String getValueAt(int i1, int i2) {
		return (String)model.getValueAt(i1, i2);
	}
	
	public boolean hasChanged() {
		return changed;
	}
	
	public int populateFrom(File file) throws FileNotFoundException { // read data from the file 'file'
		model.setRowCount(0);
		Scanner fileS = new Scanner(file);
		int firstLast = -1;
		if (fileS.hasNext()) 
			firstLast = fileS.nextLine().equals("true") ? 1 : 0;
		while (fileS.hasNext()) 
			model.addRow(new String[] {fileS.nextLine(), fileS.nextLine(),fileS.nextLine(),fileS.nextLine()});
		fileS.close();
		return firstLast;
	}
	
	public void populateTo(File file, boolean firstLast) throws IOException { //populate data into the file 'file' from the table 
		BufferedWriter write = new BufferedWriter(new FileWriter(file, true));
		PrintWriter writer = new PrintWriter(file);
		writer.print(""); // reset data from the file
		writer.close();
		write.append(firstLast + System.getProperty("line.separator"));
		for (int i = 0; i < model.getRowCount() && model.getValueAt(i, 0) != null; i++) {
			write.append(model.getValueAt(i, 0) + System.getProperty("line.separator"));
			write.append(model.getValueAt(i, 1) + System.getProperty("line.separator"));
			write.append(model.getValueAt(i, 2) + System.getProperty("line.separator"));
			write.append(model.getValueAt(i, 3) + System.getProperty("line.separator"));
		}
		write.close();
		changed = false;
	}
	
	private void swap(int from, int to) { // for swapping two columns from the table
		Object[] temp = {
				model.getValueAt(to, 0),
				model.getValueAt(to, 1),
				model.getValueAt(to, 2),
				model.getValueAt(to, 3)
		};
		model.setValueAt(model.getValueAt(from, 0), to, 0);
		model.setValueAt(model.getValueAt(from, 1), to, 1);
		model.setValueAt(model.getValueAt(from, 2), to, 2);
		model.setValueAt(model.getValueAt(from, 3), to, 3);
		model.setValueAt(temp[0], from, 0);
		model.setValueAt(temp[1], from, 1);
		model.setValueAt(temp[2], from, 2);
		model.setValueAt(temp[3], from, 3);
	}
	
	public void sort(int ci) { // ci is column index
		boolean sorted = false;
		while (!sorted) {
			sorted = true;
			for (int i = 0; i < model.getRowCount() - 1;i++) {
				int foo = checkOrder(getValueAt(i, ci),
							getValueAt(i + 1, ci));
				int val = 0;
				while (val < model.getColumnCount() && foo == 0) {
					if (val == ci) {
						val++;
						continue;
					}
					foo = checkOrder(getValueAt(i, val), //checking order of other columns if 'ci' items are same
							getValueAt(i + 1, val));
					val++;
				}
					
				if (foo == -1) {
					swap(i + 1, i);
					sorted = false;
				}
			}
		}
		changed = true;
	}
	
    private static String space(int num) { // used in order checking for making two strings same length
        String str = "";
        for (int i = 0;i < num;i++) 
            str = str + " ";
        return str;
    }
	
	private static int checkOrder(String first, String second) { //  returns true if two strings are in ascending order
		if (first.equals("") && second.equals("")) return 0;
        if (first.length() < second.length()) 
        	first = first + space(second.length() - first.length());
        else 
        	second = second + space(first.length() - second.length());
        int differentLetterAt = 0; // supposing the letters at first are different
        for (int i = 0;i < first.length();i++) {
        	if (first.charAt(i) != second.charAt(i)) {
        		differentLetterAt = i; // i th index is the index where letters are different
                break;
            }
        }
        return (first.charAt(differentLetterAt) < second.charAt(differentLetterAt)) ? 1 :
        	(first.charAt(differentLetterAt) > second.charAt(differentLetterAt)) ? -1 : 0;
    }

	public void clear() { // clearing the whole table
		while(model.getRowCount() != 0)
			model.removeRow(0);
	}
}
