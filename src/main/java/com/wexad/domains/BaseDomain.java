package com.wexad.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public abstract class BaseDomain implements Serializable {
    private UUID id;
    private boolean isActive;
    private LocalDateTime createdDate;
}
