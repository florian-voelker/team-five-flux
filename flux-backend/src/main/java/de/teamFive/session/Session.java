package de.teamFive.session;


import de.teamFive.common.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    private String id;

    @ManyToOne
    private User user;

    @ElementCollection
    private List<Integer> gamestate;
}
