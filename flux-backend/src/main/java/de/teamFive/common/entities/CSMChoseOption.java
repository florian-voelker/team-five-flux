package de.teamFive.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class CSMChoseOption extends WebSocketMessage {
    private Integer option;

    public CSMChoseOption(String type, Integer option) {
        super(type);
        this.option = option;
    }
}
