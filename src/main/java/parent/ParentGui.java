package parent;

import javax.swing.*;
import java.awt.*;

public class ParentGui extends JFrame {

    private final ParentAgent parentAgent;

    public ParentGui(final ParentAgent parentAgent) {
        this.parentAgent = parentAgent;
        this.setLayout(new BorderLayout(10, 10));

        JButton pastaButton = new JButton();
        JButton appleButton = new JButton();
        JButton pizzaButton = new JButton();
        appleButton.setIcon(new ImageIcon("apple.jpg"));
        pizzaButton.setIcon(new ImageIcon("pizza.jpg"));
        pastaButton.setIcon(new ImageIcon("pasta.jpg"));
        pastaButton.addActionListener(e -> parentAgent.feed(1));
        appleButton.addActionListener(e -> parentAgent.feed(2));
        pizzaButton.addActionListener(e -> parentAgent.feed(3));
        add(pastaButton, BorderLayout.WEST);
        add(appleButton, BorderLayout.CENTER);
        add(pizzaButton, BorderLayout.EAST);

        JButton sleepButton = new JButton();
        sleepButton.setIcon(new ImageIcon("sov.png"));
        sleepButton.addActionListener(e -> parentAgent.putToBed());
        add(sleepButton, BorderLayout.SOUTH);

        JButton wakeButton = new JButton();
        wakeButton.addActionListener(e -> parentAgent.wake());
        add(wakeButton, BorderLayout.NORTH);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
    }

    public ParentGui() {
        this(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ParentGui::new);
    }
}
