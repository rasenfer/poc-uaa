package poc.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "poc.uaa.*", "poc.fwk.*" })
public class UAAApplication {

	public static void main(String[] args) {
		SpringApplication.run(UAAApplication.class, args);
	}
}
