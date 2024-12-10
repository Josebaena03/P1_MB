/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolrHandler {

    private final SolrClient solrClient;

    public SolrHandler(String solrUrl) {
        this.solrClient = new HttpSolrClient.Builder(solrUrl).build();
    }

    public void indexarDocumento(String collectionName, Documento doc) throws SolrServerException, IOException {
        SolrInputDocument solrDoc = new SolrInputDocument();
        solrDoc.addField("id", doc.getId());
        solrDoc.addField("contenido", doc.getContenido());

        solrClient.add(collectionName, solrDoc);
        solrClient.commit(collectionName);
    }

    public List<Documento> buscarEnColeccion(String collectionName, String consulta, int maxResultados)
            throws SolrServerException, IOException {
        List<Documento> documentos = new ArrayList<>();
        SolrQuery query = new SolrQuery();
        query.setQuery("contenido:" + consulta);
        query.setRows(maxResultados);

        QueryResponse response = solrClient.query(collectionName, query);
        SolrDocumentList resultados = response.getResults();

        for (SolrDocument doc : resultados) {
            int id = Integer.parseInt((String) doc.getFieldValue("id"));
            String contenido = (String) doc.getFieldValue("contenido");
            documentos.add(new Documento(id, contenido));
        }

        return documentos;
    }

    public void cerrar() throws IOException {
        solrClient.close();
    }
}
