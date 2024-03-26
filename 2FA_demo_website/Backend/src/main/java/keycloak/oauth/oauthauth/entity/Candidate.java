package keycloak.oauth.oauthauth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "Candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", nullable = false)
    private UUID Id;

    @Column(name = "NrVotes", nullable = false)
    @Size(min = 0)
    private Integer nrVotes;

    @Column(name = "Position", nullable = false)
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "UserID", foreignKey = @ForeignKey(name = "FK_User_ID_Candidate"))
    private User user;
}
