/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

public class Documento {

    private int id;
    private String contenido;
    private double score; 
   
    
    public Documento(int id, String contenido, double score) {
        this.id = id;
        this.contenido = contenido;
        this.score = score;
    }

   
    public Documento(int id, String contenido) {
        this.id = id;
        this.contenido = contenido;
        this.score = 0.0; 
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) { 
        this.score = score;
    }

    @Override
    public String toString() {
        return "Documento{"
                + "id=" + id
                + ", contenido='" + contenido + '\''
                + ", score=" + score
                + '}';
    }
}
