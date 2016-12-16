package services;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Marcin on 21.11.2016.
 */
public class Beaufort {
    private TreeMap<Character, Integer> map;

    public Beaufort() {
        //Create A=0, B=1, C=2, D=3.....
        this.map = new TreeMap<Character, Integer>();
        int j = 0;
        for (int i = 65; i <= 90; i++) {
            char temp = (char) i;
            map.put(temp, j);
            j++;
        }
    }

    public Object getKeyFromValue(Integer v) {
        Character ch = null;
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            Object key = e.getKey();
            Object value = e.getValue();
            if (v == value) {
                ch = (Character) key;
            }
        }
        return ch;
    }

    public String encryptOrDecrypt(String text, int key) {
        /*
        Kluczem k jest dowolna liczba od 0 do 25.
        Aby zaszyfrować literę na pozycji p, najpierw odejmujemy ją od 26,
        otrzymując liczbą, którą oznaczymy przez -p. Następnie dodajemy -p i k (klucz),
        a otrzymaną sumę wpasowujemy w przedział 0-25, tj. jeśli suma jest większa od 25,
        to odejmujemy od niej 26, jeżeli suma jest mniejsza od zera, to dodajemy do niej 26
         */
        String textToEncrypt = text.toUpperCase().replaceAll("\\s+", "");
        StringBuilder encryptedText = new StringBuilder("");
        for (int i = 0; i < textToEncrypt.length(); i++) {
            char c = textToEncrypt.charAt(i);
            int minusP = 26 - map.get(c);
            int sum = minusP + key;
            if (sum > 25) {
                sum = sum - 26;
            } else if (sum < 0) {
                sum = sum + 26;
            }
            //System.out.println(sum);
            encryptedText.append(getKeyFromValue(sum));
        }
        return encryptedText.toString();
    }
}
