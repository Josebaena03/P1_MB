/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

import java.util.HashSet;
import java.util.Set;

public class StopwordHandler {

    private static final Set<String> STOPWORDS = new HashSet<>(Set.of(
            "a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if",
            "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
            "that", "the", "their", "then", "there", "these", "they", "this",
            "to", "was", "will", "with", "stopworda", "stopwordb"
    ));

    public boolean esStopword(String palabra) {
        return STOPWORDS.contains(palabra.toLowerCase());
    }
}
