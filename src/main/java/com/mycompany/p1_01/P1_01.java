/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.p1_01;

/**
 *
 * @author josec
 */
import static com.mycompany.p1_01.ParserDocumentosBufferedReader.parsearDocumentos;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class P1_01 {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\josec\\OneDrive\\Escritorio\\UNIVERSIDAD\\4-AÑO\\MB\\MED.QRY";
        String solrUrl = "http://localhost:8983/solr";
        String collectionName = "coleccionMedQRY";

        try (SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build(); 
                Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- Menu ---");
                System.out.println("1. Subir documentos a Solr");
                System.out.println("2. Mostrar documentos de Solr");
                System.out.println("3. Salir");
                System.out.print("Elige una opcion: ");
                int opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        List<Documento> documentos = parsearDocumentos(filePath);
                        for (Documento doc : documentos) {
                            SolrInputDocument solrDoc = new SolrInputDocument();
                            solrDoc.addField("id", doc.getId());
                            solrDoc.addField("contenido", doc.getContenido());

                            UpdateResponse response = solrClient.add(collectionName, solrDoc);
                            System.out.println("Subido documento ID: " + doc.getId() + " - Status: " + response.getStatus());
                        }
                        solrClient.commit(collectionName);
                        System.out.println("Todos los documentos han sido subidos a Solr.");
                        break;

                    case 2:
                   
                        System.out.println("\nConsultando documentos desde Solr...");
                        SolrQuery query = new SolrQuery();
                        query.setQuery("*:*");
                        query.setRows(Integer.MAX_VALUE);

                        QueryResponse response = solrClient.query(collectionName, query);
                        SolrDocumentList resultados = response.getResults();

                        System.out.println("Total de documentos encontrados: " + resultados.getNumFound());
                        for (SolrDocument doc : resultados) {
                            System.out.println("ID: " + doc.getFieldValue("id"));
                            System.out.println("Contenido: " + doc.getFieldValue("contenido"));
                            System.out.println("-----------------------------");
                        }
                        break;

                    case 3:
                        System.out.println("Saliendo del programa...");
                        return;

                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al interactuar con Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
