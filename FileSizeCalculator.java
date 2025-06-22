import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.StringTokenizer;

public class FileSizeCalculator extends JFrame {
    private JTextArea textArea;
    private JLabel fileSizeLabel, wordCountLabel, charCountLabel, longestWordLabel, avgWordLengthLabel;
    private JButton uploadButton;

    public FileSizeCalculator() {
        setTitle("File Size Calculator for Documents");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(6, 1));
        fileSizeLabel = new JLabel("File Size: ");
        wordCountLabel = new JLabel("Word Count: ");
        charCountLabel = new JLabel("Character Count: ");
        longestWordLabel = new JLabel("Longest Word: ");
        avgWordLengthLabel = new JLabel("Average Word Length: ");
        infoPanel.add(fileSizeLabel);
        infoPanel.add(wordCountLabel);
        infoPanel.add(charCountLabel);
        infoPanel.add(longestWordLabel);
        infoPanel.add(avgWordLengthLabel);

        add(infoPanel, BorderLayout.SOUTH);

        uploadButton = new JButton("Upload File");
        add(uploadButton, BorderLayout.NORTH);

        uploadButton.addActionListener(e -> chooseFile());

        setVisible(true);
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            displayFileInfo(file);
        }
    }

    private void displayFileInfo(File file) {
        try {
            long fileSizeBytes = Files.size(file.toPath());
            double fileSizeKB = fileSizeBytes / 1024.0;
            double fileSizeMB = fileSizeKB / 1024.0;
            fileSizeLabel.setText(String.format("File Size: %.2f KB (%.2f MB)", fileSizeKB, fileSizeMB));

            if (file.getName().endsWith(".txt")) {
                String content = new String(Files.readAllBytes(file.toPath()));
                textArea.setText(content);

                int wordCount = 0, charCount = content.length();
                String longestWord = "";
                int totalWordLength = 0;

                StringTokenizer tokenizer = new StringTokenizer(content);
                while (tokenizer.hasMoreTokens()) {
                    String word = tokenizer.nextToken();
                    wordCount++;
                    totalWordLength += word.length();
                    if (word.length() > longestWord.length()) {
                        longestWord = word;
                    }
                }

                wordCountLabel.setText("Word Count: " + wordCount);
                charCountLabel.setText("Character Count: " + charCount);
                longestWordLabel.setText("Longest Word: " + longestWord);
                avgWordLengthLabel.setText("Average Word Length: " +
                        (wordCount > 0 ? (totalWordLength / wordCount) : 0));
            } else {
                textArea.setText("Preview not supported for this file type.");
                wordCountLabel.setText("Word Count: -");
                charCountLabel.setText("Character Count: -");
                longestWordLabel.setText("Longest Word: -");
                avgWordLengthLabel.setText("Average Word Length: -");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSizeCalculator::new);
    }
}
