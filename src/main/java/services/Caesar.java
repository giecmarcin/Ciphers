package services;

public class Caesar {

    /*
    Szyfr Cezara, stosowany prze Juliusza Cezara do szyfrowania swoich listów jest bardzo prosty,
     ale i przy tym w zasadzie nieskuteczny. Choć podobno Rosjanie jeszcze na początku XX wieku korzystali
     w wojsku z tego szyfru, ze względu na jego prostotę :)

     Algorytm jest wręcz banalny, opiera się na przesunięciu o 3 każdej litery alfabetu. Gdy szyfrujemy przesuwamy
     „w prawo”, gdy deszyfrujemy „w lewo”.
     */
    public String caesar(String textToEncrypt, int shift) {
        textToEncrypt = textToEncrypt.replaceAll("\\s+", "").toLowerCase();//remove space
        char[] buffer = textToEncrypt.toCharArray();
        for (int i = 0; i < buffer.length; i++) {
            char letter = buffer[i];
            letter = (char) (letter + shift);
            if (letter > 'z') {
                letter = (char) (letter - 26);
            } else if (letter < 'a') {
                letter = (char) (letter + 26);
            }
            buffer[i] = letter;
        }
        return new String(buffer);
    }
}
