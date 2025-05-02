import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class ViewBookingHistoryApp {

    private JFrame frame;
    private JTable bookingHistoryTable;
    private JScrollPane scrollPane;
    private JTextField searchField;
    private DefaultTableModel model;

    public ViewBookingHistoryApp() {
        initialize();
    }

    public void showWindow() {
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Booking History");
        frame.setBounds(100, 100, 1200, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);
        

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Booking History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Add Print to PDF button
        JButton printPdfButton = new JButton("Print to PDF");
        printPdfButton.setFont(new Font("Arial", Font.PLAIN, 14));
        printPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printTableToPDF();
            }
        });
        topPanel.add(printPdfButton, BorderLayout.EAST);


        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setToolTipText("Search by Guest Name");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });
        topPanel.add(searchField, BorderLayout.SOUTH);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        model = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Booking ID", "Guest Name", "Phone", "Room Number", "Room Type", "Start Time", "End Time", "Total Paid"}
        );

        bookingHistoryTable = new JTable(model);
        bookingHistoryTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookingHistoryTable.setRowHeight(25);
        bookingHistoryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JTableHeader header = bookingHistoryTable.getTableHeader();
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);

        displayBookingHistory();

        scrollPane = new JScrollPane(bookingHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private void displayBookingHistory() {
        try (Connection conn = DatabaseConnector.getConnection()) {
            String query = "SELECT b.booking_id, g.full_name, g.phone, r.room_number, r.room_type, b.start_time, b.end_time, b.total_amount_paid " +
                           "FROM Bookings b " +
                           "JOIN Guests g ON b.guest_id = g.guest_id " +
                           "JOIN Rooms r ON b.room_id = r.room_id";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int bookingId = rs.getInt("booking_id");
                    String guestName = rs.getString("full_name");
                    String phone = rs.getString("phone");
                    int roomNumber = rs.getInt("room_number");
                    String roomType = rs.getString("room_type");
                    String startTime = rs.getString("start_time");
                    String endTime = rs.getString("end_time");
                    double totalPaid = rs.getDouble("total_amount_paid");

                    model.addRow(new Object[] { bookingId, guestName, phone, roomNumber, roomType, startTime, endTime, totalPaid });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        bookingHistoryTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query, 1));
    }

    private void printTableToPDF() {
    // Create a file chooser to allow the user to choose the location and name of the PDF
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save PDF");
    fileChooser.setSelectedFile(new File("BookingHistory.pdf")); // default file name

    // Set the file filter for PDF files
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showSaveDialog(frame);

    // If the user clicks "Save"
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        String filePath = fileToSave.getAbsolutePath();

        // Ensure the file has the .pdf extension
        if (!filePath.endsWith(".pdf")) {
            filePath += ".pdf";
        }

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open(); 

            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(model.getColumnCount());
            pdfTable.setWidthPercentage(100);

            for (int i = 0; i < model.getColumnCount(); i++) {
                pdfTable.addCell(new com.itextpdf.text.Phrase(model.getColumnName(i)));
            }

            for (int rows = 0; rows < model.getRowCount(); rows++) {
                for (int cols = 0; cols < model.getColumnCount(); cols++) {
                    pdfTable.addCell(new com.itextpdf.text.Phrase(String.valueOf(model.getValueAt(rows, cols))));
                }
            }

            document.add(pdfTable);

            document.close();

            JOptionPane.showMessageDialog(frame, "PDF saved successfully to: " + filePath);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error while saving PDF: " + ex.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

    
}
