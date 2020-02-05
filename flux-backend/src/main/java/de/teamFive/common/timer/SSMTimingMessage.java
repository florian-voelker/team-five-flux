package de.teamFive.common.timer;

import de.teamFive.common.entities.WebSocketMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SSMTimingMessage extends WebSocketMessage {
    private String msg;

    public SSMTimingMessage(String type, String msg) {
        super(type);
        this.msg = msg;
    }
}
