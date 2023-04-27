package output;

import memory.Memory;

import javax.swing.*;

import java.awt.*;

import static java.lang.Thread.sleep;

public class Monitor extends JFrame implements Runnable {

    // associations
    private Memory memory;

    // GUI components
    private JScrollPane scrollPane = new JScrollPane();
    private JTextArea textArea = new JTextArea();

    public void associate(Memory memory) {
        this.memory = memory;
    }

    public Monitor() {
        setTitle("Monitor");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));
        scrollPane.setViewportView(textArea);
        add(scrollPane);
        setVisible(true);
    }

    @Override
    public void run() {
        while (true) {
            String str = memory.getOutputBuffer();
            if (str != null)
                textArea.append(str);
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
