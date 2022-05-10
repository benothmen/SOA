package soa.org.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

enum Rank {
	Iron, Bronze, Silver, Gold, Platinum, Diamond, Immortal, Radiant
}
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class User{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
 	private Long Id;
	private String userName;
	private int score;
	private Rank rank;
	private String email;
	private String password;
}
@RepositoryRestResource
interface UserRepository extends JpaRepository<User,Long>{

}
@SpringBootApplication
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	@Bean
	CommandLineRunner start(UserRepository userRepository, RepositoryRestConfiguration repositoryRestConfiguration){
		return args -> {
			repositoryRestConfiguration.exposeIdsFor(User.class);
			userRepository.save(new User(null,"zied",200,Rank.Gold,"zied@gmail.com","0000"));
			userRepository.save(new User(null,"syrine",400,Rank.Diamond,"syrine@gmail.com","0000"));
			userRepository.save(new User(null,"Fatma",100,Rank.Bronze,"fatma@gmail.com","0000"));
		} ;
	}
}
