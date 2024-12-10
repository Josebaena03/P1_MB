/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserDocumentosBufferedReader {

    private final StopwordHandler stopwordHandler = new StopwordHandler();

    public List<Documento> parsearDocumentos(String filePath) {
        List<Documento> documentos = new ArrayList<>();
        StringBuilder contenidoActual = new StringBuilder();
        int idActual = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(".I")) {
                    if (idActual != -1) {
                        documentos.add(new Documento(idActual, contenidoActual.toString().strip()));
                        contenidoActual.setLength(0);
                    }
                    idActual = Integer.parseInt(line.split(" ")[1]);
                } else if (!line.startsWith(".W")) {
                    contenidoActual.append(line.strip()).append(" ");
                }
            }
            if (idActual != -1) {
                documentos.add(new Documento(idActual, contenidoActual.toString().strip()));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
        return documentos;
    }

    public List<String> leerConsultas(String filePath, int maxPalabras) {
        List<String> consultas = new ArrayList<>();
        StringBuilder consultaActual = new StringBuilder();
        boolean leyendoConsulta = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.startsWith(".I")) {
                    if (leyendoConsulta) {
                        String consultaProcesada = extraerPrimerasPalabras(consultaActual.toString(), maxPalabras);
                        consultas.add(escaparConsulta(consultaProcesada));
                        consultaActual.setLength(0);
                    }
                    leyendoConsulta = true;
                } else if (!linea.startsWith(".W")) {
                    consultaActual.append(linea.strip()).append(" ");
                }
            }
            if (leyendoConsulta) {
                String consultaProcesada = extraerPrimerasPalabras(consultaActual.toString(), maxPalabras);
                consultas.add(escaparConsulta(consultaProcesada));
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de consultas: " + e.getMessage());
            e.printStackTrace();
        }
        return consultas;
    }

    private String extraerPrimerasPalabras(String texto, int n) {
        String[] palabras = texto.split("\\s+");
        StringBuilder resultado = new StringBuilder();
        int contador = 0;

        for (String palabra : palabras) {
            if (!stopwordHandler.esStopword(palabra)) {
                resultado.append(palabra).append(" ");
                contador++;
                if (contador == n) {
                    break;
                }
            }
        }
        return resultado.toString().strip();
    }

    private String escaparConsulta(String consulta) {
        return consulta.replaceAll("([+\\-!(){}\\[\\]^\"~*?:\\\\/])", "\\\\$1");
    }
}
