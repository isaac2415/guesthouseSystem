import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Main {

    private static final String FILE_NAME = "credentials.txt";

    public static void main(String[] args) {
        File file = new File(FILE_NAME);
        if (!file.exists() || file.length() == 0) {
            Register.showRegisterUI();
        } else {
            showLoginUI();
        }
    }

    public static void showLoginUI() {
        JLabel headlabel = new JLabel("LOGIN");
        headlabel.setBounds(5, 5, 350, 40);
        headlabel.setFont(new Font("Arial", Font.BOLD, 35));

        JLabel usernamelabel = new JLabel("Username");
        usernamelabel.setBounds(5, 70, 360, 20);
        usernamelabel.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField username = new JTextField();
        username.setBounds(5, 100, 370, 40);

        JLabel passwordlabel = new JLabel("Password");
        passwordlabel.setBounds(5, 170, 360, 20);
        passwordlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPasswordField password = new JPasswordField();
        password.setBounds(5, 200, 370, 40);

        JButton button = new JButton("LOGIN");
        button.setBounds(5, 270, 370, 40);
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);

        JFrame frame = new JFrame("LOGIN");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.add(headlabel);
        frame.add(usernamelabel);
        frame.add(username);
        frame.add(passwordlabel);
        frame.add(password);
        frame.add(button);
        frame.setResizable(false);
        frame.setVisible(true);

        button.addActionListener(_ -> {
            String user = username.getText();
            String pass = new String(password.getPassword());

            if (authenticate(user, pass)) {
                frame.dispose();
                Home.display();
            } else {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static boolean authenticate(String user, String pass) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                return parts[0].equals(user) && parts[1].equals(pass);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
