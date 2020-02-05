package de.teamFive.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class SSMGameState extends WebSocketMessage {
    private Integer lastRoomId;
    private String result;
    private String description;
    private List<AvailableAction> allActions;

    public SSMGameState(Integer lastRoomId, String result, String description, List<AvailableAction> allActions) {
        super("NEW_GAME_STATE");
        this.lastRoomId = lastRoomId;
        this.result = result;
        this.description = description;
        this.allActions = allActions;
    }
}
