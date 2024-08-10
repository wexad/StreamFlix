package com.wexad.domains.movie;

import com.wexad.domains.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseDomain {
    private String title;
    private String trailerUrl;
    private String genre;
    private Integer duration;
    private Double rating;
    private LocalDate releaseDate;
    private MultipartFile[] files;
}
