package com.wexad.controller.admin;

import com.wexad.domains.image.Image;
import com.wexad.domains.movie.Movie;
import com.wexad.domains.screen.Screen;
import com.wexad.domains.show.Show;
import com.wexad.domains.theater.Theater;
import com.wexad.domains.user.AuthUser;
import com.wexad.enums.Genres;
import com.wexad.service.image.ImageService;
import com.wexad.service.movie.MovieService;
import com.wexad.service.screen.ScreenService;
import com.wexad.service.show.ShowService;
import com.wexad.service.theater.TheaterService;
import com.wexad.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final TheaterService theaterService;
    private final ScreenService screenService;
    private final MovieService movieService;
    private final ImageService imageService;
    private final Path PATH = Path.of("D:\\Java\\java-codes\\StreamFlix\\src\\main\\resources\\static\\imgs");
    private final ShowService showService;

    public AdminController(UserService userService, TheaterService theaterService, ScreenService screenService, MovieService movieService, ImageService imageService, ShowService showService) {
        this.userService = userService;
        this.theaterService = theaterService;
        this.screenService = screenService;
        this.movieService = movieService;
        this.imageService = imageService;
        this.showService = showService;
    }

    @GetMapping("")
    public String admin() {
        return "admin/main";
    }

    @GetMapping("/theaters")
    public String theaters(Model model) {
        List<Theater> theaters = theaterService.findAll();
        model.addAttribute("theaters", theaters);
        return "admin/theaters";
    }

    @PostMapping("/theaters/save")
    public String saveTheater(@RequestParam("name") String name, @RequestParam("location") String location) {
        theaterService.save(Theater.builder().location(location).name(name).build());
        return "redirect:/admin/theaters";
    }

    @GetMapping("/theaters/add")
    public String showAddTheaterForm(Model model) {
        model.addAttribute("showForm", true);
        model.addAttribute("theaters", theaterService.findAll());
        return "admin/theaters";
    }

    @GetMapping("/movies/add")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        Genres[] values = Genres.values();
        List<Genres> genres = new ArrayList<>(List.of(values));
        model.addAttribute("genres", genres);
        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("showForm", true);
        return "admin/movies";
    }

    @PostMapping("/movies/save")
    public String saveMovie(
            @RequestParam("title") String title,
            @RequestParam("trailerUrl") String trailerUrl,
            @RequestParam("genre") String genre,
            @RequestParam("duration") Integer duration,
            @RequestParam("releaseDate") LocalDate releaseDate,
            @RequestParam(value = "files") MultipartFile[] files) throws IOException {
        Movie movie = Movie.builder()
                .title(title)
                .trailerUrl(trailerUrl)
                .genre(genre)
                .releaseDate(releaseDate)
                .duration(duration)
                .build();
        UUID movieId = movieService.save(movie);
        for (MultipartFile file : files) {
            UUID id = UUID.randomUUID();
            String generatedName = (id + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
            String extension = "/static/imgs/" + generatedName;
            Path filePath = PATH.resolve(generatedName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            Image image = Image.builder()
                    .id(id)
                    .originalName(file.getOriginalFilename())
                    .movieId(movieId)
                    .mimeType(file.getContentType())
                    .extension(extension)
                    .build();
            imageService.save(image);
        }
        return "redirect:/admin/movies";
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("showForm", false);
        return "admin/movies";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<AuthUser> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    @GetMapping("/screens")
    public String screens(Model model) {
        List<Screen> screens = screenService.findAll();
        List<Theater> theaters = theaterService.findAll();
        Map<UUID, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("screens", screens);
        model.addAttribute("theaterMap", theaterMap);
        return "admin/screens";
    }

    @GetMapping("/screens/add")
    public String showAddScreenForm(Model model) {
        model.addAttribute("showForm", true);
        List<Screen> screens = screenService.findAll();
        List<Theater> theaters = theaterService.findAll();
        Map<UUID, String> theaterMap = theaters.stream()
                .collect(Collectors.toMap(Theater::getId, Theater::getName));
        model.addAttribute("screens", screens);
        model.addAttribute("theaterMap", theaterMap);
        model.addAttribute("theaters", theaters);
        return "admin/screens";
    }

    @PostMapping("/screens/save")
    public String saveScreen(@ModelAttribute Screen screen) {
        screenService.save(screen);
        return "redirect:/admin/screens";
    }

    @GetMapping("/users/makeAdmin/{id}")
    public String makeAdmin(@PathVariable("id") UUID id) {
        userService.makeAdmin(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/block/{id}")
    public String blockUser(@PathVariable("id") UUID id) {
        userService.blockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/show/{id}")
    public String showAddShowForm(@PathVariable("id") UUID screenId, Model model) {
        List<Movie> movies = movieService.findAll();
        model.addAttribute("movies", movies);
        model.addAttribute("screenId", screenId);
        return "admin/show";
    }

    @GetMapping("/show")
    public String show(Model model) {
        List<Show> shows = showService.findAll();
        Map<UUID, String> moviesMap = movieService.findAll()
                .stream().collect(Collectors.toMap(Movie::getId, Movie::getTitle));
        model.addAttribute("shows", shows);
        model.addAttribute("moviesMap", moviesMap);
        return "admin/show";
    }

    @PostMapping("/show/save")
    public String saveShow(
            @RequestParam("screenId") UUID screenId,
            @RequestParam("movieId") UUID movieId,
            @RequestParam("price") Double price,
            @RequestParam("showTime") LocalDateTime showTime
    ) {
        Show show = Show.builder()
                .screenId(screenId)
                .movieId(movieId)
                .price(price)
                .showTime(showTime)
                .build();
        showService.save(show);
        return "redirect:/admin/show";
    }
}
