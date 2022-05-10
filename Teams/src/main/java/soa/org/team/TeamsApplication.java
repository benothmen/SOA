package soa.org.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import java.util.Collection;

enum Rank {
    Iron, Bronze, Silver, Gold, Platinum, Diamond, Immortal, Radiant
}
@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class Team{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy= "team")
    private Collection<Player> players ;
}

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class Player{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String name;
    @ManyToOne
    private Team team;
}
@Data
class User{
    private Long Id;
    private String userName;
    private int score;
    private Rank rank;
    private String email;
    private String password;
}
@RepositoryRestResource
interface TeamRepository extends JpaRepository<Team,Long>{

}
@RepositoryRestResource
interface PlayerRepository extends JpaRepository<Player,Long>{

}
@Projection(name = "fullTeam",types = Team.class)
interface TeamProjection{
    public Long getId();
    public Collection<Player> getPlayers();
}

@FeignClient(value = "users", url = "https://localhost:8080")
interface UserService {
    @GetMapping(path = "/user/{id}")
    User getUserById(@PathVariable Long id);
}

@SpringBootApplication
public class TeamsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamsApplication.class, args);
    }
    @Bean
    CommandLineRunner start(TeamRepository teamRepository,
                            PlayerRepository playerRepository,
                            RepositoryRestConfiguration repositoryRestConfiguration){
        return args -> {
            repositoryRestConfiguration.exposeIdsFor(Team.class);
            Team team1=teamRepository.save(new Team(null,null));
            playerRepository.save(new Player(null,1L,"aa",team1));
            playerRepository.save(new Player(null,2L,"bb",team1));
            playerRepository.save(new Player(null,3L,"cc",team1));
            playerRepository.save(new Player(null,4L,"dd",team1));
            playerRepository.save(new Player(null,5L,"ee",team1));
            Team team2=teamRepository.save(new Team(null,null));
            playerRepository.save(new Player(null,10L,"tt",team2));
            playerRepository.save(new Player(null,11L,"nn",team2));
            playerRepository.save(new Player(null,16L,"jj",team2));
            playerRepository.save(new Player(null,17L,"zz",team2));
            playerRepository.save(new Player(null,20L,"kk",team2));
        };
    }
}
