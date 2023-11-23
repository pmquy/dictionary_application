package com.example.dictionary.api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class Translate {

    /**
     * Translates a word from one language to another.
     *
     * @param word The word to be translated.
     * @param sl The source language code.
     * @param tl The target language code.
     * @return The translated word.
     * @throws Exception If an error occurs during translation.
     */
    public static String translate(String word, String sl, String tl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getURL(word, sl, tl)))
                .header("Accept-Encoding", "application/gzip")
                .build();
        HttpResponse<String> response;
        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body().split("\"")[1];
    }

    /**
     * Get a URL for translation using Google Translate API.
     *
     * @param word The word to be translated.
     * @param sl The source language code.
     * @param tl The target language code.
     * @return A string representing the URL for translation.
     */
    private static String getURL(String word, String sl, String tl) {
        return "https://translate.googleapis.com/translate_a/single?client=gtx&dt=t" + "&q=" +
                URLEncoder.encode(word, StandardCharsets.UTF_8) +
                "&sl=" + sl +
                "&tl=" + tl;
    }

    /**
     * Retrieves suggestions or word based on the provided word and type.
     *
     * @param word The word for which suggestions are required.
     * @param type The type of suggestion service.
     * @return An ArrayList containing suggested words.
     */
    public static ArrayList<String> getSuggestions(String word, int type) {
        word = word.replace(" ", "%20");
        ArrayList<String> res = new ArrayList<>();
        if(type == 0) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.oed.com/autocomplete/dictionary/?q=" + word))
                    .header("Accept-Encoding", "application/gzip")
                    .build();
            HttpResponse<String> response;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                Pattern pattern = Pattern.compile("\"name\":\"(.*?)\"");
                Matcher matcher = pattern.matcher(response.body());
                while (matcher.find()) {
                    res.add(matcher.group(1));
                }
            } catch (Exception ignored) {

            } finally {
                return res;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://vi.wiktionary.org/w/api.php?action=opensearch&format=json&formatversion=2&namespace=0&limit=10&search=" + word))
                .header("Accept-Encoding", "application/gzip")
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String[] arr = response.body().split("\\[|\\]")[2].split(",");
            for(int i = 0; i < arr.length; i++)
                res.add(arr[i].substring(1, arr[i].length()-1));
        } catch (Exception ignored) {

        } finally {
            return res;
        }



    }

    /**
     * Retrieves detailed information about words in a given sentence.
     *
     * @param sentence The sentence containing words.
     * @return details about the words in the sentence.
     * @throws Exception If an error occurs while retrieving word details.
     */
    public static String getDetail(String sentence) throws Exception {
        StringBuilder res = new StringBuilder("<html><head><style>::selection{background-color:#8EE4AF; color:#FFFBF5}body {padding : 120px 20px}.container {padding: 10px;background-color : #FFFBF5;border: 2px #05386B solid;display: none;position: absolute;background-color: #5CDB95; color : #EDF5E1;border-radius: 10px;} button {border-radius: 10px;padding: 5px; background-color : #05386B; color : #EDF5E1}</style></head><body><ul>");
        String[] words = sentence.split("\\W+");

        for (String word : words) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/" + word))
                    .header("Accept-Encoding", "application/gzip")
                    .build();
            HttpResponse<String> response;

            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response == null || response.statusCode() == 404) {
                continue;
            }
            res.append("<li><h1 style=\"color : #05386B\">").append(word).append("</h1><ul>");

            JSONArray jsonArray = new JSONArray(response.body());

            res.append("<li><h2 style=\"color : #5CDB95\">Phát âm : </h2>");
            processPhonetics(res, jsonArray.getJSONObject(0).getJSONArray("phonetics"));
            res.append("</li>");

            res.append("<li><h2 style=\"color : #5CDB95\">Nghĩa : </h2>");
            processMeanings(res, jsonArray.getJSONObject(0).getJSONArray("meanings"));
            res.append("</li>");

            res.append("</ul></li>");
        }

        res.append("</ul>");
        res.append("<div class=\"container\"><button class=\"btn\"><img src=\"https://vignette.wikia.nocookie.net/tlaststand/images/2/25/Speaker_Icon.png/revision/latest?cb=20150730190045\" width=\"20px\" alt=\"\"></button>    <p class=\"txt\">sd</p><button class='btn1'>Tìm kiếm</button></div><script>var div = document.querySelector('.container');var btn = document.querySelector('.btn');var btn1 = document.querySelector('.btn1');var txt = document.querySelector('.txt');document.onmouseup = function() {var selection = window.getSelection();var text = selection.toString().trim();if(text.length) {let rect = selection.getRangeAt(0).getBoundingClientRect();div.style.display = 'block';div.style.left = rect.left + scrollX + 'px';div.style.top = rect.bottom + 20 + scrollY + 'px';btn1.onclick = function(){javaConnector.find(text);};btn.onclick = function(){javaConnector.speak(text);};txt.innerHTML = javaConnector.trans(text);} else {div.style.display = 'none'}}</script></body></html>");
        return res.toString();
    }

    /**
     * Processes phonetic information.
     *
     * @param res The StringBuilder to which the HTML content is appended.
     * @param phonetics The JSONArray containing phonetic information.
     */
    private static void processPhonetics(StringBuilder res, JSONArray phonetics) {
        JSONObject temp;
        res.append("<ul>");
        for (int i = 0; i < phonetics.length(); i++) {
            temp = phonetics.getJSONObject(i);
            if (temp.keySet().contains("text")) {
                res.append("<li>").append(temp.getString("text")).append("</li>");
            }
        }
        res.append("</ul>");
    }

    private static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("noun", "danh từ");
        map.put("verb", "động từ");
        map.put("interjection", "thán từ");
        map.put("pronoun", "đại từ");
        map.put("adjective", "tính từ");
        map.put("adverb", "trạng từ");
        map.put("determiner", "từ hạn định");
        map.put("preposition", "giới từ");
        map.put("conjunction", "liên từ");
    }

    /**
     * Processes meanings and constructs representation with details like definitions, examples,
     * synonyms, and antonyms.
     *
     * @param res The StringBuilder to which the HTML content is appended.
     * @param meanings The JSONArray containing meaning information.
     */
    private static void processMeanings(StringBuilder res, JSONArray meanings) {
        JSONObject temp;
        res.append("<ul>");

        for (int i = 0; i < meanings.length(); i++) {
            res.append("<li>");

            temp = meanings.getJSONObject(i);
            res.append("<h3 style=\"color : #0099cc\">Loại : ").append(map.get(temp.getString("partOfSpeech"))).append("</h3><ul>");


            JSONArray jsonArray = temp.getJSONArray("definitions");
            JSONObject temp1;

            res.append("<li><h4 style=\"color : #cc6600\">Các định nghĩa : </h4><ul style=\"list-style : circle\">");
            for (int j = 0; j < jsonArray.length(); j++) {
                res.append("<li>");
                temp1 = jsonArray.getJSONObject(j);
                if (temp1.keySet().contains("definition")) {
                    res.append("<p>Nghĩa : ").append(temp1.getString("definition")).append("</p>");
                }
                if (temp1.keySet().contains("example")) {
                    res.append("<p>Ví dụ : ").append(temp1.getString("example")).append("</p>");
                }
                res.append("</li>");
            }
            res.append("</ul></li>");


            jsonArray = temp.getJSONArray("synonyms");
            res.append("<li><h4 style=\"color : #cc6600\">Từ đồng nghĩa : </h4><ul style=\"list-style : circle\">");
            for (int j = 0; j < jsonArray.length(); j++) {
                res.append("<li>").append(jsonArray.getString(j)).append("</li>");
            }
            res.append("</ul></li>");


            jsonArray = temp.getJSONArray("antonyms");
            res.append("<li><h4 style=\"color : #cc6600\">Từ trái nghĩa : </h4><ul style=\"list-style : circle\">");
            for (int j = 0; j < jsonArray.length(); j++) {
                res.append("<li>").append(jsonArray.getString(j)).append("</li>");
            }
            res.append("</ul></li>");

            res.append("</ul></li>");
        }

        res.append("</ul>");
    }
}
