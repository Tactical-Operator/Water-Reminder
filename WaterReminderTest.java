import javax.swing.*;
import javax.sound.sampled.*;
import java.awt.*;

public class WaterReminderTest {

    private static final long INTERVAL = 10 * 1000;

    private static JFrame frame;
    private static Clip clip;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WaterReminderTest::createWindow);
        System.out.println("Water Reminder Started");

        while (true) {
            try {
                Thread.sleep(INTERVAL);

                SwingUtilities.invokeLater(() -> {
                    playSound();
                    frame.setVisible(true);
                    frame.toFront();
                    frame.requestFocus();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void createWindow() {
        frame = new JFrame("Water Break");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(
                WaterReminder.class.getResource("/background.jpg"));

        Image img = icon.getImage();

        int width = img.getWidth(null);
        int height = img.getHeight(null);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

        double scale = Math.min(
                (double) screen.width / width,
                (double) screen.height / height
        ) * 0.6;

        int finalW = (int) (width * scale);
        int finalH = (int) (height * scale);

        frame.setSize(finalW - 95, finalH - 100);

        frame.setResizable(false);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Consolas", Font.BOLD, 18));
        okButton.setBackground(Color.BLACK);
        okButton.setForeground(new Color(57, 255, 20));
        okButton.setFocusPainted(false);
        okButton.setBorder(
                BorderFactory.createLineBorder(
                        new Color(57, 255, 20), 1));
        okButton.setPreferredSize(new Dimension(100, 40));

        okButton.addActionListener(e -> {
            stopSound();
            frame.setVisible(false);
        });

        bottomPanel.add(okButton);

        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(backgroundPanel);
        frame.setVisible(false);
    }

    private static void playSound() {
        try {
            stopSound();

            AudioInputStream ais =
                    AudioSystem.getAudioInputStream(
                            WaterReminder.class.getResource("/audio.wav"));

            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopSound() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
}

class BackgroundPanel extends JPanel {

    private final Image image;

    public BackgroundPanel() {
        image = new ImageIcon(
                WaterReminder.class.getResource("/background.jpg"))
                .getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2.drawImage(
                image,
                0,
                0,
                getWidth(),
                getHeight(),
                this);

        g2.dispose();
    }
}