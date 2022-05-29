package zad1;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Test {
    public static void main(String[] args) {
        Locale locale = new Locale("pl","PL");
        Locale locale2 = new Locale("en","GB");
        String c = "Chiny";
        for (Locale L : Locale.getAvailableLocales()) {
            if (L.getDisplayCountry(locale).equals(c)) {
                System.out.println(L.getDisplayCountry(locale));
            }
        }

    }

}
