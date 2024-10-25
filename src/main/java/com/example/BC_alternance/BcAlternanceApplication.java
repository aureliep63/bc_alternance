package com.example.BC_alternance;

import com.example.BC_alternance.model.Borne;
import com.example.BC_alternance.model.Lieux;
import com.example.BC_alternance.model.Reservation;
import com.example.BC_alternance.model.Utilisateur;
import com.example.BC_alternance.service.impl.BorneServiceImpl;
import com.example.BC_alternance.service.impl.LieuxServiceImpl;
import com.example.BC_alternance.service.impl.ReservationServiceImpl;
import com.example.BC_alternance.service.impl.UtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalTime;

@SpringBootApplication
@EnableScheduling
public class BcAlternanceApplication  {

	public static void main(String[] args) {
		SpringApplication.run(BcAlternanceApplication.class, args);
	}

}
