import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Book {
    int id;
    String title, author;
    boolean isIssued = false;

    Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public String toString() {
        return id + ": " + title + " by " + author + (isIssued ? " [Issued]" : " [Available]");
    }
}

public class DigitalLibraryGUI {
    static ArrayList<Book> books = new ArrayList<>();
    static int bookIdCounter = 1;
    static JFrame frame;
    static JTextArea outputArea;

    public static void main(String[] args) {
        initializeBooks();
        loginScreen();
    }

    static void initializeBooks() {
        books.add(new Book(bookIdCounter++, "The Alchemist", "Paulo Coelho"));
        books.add(new Book(bookIdCounter++, "1984", "George Orwell"));
        books.add(new Book(bookIdCounter++, "To Kill a Mockingbird", "Harper Lee"));
    }

    static void loginScreen() {
        frame = new JFrame("Digital Library Login");
        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            frame.dispose();

            if (user.equals("admin") && pass.equals("admin123")) {
                showAdminUI();
            } else {
                showUserUI();
            }
        });

        frame.setLayout(new GridLayout(3, 2));
        frame.add(userLabel); frame.add(userField);
        frame.add(passLabel); frame.add(passField);
        frame.add(new JLabel()); frame.add(loginBtn);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void showAdminUI() {
        frame = new JFrame("Admin Panel");
        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);

        JTextField titleField = new JTextField(10);
        JTextField authorField = new JTextField(10);
        JTextField deleteIdField = new JTextField(5);

        JButton addBook = new JButton("Add Book");
        JButton deleteBook = new JButton("Delete Book");
        JButton viewBooks = new JButton("View Books");
        JButton logout = new JButton("Logout");

        addBook.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            if (!title.isEmpty() && !author.isEmpty()) {
                books.add(new Book(bookIdCounter++, title, author));
                showMessage("Book added successfully!");
            }
        });

        deleteBook.addActionListener(e -> {
            try {
                int id = Integer.parseInt(deleteIdField.getText());
                books.removeIf(book -> book.id == id);
                showMessage("Book deleted (if exists).");
            } catch (NumberFormatException ex) {
                showMessage("Enter a valid book ID.");
            }
        });

        viewBooks.addActionListener(e -> {
            outputArea.setText("Library Books:\n");
            for (Book b : books) {
                outputArea.append(b.toString() + "\n");
            }
        });

        logout.addActionListener(e -> {
            frame.dispose();
            loginScreen();
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Title:")); panel.add(titleField);
        panel.add(new JLabel("Author:")); panel.add(authorField);
        panel.add(addBook);
        panel.add(new JLabel("Delete Book ID:")); panel.add(deleteIdField);
        panel.add(deleteBook);
        panel.add(viewBooks);
        panel.add(logout);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void showUserUI() {
        frame = new JFrame("User Panel");
        outputArea = new JTextArea(15, 40);
        outputArea.setEditable(false);

        JTextField searchField = new JTextField(10);
        JTextField issueField = new JTextField(5);
        JTextField returnField = new JTextField(5);

        JButton viewBooks = new JButton("View Books");
        JButton searchBook = new JButton("Search");
        JButton issueBook = new JButton("Issue");
        JButton returnBook = new JButton("Return");
        JButton email = new JButton("Email Query");
        JButton exit = new JButton("Exit");

        viewBooks.addActionListener(e -> {
            outputArea.setText("Library Books:\n");
            for (Book b : books) {
                outputArea.append(b.toString() + "\n");
            }
        });

        searchBook.addActionListener(e -> {
            String keyword = searchField.getText().toLowerCase();
            outputArea.setText("Search Results:\n");
            for (Book b : books) {
                if (b.title.toLowerCase().contains(keyword)) {
                    outputArea.append(b.toString() + "\n");
                }
            }
        });

        issueBook.addActionListener(e -> {
            try {
                int id = Integer.parseInt(issueField.getText());
                for (Book b : books) {
                    if (b.id == id && !b.isIssued) {
                        b.isIssued = true;
                        showMessage("Book issued.");
                        return;
                    }
                }
                showMessage("Book not available.");
            } catch (Exception ex) {
                showMessage("Invalid book ID.");
            }
        });

        returnBook.addActionListener(e -> {
            try {
                int id = Integer.parseInt(returnField.getText());
                for (Book b : books) {
                    if (b.id == id && b.isIssued) {
                        b.isIssued = false;
                        showMessage("Book returned.");
                        return;
                    }
                }
                showMessage("Invalid return.");
            } catch (Exception ex) {
                showMessage("Invalid ID.");
            }
        });

        email.addActionListener(e -> showMessage("Email sent to admin@library.com"));
        exit.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel();
        panel.add(viewBooks);
        panel.add(new JLabel("Search:")); panel.add(searchField); panel.add(searchBook);
        panel.add(new JLabel("Issue ID:")); panel.add(issueField); panel.add(issueBook);
        panel.add(new JLabel("Return ID:")); panel.add(returnField); panel.add(returnBook);
        panel.add(email); panel.add(exit);

        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    static void showMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }
}