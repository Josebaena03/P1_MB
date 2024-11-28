/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josec
 */
public class ParserDocumentosBufferedReader {

    public static List<Documento> parsearDocumentos(String filePath) {
        List<Documento> documentos = new ArrayList<>();
        StringBuilder contenidoActual = new StringBuilder();
        int idActual = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(".I")) {
                    // Guardar documento actual si existe
                    if (idActual != -1) {
                        String contenidoLimitado = extraerPrimerasPalabras(contenidoActual.toString(), 5);
                        documentos.add(new Documento(idActual, contenidoLimitado));
                        contenidoActual.setLength(0); // Resetear contenido
                    }
                    // Extraer ID del nuevo documento
                    idActual = Integer.parseInt(line.split(" ")[1]);
                } else if (!line.startsWith(".W")) {
                    // Acumular contenido (ignorar encabezado .W)
                    contenidoActual.append(line.strip()).append(" ");

                }
            }
            // Agregar el último documento
            if (idActual != -1) {
                String contenidoLimitado = extraerPrimerasPalabras(contenidoActual.toString(), 5);
                documentos.add(new Documento(idActual, contenidoLimitado));
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
        return documentos;
    }

    private static String extraerPrimerasPalabras(String texto, int n) {
        String[] palabras = texto.split("\\s+");
        
        StringBuilder resultado = new StringBuilder();
        
        for (int i = 0; i < Math.min(n, palabras.length); i++) {
            resultado.append(palabras[i]).append(" ");
        }

        return resultado.toString().strip();
    }
}
