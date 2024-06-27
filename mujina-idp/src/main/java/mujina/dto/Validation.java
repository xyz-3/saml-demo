package mujina.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Validation {
    private boolean valid;

    public Validation(boolean valid) {
        this.valid = valid;
    }
}
