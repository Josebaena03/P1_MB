/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.p1_01;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class P1_01 {

    public static void main(String[] args) {
        String solrUrl = "http://localhost:8983/solr";
        String collectionName = "coleccionMed";
        String indexFilePath = "C:\\Users\\josec\\OneDrive\\Escritorio\\UNIVERSIDAD\\4-AÑO\\MB\\MED.ALL";
        String queryFilePath = "C:\\Users\\josec\\OneDrive\\Escritorio\\UNIVERSIDAD\\4-AÑO\\MB\\MED.QRY";
        String trecFilePath = "C:\\Users\\josec\\OneDrive\\Escritorio\\UNIVERSIDAD\\4-AÑO\\MB\\P1_01\\trec_solr_file.txt";
        SolrHandler solrHandler = new SolrHandler(solrUrl);
        ParserDocumentosBufferedReader parser = new ParserDocumentosBufferedReader();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Iniciar Solr");
            System.out.println("2. Detener Solr");
            System.out.println("3. Indexar documentos");
            System.out.println("4. Realizar consultas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1 ->
                        iniciarSolr();
                    case 2 ->
                        detenerSolr();
                    case 3 ->
                        indexarDocumentos(indexFilePath, collectionName, solrHandler, parser);
                    case 4 ->
                        realizarConsultas(queryFilePath, collectionName, solrHandler, parser, trecFilePath);
                    case 5 -> {
                        System.out.println("Saliendo del programa...");
                        solrHandler.cerrar();
                        return;
                    }
                    default ->
                        System.out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void indexarDocumentos(String filePath, String collectionName, SolrHandler solrHandler, ParserDocumentosBufferedReader parser)
            throws IOException, SolrServerException {
        System.out.println("Indexando documentos desde el archivo: " + filePath);

        List<Documento> documentos = parser.parsearDocumentos(filePath);
        for (Documento doc : documentos) {
            solrHandler.indexarDocumento(collectionName, doc);
        }

        System.out.println("Documentos indexados exitosamente: " + documentos.size());
    }

    private static void realizarConsultas(String filePath, String collectionName, SolrHandler solrHandler,
            ParserDocumentosBufferedReader parser, String trecFilePath) throws IOException, SolrServerException {
        List<String> consultas = parser.leerConsultas(filePath, 5);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(trecFilePath))) {
            int queryId = 1;
            for (String consulta : consultas) {
                List<Documento> resultados = solrHandler.buscarEnColeccion(collectionName, consulta, 100);
                generarArchivoTrec(writer, queryId, resultados);
                queryId++;
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo TREC: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void iniciarSolr() {
        try {
            System.out.println("Iniciando Solr...");
            Process process = Runtime.getRuntime().exec("cmd /c start C:\\solr-8.11.4\\solr-8.11.4\\bin\\solr.cmd start");
            System.out.println("Solr iniciado correctamente.");
        } catch (IOException e) {
            System.err.println("Error al iniciar Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void detenerSolr() {
        try {
            System.out.println("Deteniendo Solr...");
            Process process = Runtime.getRuntime().exec("cmd /c start C:\\solr-8.11.4\\solr-8.11.4\\bin\\solr.cmd stop -all");
            System.out.println("Solr detenido correctamente.");
        } catch (IOException e) {
            System.err.println("Error al detener Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void generarArchivoTrec(BufferedWriter writer, int queryId, List<Documento> resultados)
            throws IOException {
        int contador = 0;
        for (Documento doc : resultados) {
            try {
                writer.write(String.format("%d Q0 %d %d %.6f ETSI%n", queryId, doc.getId(), contador, doc.getScore()));
            } catch (IOException e) {
                System.err.println("Error al escribir la línea TREC para el documento: " + doc.getId());
                e.printStackTrace();
            }
            contador++;
        }
    }
}
