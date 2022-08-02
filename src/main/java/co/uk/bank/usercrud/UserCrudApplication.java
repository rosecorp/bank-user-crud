package co.uk.bank.usercrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class UserCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCrudApplication.class, args);
	}

}
