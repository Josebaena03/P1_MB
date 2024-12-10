/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.p1_01;

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

        SolrHandler solrHandler = new SolrHandler(solrUrl);
        ParserDocumentosBufferedReader parser = new ParserDocumentosBufferedReader();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Indexar documentos");
            System.out.println("2. Realizar consultas");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1 -> indexarDocumentos(indexFilePath, collectionName, solrHandler, parser);
                    case 2 -> realizarConsultas(queryFilePath, collectionName, solrHandler, parser);
                    case 3 -> {
                        System.out.println("Saliendo del programa...");
                        solrHandler.cerrar();
                        return;
                    }
                    default -> System.out.println("Opción inválida. Intente de nuevo.");
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

    private static void realizarConsultas(String filePath, String collectionName, SolrHandler solrHandler, ParserDocumentosBufferedReader parser)
            throws IOException, SolrServerException {
        System.out.println("Leyendo consultas desde el archivo: " + filePath);

        List<String> consultas = parser.leerConsultas(filePath, 5);
        for (String consulta : consultas) {
            System.out.println("\nConsulta: " + consulta);

            List<Documento> resultados = solrHandler.buscarEnColeccion(collectionName, consulta, 100);
            System.out.println("Resultados encontrados: " + resultados.size());

            for (Documento doc : resultados) {
                System.out.println(doc);
            }
        }
    }
}
