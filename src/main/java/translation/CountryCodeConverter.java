package translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * This class provides the service of converting country codes to their names and back.
 */
public class CountryCodeConverter {

    private Map<String, String> countryCodeToCountry = new HashMap<>();
    private Map<String, String> countryToCountryCode = new HashMap<>();

    /**
     * Default constructor that loads the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor that allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resources file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> iterator = lines.iterator();
            iterator.next(); // skip the first line
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] parts = line.split("\t");
                countryCodeToCountry.put(parts[2].toLowerCase(), parts[0]);
                countryToCountryCode.put(parts[0], parts[2].toLowerCase());
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Return the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        String country =  countryCodeToCountry.get(code);
        return country;
    }

    /**
     * Return the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        String code = countryToCountryCode.get(country);
        return code;
    }

    /**
     * Return how many countries are included in this country code converter.
     * @return how many countries are included in this country code converter.
     */
    public int getNumCountries() {
        return countryCodeToCountry.size();
    }

    public List<String>  getCountries() {
        return new ArrayList<>(countryToCountryCode.keySet());
    }
}
