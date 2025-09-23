package translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GUI {

    private static JComboBox<String> countryComboBox;
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
        // Country Selection Panel
        JPanel countryPanel = new JPanel();
        countryPanel.add(new JLabel("Country:"));

        countryComboBox = new JComboBox<>();

        // Populate with all countries that have translations
        for(String countryCode : translator.getCountryCodes()) {
            String countryName = countryConverter.fromCountryCode(countryCode);
            if (countryName != null) {
                countryComboBox.addItem(countryName);
            }
        }
        countryPanel.add(countryComboBox);

        // Language Selection Panel
        JPanel languagePanel = new JPanel();
        languagePanel.add(new JLabel("Language:"));

        languageComboBox = new JComboBox<>();
        languagePanel.add(languageComboBox);

        // Result Panel
        JPanel resultPanel = new JPanel();
        resultLabel = new JLabel("Translation will appear here");
        resultPanel.add(resultLabel);

        // Add ActionListener for country selection
        countryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLanguageComboBoxBasedOnCountry();
                updateTranslation();
            }
        });

        // Add ActionListener for language selection
        languageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTranslation();
            }
        });

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(countryPanel);
        mainPanel.add(languagePanel);
        mainPanel.add(resultPanel);

        JFrame frame = new JFrame("Country Name Translator");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initialize language combo box with first country's languages
        if (countryComboBox.getItemCount() > 0) {
            updateLanguageComboBoxBasedOnCountry();
            updateTranslation();
        }
    }

    private static void updateLanguageComboBoxBasedOnCountry() {
        // Get selected country
        String countryName = (String) countryComboBox.getSelectedItem();
        if (countryName == null) return;

        String countryCode = countryConverter.fromCountry(countryName);

        // Clear current languages
        languageComboBox.removeAllItems();

        // Get all possible language codes
        java.util.List<String> allLanguageCodes = translator.getLanguageCodes();

        // Filter languages that have translations for this country
        for (String languageCode : allLanguageCodes) {
            // Check if translation exists for this country-language combination
            String translation = translator.translate(countryCode, languageCode);
            if (translation != null && !translation.isEmpty()) {
                // Add the language to the combo box
                String languageName = languageConverter.fromLanguageCode(languageCode);
                if (languageName != null) {
                    languageComboBox.addItem(languageName);
                } else {
                    languageComboBox.addItem(languageCode);
                }
            }
        }

        // If no languages found, add a message
        if (languageComboBox.getItemCount() == 0) {
            languageComboBox.addItem("No translations available");
        }
    }

    private static void updateTranslation() {
        String countryName = (String) countryComboBox.getSelectedItem();
        String languageName = (String) languageComboBox.getSelectedItem();

        if (countryName == null || languageName == null) return;

        String countryCode = countryConverter.fromCountry(countryName);
        String languageCode = languageConverter.fromLanguage(languageName);

        String result = translator.translate(countryCode, languageCode);
        if (result == null) {
            result = "No translation found!";
        }

        resultLabel.setText(countryName + " in " + languageName + ": " + result);
    }
}