package org.data.mujinaidpplatform.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidateCodeDto {
    private Integer code;
    private String username;
}