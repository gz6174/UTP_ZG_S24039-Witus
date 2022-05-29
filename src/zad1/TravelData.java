package zad1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TravelData
{
    String[] rows;

    public TravelData(File dataDir) {
        String[] files = dataDir.list();
        int rowsCount = 0;
        for (String filename : files) {
            rowsCount += getRowsCount(dataDir+"/"+filename);
        }

        rows = new String[rowsCount];
        int rowID = 0;

        for (String filename : files) {
            String[] newRows = readAllLines(dataDir+"/"+filename);
            for (int i = 0; i < newRows.length; i++) {
                rows[rowID] = newRows[i];
                rowID++;
            }
        }
    }

    private int getRowsCount(String filename) {
        int res = 0;
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filename));
            while (bfr.readLine() != null) {
                res++;
            }
        } catch (Exception e) {e.printStackTrace();}
        return res;
    }

    private String[] readAllLines(String filename) {
        String[] res = new String[getRowsCount(filename)];
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filename));
            String nextLine = bfr.readLine();
            int i = 0;
            while (nextLine != null) {
                res[i] = nextLine;
                i++;
                nextLine = bfr.readLine();
            }
        } catch (Exception e) {e.printStackTrace();}
        return res;
    }

    public List<String> getOffersDescriptionsList(String loc, String dateFormat) {
        List<String> res = new LinkedList<>();
        Locale currentLocale = Locale.getDefault();
        Locale outLocale = new Locale(loc.split("_")[0],loc.split("_")[1]);
        for (int i = 0; i < rows.length; i++) {
            String[] row_split = rows[i].split("\t");
            String[] rowLoc = row_split[0].split("_");
            String country = row_split[1];
            String dateFrom = row_split[2];
            String dateTo = row_split[3];
            String object = row_split[4];
            String number = row_split[5];
            String currency = row_split[6];

            Locale locale = currentLocale;
            if (rowLoc.length==1) {
                locale = new Locale(rowLoc[0]);
            } else if (rowLoc.length==2) {
                locale = new Locale(rowLoc[0], rowLoc[1]);
            } else if (rowLoc.length==3) {
                locale = new Locale(rowLoc[0], rowLoc[1], rowLoc[2]);
            }
            Locale.setDefault(locale);

            String outCountry = translateCountry(country, locale, outLocale);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            LocalDate temp_dateFrom = LocalDate.parse(dateFrom, formatter);
            LocalDate temp_dateTo = LocalDate.parse(dateTo, formatter);

            String outDateFrom = temp_dateFrom.toString();
            String outDateTo = temp_dateTo.toString();
            String outObject = translate(object, locale, outLocale);

            String outNumber = "";
            try {
                NumberFormat nf = NumberFormat.getInstance(locale);
                double myNumber = nf.parse(number).doubleValue();
                nf = NumberFormat.getInstance(outLocale);
                outNumber=nf.format(myNumber);
            } catch (Exception e) {e.printStackTrace();}

            String newRow = outCountry + " " + outDateFrom + " " + outDateTo + " " + outObject + " " + outNumber + " " + currency;
            res.add(newRow);
            Locale.setDefault(currentLocale);
        }
        return res;
    }

    private String translate(String in, Locale loc1, Locale loc2) {
        String loc1Language = loc1.getLanguage();
        String loc2Language = loc2.getLanguage();
        if (loc1Language.equals(loc2Language)) {
            return in;
        } else if (loc1Language.equals("en")) {
            return translate_en_pl(in);
        } else if (loc1Language.equals("pl")) {
            return translate_pl_en(in);
        } else {
            return in;
        }
    }

    private String translate_pl_en(String in) {
        return switch (in) {
            case "jezioro" -> "lake";
            case "morze" -> "sea";
            case "góry" -> "mountains";
            default -> in;
        };
    }

    private String translate_en_pl(String in) {
        return switch (in) {
            case "lake" -> "jezioro";
            case "sea" -> "morze";
            case "mountains" -> "góry";
            default -> in;
        };
    }

    private String translateCountry(String in, Locale loc1, Locale loc2) {
        String loc1language = loc1.getLanguage();
        String loc2language = loc2.getLanguage();
        if (loc1language.equals(loc2language)) {
            return in;
        }
        Locale translatedCountry = loc1;
        for (Locale L : Locale.getAvailableLocales()) {
            if (L.getDisplayCountry(loc1).equals(in)) {
                translatedCountry = L;
                break;
            }
        }
        return translatedCountry.getDisplayCountry(loc2);
    }

    public String[] getRows() {
        return this.rows;
    }

}
