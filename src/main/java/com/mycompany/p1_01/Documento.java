/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.p1_01;

public class Documento {

    private int id;
    private String contenido;

    public Documento(int id, String contenido) {
        this.id = id;
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String toString() {
        return "Documento{id=" + id + ", contenido='" + contenido + "'}";
    }
}
