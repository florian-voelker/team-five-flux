package de.teamFive.savegame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavegameDTO {

    private Long id;
    private Long timestamp;
    private String name;
    private List<Integer> savegame;
}
