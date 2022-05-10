package soa.org.matchmaking;

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
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.util.Collection;
import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class MatchMaking{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    @OneToMany(mappedBy = "matchMaking")
    private Collection<Team> teams;
}

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
class Team{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy= "team")
    private Collection<Player> players;
    @ManyToOne
    private MatchMaking matchMaking;
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

@RepositoryRestResource
interface MatchMakingRepository extends JpaRepository<MatchMaking,Long>{

}

@FeignClient(value = "teams", url = "https://localhost:8081")
interface TeamServices {
    @GetMapping(path = "/teams/{id}")
    Team getTeamsById(@PathVariable Long id);
}

@FeignClient(value = "player", url = "https://localhost:8081")
interface PlayerServices {
    @GetMapping( path = "/teams/{id}/players")
    Team getPlayerByTeam(@PathVariable Long id);
}

@SpringBootApplication
public class MatchmakingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchmakingApplication.class, args);
    }
    @Bean
    CommandLineRunner start(MatchMakingRepository matchMakingRepository, RepositoryRestConfiguration repositoryRestConfiguration){
        return args -> {
            repositoryRestConfiguration.exposeIdsFor(MatchMaking.class);
            matchMakingRepository.save(new MatchMaking(null,new Date(),null));
            matchMakingRepository.save(new MatchMaking(null,new Date(),null));
            matchMakingRepository.save(new MatchMaking(null,new Date(),null));
            matchMakingRepository.save(new MatchMaking(null,new Date(),null));
        };
    }
}
