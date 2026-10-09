package com.agrotis.challenge.config;

import com.agrotis.challenge.entities.Grower;
import com.agrotis.challenge.repositories.GrowerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializerConfig {

	@Bean
	CommandLineRunner initDatabase(GrowerRepository growerRepository) {
		return args -> {
			if (growerRepository.count() == 0) {
				Grower grower = new Grower();
				grower.setName("Fazenda Modelo S.A.");
				grower.setRegistration("12.345.678/0001-99");
				growerRepository.save(grower);
				// Adicione outras entidades (Farmstead, Laboratory) conforme necessário
			}
		};
	}
}