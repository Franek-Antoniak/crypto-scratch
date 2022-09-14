package frank.antoniak.visualblockchain.blockchain.messenger.person;

import frank.antoniak.visualblockchain.blockchain.cryptography.keypair.GenerateKeys;
import frank.antoniak.visualblockchain.blockchain.messenger.controller.MessengerController;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PersonFrame extends JFrame {
    private final MessengerController messengerController = MessengerController.getInstance();
    private final AtomicInteger id = new AtomicInteger(0);

    public PersonFrame() {
        super("Messenger");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);

        initComponents();

        setLayout(null); // sets absolute positioning of components
        setVisible(true);
    }

    private void initComponents() {
        JLabel nameLabel = new JLabel("Your Name");
        nameLabel.setBounds(130, 20, 100, 30);
        add(nameLabel);

        JTextField nameTextField = new JTextField();
        nameTextField.setBounds(210, 20, 120, 30);
        add(nameTextField);

        JLabel messageLabel = new JLabel("Message");
        messageLabel.setBounds(130, 60, 100, 30);
        add(messageLabel);

        JTextField nameTextField2 = new JTextField();
        nameTextField2.setBounds(210, 60, 120, 30);
        add(nameTextField2);

        JPanel greenPanel = new JPanel();
        greenPanel.setBounds(60, 150, 350, 70);
        greenPanel.setLayout(new BorderLayout());
        greenPanel.setBackground(Color.GREEN);
        add(greenPanel);

        JButton acceptButton = new JButton("Accept");
        acceptButton.setBounds(180, 100, 100, 30);
        add(acceptButton);

        JLabel helloLabel = new JLabel();
        helloLabel.setBounds(50, 20, 100, 30);
        helloLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helloLabel.setVerticalAlignment(SwingConstants.CENTER);

        Font font = new Font("Courier", Font.BOLD, 12);
        helloLabel.setFont(font);
        helloLabel.setFont(helloLabel.getFont()
                .deriveFont(16f));

        greenPanel.add(helloLabel);


        acceptButton.addActionListener(e -> {
            String name = nameTextField.getText();
            String message = nameTextField2.getText();
            if (name != null && name.trim()
                    .length() > 0 &&
                    message != null && message.trim()
                    .length() > 0) {
                Thread.currentThread()
                        .setName(name);
                GenerateKeys.generateKeysForUser();
                messengerController.sendNewMessage(message);
                helloLabel.setText("Message has been sent - " + id.incrementAndGet() + " - " + name);
            }
        });
    }
}
