package com.example.equipos;

import com.example.equipos.DAO.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EquiposApplication {

	@Autowired
	private EquipoRepository equipoRepository;

	/*@Autowired
	private EstadioRepository estadioRepository;*/

	public static void main(String[] args) {
		SpringApplication.run(EquiposApplication.class, args);
	}

}
