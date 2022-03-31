package com.example.cryptofx.brutforce;

import com.example.cryptofx.cryptor.CaesarCryptor;
import com.example.cryptofx.cryptor.Cryptor;
import com.example.cryptofx.utils.Alphabet;
import com.example.cryptofx.utils.ConsoleColors;
import com.example.cryptofx.utils.Logger;

import java.util.HashMap;
import java.util.Map;

public class CaesarBrutfoce implements Brutforce{

    @Override
    public String doBrutforce(String data) {
        Logger logger = new Logger();
        Map<Integer, String> searchResults = new HashMap<>();
        Cryptor cryptor = new CaesarCryptor();
        if (data != null && data.length() > 0) { // Здесь может быть стоит проверить на длину, если текст слишком длинный, взять половину от него
            int key = 0;
            boolean stop = false;
            while (!stop) {
                // Понимаем, что прошли круг и останавливаемся
                if (key != 0 && key - Alphabet.getCharacterList().size() == 0) {
                    stop = true;
                }

                String encData = cryptor.decrypt(data, key);
                if (isBrutForceDone(encData)) {
                    // Записываем варианты которые нашли в мапу
                    searchResults.put(key, encData);
                }
                if (!stop)
                    key++;
            }
            logger.info("Brutforce done. Possible case(s) count: " + searchResults.size() + "\n", ConsoleColors.GREEN);

            if (searchResults.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                logger.info(searchResults.size() > 1 ? "POSSIBLE KEYS: " : "POSSIBLE KEY: ", ConsoleColors.PURPLE);
                String keysString = "";
                for (Map.Entry<Integer, String> entry : searchResults.entrySet()) {
                    keysString += entry.getKey() + ", ";
                    stringBuilder.append("key '" + entry.getKey() + "':\n\n");
                    stringBuilder.append(entry.getValue() + "\n\n");
                }
                logger.info(keysString.substring(0, keysString.length() - 2), ConsoleColors.PURPLE);
                return stringBuilder.toString();
            }
        }
        return null;
    }

    private boolean isBrutForceDone(String dataForAnalyze) {
        String data = dataForAnalyze.toUpperCase();
        // Над этим условием можно подумать еще конечно
        if (data.endsWith(":") || data.endsWith(",") || data.endsWith("-"))
            return false;

        String[] splitString = data.split("\\s");
        int countOfRealWords = 0;

        for (String s : splitString) {
            if (!s.equals("")) {
                String additionalCh = Alphabet.containsAdditional(s);
                if (additionalCh != null) {
                    // Если сходу понимаем, что чушь, сразу выкидываем
                    if (!Alphabet.startWithAdditional(s) && Alphabet.containsAdditionalCount(s) > 2 && !s.equals("-")) {
                        return false;
                    } else if (Alphabet.startWithAdditional(s) && countOfRealWords <=0) {
                        return false;
                    }

                    if (!s.startsWith(additionalCh)) {
                        if (s.endsWith(additionalCh)) {
                            String[] lastSplit = s.split("\\" + additionalCh);
                            if (lastSplit.length == 1 && Alphabet.containsAdditional(lastSplit[0]) == null) {
                                countOfRealWords++;
                            } else {
                                countOfRealWords--;
                            }
                        } else {
                            if (Alphabet.containsAdditionalCount(s) == 1 && s.contains("-")){
                                countOfRealWords++;
                            } else {
                                countOfRealWords--;
                            }
                        }
                    } else {
                        countOfRealWords--;
                    }
                } else {
                    countOfRealWords++;
                }
            }
        }
        return countOfRealWords > 0;
    }
}
