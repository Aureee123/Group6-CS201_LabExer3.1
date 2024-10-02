import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

public class LibraryManagementSystemGUI extends JFrame {
    private ArrayList<String> books = new ArrayList<>();
    private Stack<String> undoStack = new Stack<>();

    private JTextArea bookDisplayArea;
    private JTextField bookInputField;
    private JComboBox<String> sortMethodComboBox;

    public LibraryManagementSystemGUI() {
        setTitle("Library Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new FlowLayout());

        bookInputField = new JTextField(20);
        inputPanel.add(new JLabel("Enter Book Title:"));
        inputPanel.add(bookInputField);

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());
        inputPanel.add(addButton);

        JButton removeButton = new JButton("Remove Book");
        removeButton.addActionListener(e -> removeBook());
        inputPanel.add(removeButton);

        String[] sortMethods = {"Heap Sort", "Bubble Sort", "Insertion Sort", "Selection Sort"};
        sortMethodComboBox = new JComboBox<>(sortMethods);
        JButton sortButton = new JButton("Sort Books");
        sortButton.addActionListener(e -> sortBooks());
        inputPanel.add(new JLabel("Sort by:"));
        inputPanel.add(sortMethodComboBox);
        inputPanel.add(sortButton);

        JButton undoButton = new JButton("Undo Last Action");
        undoButton.addActionListener(e -> undoLastOperation());
        inputPanel.add(undoButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        bookDisplayArea = new JTextArea();
        bookDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookDisplayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);
        updateBookDisplay();
    }

    private void addBook() {
        String book = bookInputField.getText().trim();
        if (!book.isEmpty()) {
            books.add(book);
            undoStack.push("ADD " + book);
            updateBookDisplay();
            bookInputField.setText("");
            JOptionPane.showMessageDialog(this, "Book added: " + book);
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a book title.");
        }
    }

    private void removeBook() {
        String book = bookInputField.getText().trim();
        if (books.contains(book)) {
            books.remove(book);
            undoStack.push("REMOVE " + book);
            updateBookDisplay();
            bookInputField.setText("");
            JOptionPane.showMessageDialog(this, "Book removed: " + book);
        } else {
            JOptionPane.showMessageDialog(this, "Book not found.");
        }
    }

    private void updateBookDisplay() {
        if (books.isEmpty()) {
            bookDisplayArea.setText("No books in the library.");
        } else {
            bookDisplayArea.setText("Books in the library:\n");
            for (String book : books) {
                bookDisplayArea.append(book + "\n");
            }
        }
    }

    private void sortBooks() {
        String selectedSortMethod = (String) sortMethodComboBox.getSelectedItem();
        if (selectedSortMethod != null) {
            switch (selectedSortMethod) {
                case "Heap Sort":
                    heapSort(books);
                    break;
                case "Bubble Sort":
                    bubbleSort(books);
                    break;
                case "Insertion Sort":
                    insertionSort(books);
                    break;
                case "Selection Sort":
                    selectionSort(books);
                    break;
            }
            updateBookDisplay();
            JOptionPane.showMessageDialog(this, "Books sorted using " + selectedSortMethod + ".");
        }
    }

    private void heapSort(ArrayList<String> books) {
        int n = books.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(books, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            String temp = books.get(0);
            books.set(0, books.get(i));
            books.set(i, temp);
            heapify(books, i, 0);
        }
    }

    private void heapify(ArrayList<String> books, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && books.get(left).compareTo(books.get(largest)) > 0) largest = left;
        if (right < n && books.get(right).compareTo(books.get(largest)) > 0) largest = right;
        if (largest != i) {
            String temp = books.get(i);
            books.set(i, books.get(largest));
            books.set(largest, temp);
            heapify(books, n, largest);
        }
    }

    private void bubbleSort(ArrayList<String> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (books.get(j).compareTo(books.get(j + 1)) > 0) {
                    String temp = books.get(j);
                    books.set(j, books.get(j + 1));
                    books.set(j + 1, temp);
                }
            }
        }
    }

    private void insertionSort(ArrayList<String> books) {
        int n = books.size();
        for (int i = 1; i < n; i++) {
            String key = books.get(i);
            int j = i - 1;
            while (j >= 0 && books.get(j).compareTo(key) > 0) {
                books.set(j + 1, books.get(j));
                j = j - 1;
            }
            books.set(j + 1, key);
        }
    }

    private void selectionSort(ArrayList<String> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).compareTo(books.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            String temp = books.get(minIndex);
            books.set(minIndex, books.get(i));
            books.set(i, temp);
        }
    }

    private void undoLastOperation() {
        if (!undoStack.isEmpty()) {
            String lastOperation = undoStack.pop();
            String[] parts = lastOperation.split(" ", 2);
            String action = parts[0];
            String book = parts[1];

            if (action.equals("ADD")) {
                books.remove(book);
                updateBookDisplay();
                JOptionPane.showMessageDialog(this, "Undone adding book: " + book);
            } else if (action.equals("REMOVE")) {
                books.add(book);
                updateBookDisplay();
                JOptionPane.showMessageDialog(this, "Undone removing book: " + book);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No operations to undo.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystemGUI gui = new LibraryManagementSystemGUI();
            gui.setVisible(true);
        });
    }
}
