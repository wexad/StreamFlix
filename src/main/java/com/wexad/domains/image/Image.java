package com.wexad.domains.image;

import com.wexad.domains.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseDomain {
    private String extension;
    private String mimeType;
    private String originalName;
    private UUID movieId;
}
//private String originalName;  originalName
//private String generatedName; id
//private String mimeType;
