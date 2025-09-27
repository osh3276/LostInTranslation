package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class GUI {

    private static JList<String> countryList;
    private static JComboBox<String> languageComboBox;
    private static JSONTranslator translator;
    private static CountryCodeConverter countryConverter;
    private static LanguageCodeConverter languageConverter;
    private static JLabel resultLabel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize converters and translator
            countryConverter = new CountryCodeConverter();
            languageConverter = new LanguageCodeConverter();
            translator = new JSONTranslator();

            createGUI();
        });
    }

    private static void createGUI() {
        // Main frame with BorderLayout
        JFrame frame = new JFrame("Country Name Translator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Top Panel: Language selection and result (using your exact code structure)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Language Selection Panel (your code)
        JPanel languagePanel = new JPanel();
        languagePanel.add(new JLabel("Language:"));

        languageComboBox = new JComboBox<>();
        languagePanel.add(languageComboBox);
        topPanel.add(languagePanel);

        // Result Panel (your code)
        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel("Translation will appear here");
        resultPanel.add(resultLabel);
        topPanel.add(resultPanel);

        // Bottom Panel: Scrollable country list (JList version)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Select Country:"), BorderLayout.NORTH);

        // Create country list using your converter methods
        java.util.List<String> countryNames = new ArrayList<>();
        for(String countryCode : translator.getCountryCodes()) {
            String countryName = countryConverter.fromCountryCode(countryCode); // Your method name
            if (countryName != null) {
                countryNames.add(countryName);
            }
        }

        // Convert to array for JList
        String[] countryArray = countryNames.toArray(new String[0]);
        countryList = new JList<>(countryArray);
        countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Make it scrollable
        JScrollPane countryScrollPane = new JScrollPane(countryList);
        countryScrollPane.setPreferredSize(new Dimension(400, 150));
        bottomPanel.add(countryScrollPane, BorderLayout.CENTER);

        // Add panels to frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.CENTER);

        // Add listener for country list selection (adapted from your ActionListener)
        countryList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    updateLanguageComboBoxBasedOnCountry();
                    updateTranslation();
                }
            }
        });

        // Add listener for language selection (your exact code)
        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTranslation();
            }
        });

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Set initial selection (adapted from your initialization)
        if (countryList.getModel().getSize() > 0) {
            countryList.setSelectedIndex(0);
            updateLanguageComboBoxBasedOnCountry();
            updateTranslation();
        }
    }

    private static void updateLanguageComboBoxBasedOnCountry() {
        // Get selected country from JList (adapted from your method)
        String countryName = countryList.getSelectedValue();
        if (countryName == null) return;

        String countryCode = countryConverter.fromCountry(countryName); // Your method name

        // Clear current languages (your code)
        languageComboBox.removeAllItems();

        // Get all possible language codes (your code)
        java.util.List<String> allLanguageCodes = translator.getLanguageCodes();

        // Filter languages that have translations for this country (your code)
        for (String languageCode : allLanguageCodes) {
            String translation = translator.translate(countryCode, languageCode);
            if (translation != null && !translation.isEmpty()) {
                // Add the language to the combo box (your code with your method names)
                String languageName = languageConverter.fromLanguageCode(languageCode);
                if (languageName != null) {
                    languageComboBox.addItem(languageName);
                } else {
                    languageComboBox.addItem(languageCode);
                }
            }
        }

        // If no languages found, add a message (your code)
        if (languageComboBox.getItemCount() == 0) {
            languageComboBox.addItem("No translations available");
        }
    }

    private static void updateTranslation() {
        // Get selections (adapted from your method)
        String countryName = countryList.getSelectedValue();
        String languageName = (String) languageComboBox.getSelectedItem();

        if (countryName == null || languageName == null) return;

        // Use your converter methods
        String countryCode = countryConverter.fromCountry(countryName);
        String languageCode = languageConverter.fromLanguage(languageName);

        String result = translator.translate(countryCode, languageCode);
        if (result == null) {
            result = "No translation found!";
        }

        resultLabel.setText(countryName + " in " + languageName + ": " + result);
    }
}