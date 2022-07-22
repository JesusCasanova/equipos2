package com.example.equipos.services;

import com.example.equipos.DAO.EquipoRepository;
import com.example.equipos.entities.Equipo;
import com.example.equipos.entities.Jugador;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//con esto  y el resto de la anotación hacemos parte del punto dos de la foto.
@RestController
public class EquipoRestServices {


    @Autowired
    private EquipoRepository equipoRepository;


    /*
    Operacion para listar todos los equipos de la base de datos mediante el metodo get.
     */
    @GetMapping(value = "/listEquipos")
    // El tipo de elemento que contiene la lista será el nombre de la clase, equipo en este caso.
    public List<Equipo> listEquipos() {
        return equipoRepository.findAll();
    }
        /*
        Crear un proyecto nuevo de fichajes, siguiendo los pasos de este y del de jugador
        hecho la integracion de base de datos para jugadores y equipos, falta fichajes

         */
        @GetMapping(value = "/listEquipos/{id}")
        public Equipo listEquipos(@PathVariable(name = "id") Long id) {
            return equipoRepository.findById(id).get();
        }

        /*
        13/07
         */
        @GetMapping(value = "/transfermarkt/liga/{id}")
        public void buscaEquiposTM(@PathVariable(name = "id") String id) throws IOException, InterruptedException {
            String URITMget = "https://transfermarket.p.rapidapi.com/clubs/list-by-competition?id="+id;
            String headers= "";
            JSONObject objetoTM = doRequestHeader("GET", URITMget, "X-RapidAPI-Key", "2b5f9bb939msh8cc9809ff25e964p1e2f47jsn9bfabdd4f948", "X-RapidAPI-Host", "transfermarket.p.rapidapi.com");
            System.out.println(objetoTM);
            String URIEquipo = "http://localhost:8082/equipoes";
            JSONObject equipoJSON = null;
            String nombreEquipo= null;
            JSONObject objetoEquipo = new JSONObject();
            Map<String, String> map = new HashMap<>();
            for(int i= 0; i< objetoTM.getJSONArray("clubs").length(); i++) {
                try {
                    nombreEquipo = objetoTM.getJSONArray("clubs").getJSONObject(i).getString("name");
                } catch(Exception e){
                    System.out.println("Error en la recuperacion del nombre del equipo.."+e);
                    nombreEquipo= "";
                }
                System.out.println(nombreEquipo);
                /*equipoJSON= new JSONObject();
                equipoJSON.put();
                equipoJSON.put("idnac", 3);
                equipoJSON.put("idliga", 1);
                equipoJSON.put("idestadio", 1);*/
                map.put("nombre", nombreEquipo);
                map.put("idnac", String.valueOf(3));
                map.put("idliga", String.valueOf(1));
                map.put("idestadio", String.valueOf(1));
                System.out.println(map.toString());
                doRequestBody("POST", URIEquipo, map);
            }

        }



        @GetMapping(value = "/listEquipos/{id}/jugadores")
        public List<Jugador> listEquiposJugadores(@PathVariable(name = "id") Long id) throws IOException, InterruptedException {
            List<Jugador> listaJugadores= new ArrayList<>();
            /*
             * obtenemos una serie de fichajes para un equipo determinado (tabla fichajes)
             * */
            String URIfichajes = "http://localhost:8083/fichajes/search/byIdequ?idequ="+id;
            JSONObject objetoFichajes = doRequest("GET", URIfichajes);
            System.out.println(objetoFichajes);
            String URIjugadores = null;
            JSONObject objetoJugadores = null;

            //bucle
            /*
             * por cada fichaje hacemos de nuevo una llamada a otro ws en este caso al de jugadores
             * cada jugador que me devuelva lo meto en la lista de jugadores listaJugadores
             *
             * */
            for (int i= 0; i< objetoFichajes.getJSONObject("_embedded").getJSONArray("fichajes").length(); i++){
                System.out.println("Id del jugador del equipo "+id+": "+objetoFichajes.getJSONObject("_embedded").getJSONArray("fichajes").getJSONObject(i).getLong("idjug"));
                URIjugadores= "http://localhost:8081/listJugadores/"+objetoFichajes.getJSONObject("_embedded").getJSONArray("fichajes").getJSONObject(i).getLong("idjug");
                objetoJugadores = doRequest("GET", URIjugadores);
                System.out.println(objetoJugadores);
                //añadimos el jugador a la lista
                listaJugadores.add(new Jugador(objetoJugadores.getLong("id"), objetoJugadores.getString("nombre"), objetoJugadores.getLong("idnac")));
            }

            //fin bucle interno
            //fin bucle externo

            return listaJugadores;
        }

        public JSONObject doRequest(String method, String uri) throws IOException, InterruptedException {
            return new JSONObject((HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                            .uri(URI.create(uri))
                            .method(method, HttpRequest.BodyPublishers.noBody())
                            .build()
                    , HttpResponse.BodyHandlers.ofString())).body());
        }

    public CompletableFuture<Void> doRequestBody(String method, String uri, Map<String, String> map) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(map);

        HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return HttpClient.newHttpClient()
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(System.out::println);
    }
/*
13/7
 */

        public JSONObject doRequestHeader(String method, String uri, String h1c, String h1v, String h2c, String h2v) throws IOException, InterruptedException {
            return new JSONObject((HttpClient.newHttpClient().send(HttpRequest.newBuilder()
                            .uri(URI.create(uri)).header(h1c, h1v).header(h2c, h2v)
                            .method(method, HttpRequest.BodyPublishers.noBody())
                            .build()
                    , HttpResponse.BodyHandlers.ofString())).body());
        }

        @PutMapping(value = "/listEquipos/{id}")
        public  Equipo update(@PathVariable(name = "id") Long id, @RequestBody  Equipo j) {
            j.setId(id);
            return equipoRepository.save(j);
        }

        @PostMapping(value = "/listEquipos")
        public Equipo save(@RequestBody Equipo j) {
            return equipoRepository.save(j);
        }

        @PostMapping(value = "/listEquipos/{id}")
        public void delete(@PathVariable(name = "id") Long id) {
            equipoRepository.deleteById(id);
        }

        // GET => obtener informacion (select sql)
        // PUT => actualizar informacion (update sql)
        // POST => crear (insert sql) / borrar (delete sql) informacion

    }

