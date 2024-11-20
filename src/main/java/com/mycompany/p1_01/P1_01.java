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
import java.util.UUID;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class P1_01 {

    public static void main(String[] args) {
        String filePath = "C:\\Users\\josec\\OneDrive\\Escritorio\\UNIVERSIDAD\\4-AÑO\\MB\\MED.ALL";
        String solrUrl = "http://localhost:8983/solr";
        String collectionName = "coleccionMed";

        List<Documento> documentos = ParserDocumentosBufferedReader.parsearDocumentos(filePath);

        try (SolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build()) {
            for (Documento doc : documentos) {
                SolrInputDocument solrDoc = new SolrInputDocument();
                solrDoc.addField("id", doc.getId());
                solrDoc.addField("contenido", doc.getContenido());

                UpdateResponse response = solrClient.add(collectionName, solrDoc);
                System.out.println("Subido documento ID: " + doc.getId() + " - Status: " + response.getStatus());
            }
            
            solrClient.commit(collectionName);
            System.out.println("Todos los documentos han sido subidos a Solr.");
        } catch (Exception e) {
            System.out.println("Error al subir documentos a Solr: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
