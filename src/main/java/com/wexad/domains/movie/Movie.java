package com.wexad.domains.movie;

import com.wexad.domains.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseDomain {
    private String name;
    private String title;
    private String trailerUrl;
    private String genre;
    private Integer duration;
    private Double rating;
}
