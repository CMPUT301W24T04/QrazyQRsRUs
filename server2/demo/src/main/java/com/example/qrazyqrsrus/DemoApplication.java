package com.example.qrazyqrsrus;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	//we initialize our firebase app and firebase messaging service
	//this method of initialization is from https://www.youtube.com/watch?v=pbAoRzU7OXY&ab_channel=Utter, Accessed on Mar. 29th, 2024
	@Bean
	FirebaseMessaging firebaseMessaging() throws IOException{
		GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
				new ClassPathResource("service_account_key.json").getInputStream()
		);
		FirebaseOptions firebaseOptions = FirebaseOptions.builder()
				.setCredentials(googleCredentials).build();
		FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions);
		return FirebaseMessaging.getInstance(app);
	}


	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
