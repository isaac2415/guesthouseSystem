import java.awt.*;
import java.io.*;
import javax.swing.*;

public class Register {

    private static final String FILE_NAME = "credentials.txt";

    public static void showRegisterUI() {
        JLabel headlabel = new JLabel("REGISTER ADMIN");
        headlabel.setBounds(5, 5, 350, 40);
        headlabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel usernamelabel = new JLabel("Set Username");
        usernamelabel.setBounds(5, 70, 360, 20);
        usernamelabel.setFont(new Font("Arial", Font.BOLD, 24));

        JTextField username = new JTextField();
        username.setBounds(5, 100, 370, 40);

        JLabel passwordlabel = new JLabel("Set Password");
        passwordlabel.setBounds(5, 170, 360, 20);
        passwordlabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPasswordField password = new JPasswordField();
        password.setBounds(5, 200, 370, 40);

        JButton registerButton = new JButton("REGISTER");
        registerButton.setBounds(5, 270, 370, 40);
        registerButton.setBackground(new Color(0,123,255));
        registerButton.setForeground(Color.WHITE);

        JFrame frame = new JFrame("ADMIN REGISTRATION");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.add(headlabel);
        frame.add(usernamelabel);
        frame.add(username);
        frame.add(passwordlabel);
        frame.add(password);
        frame.add(registerButton);
        frame.setResizable(false);
        frame.setVisible(true);

        registerButton.addActionListener(_ -> {
            String user = username.getText().trim();
            String pass = new String(password.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(frame, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write(user + "," + pass);
                writer.flush();
                JOptionPane.showMessageDialog(frame, "Admin registered successfully!");
                frame.dispose();
                Main.showLoginUI();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error saving credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
