package com.wexad.controller.user;

import com.wexad.domains.bookedSeat.BookedSeat;
import com.wexad.domains.booking.Booking;
import com.wexad.domains.image.Image;
import com.wexad.domains.movie.Movie;
import com.wexad.domains.review.Review;
import com.wexad.domains.screen.Screen;
import com.wexad.domains.show.Show;
import com.wexad.domains.theater.Theater;
import com.wexad.domains.user.AuthUser;
import com.wexad.dto.DataDTO;
import com.wexad.dto.LoginDTO;
import com.wexad.dto.ShowDTO;
import com.wexad.security.SecurityUtils;
import com.wexad.service.bookedSeat.BookedSeatService;
import com.wexad.service.booking.BookingService;
import com.wexad.service.image.ImageService;
import com.wexad.service.movie.MovieService;
import com.wexad.service.review.ReviewService;
import com.wexad.service.screen.ScreenService;
import com.wexad.service.show.ShowService;
import com.wexad.service.theater.TheaterService;
import com.wexad.service.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class HomeController {

    private final MovieService movieService;
    private final ImageService imageService;
    private final ShowService showService;
    private final ScreenService screenService;
    private final TheaterService theaterService;
    private final BookedSeatService bookedSeatService;
    private final UserService userService;
    private final BookingService bookingService;
    private final ReviewService reviewService;

    public HomeController(MovieService movieService, ImageService imageService, ShowService showService, ScreenService screenService, TheaterService theaterService, BookedSeatService bookedSeatService, UserService userService, BookingService bookingService, ReviewService reviewService) {
        this.movieService = movieService;
        this.imageService = imageService;
        this.showService = showService;
        this.screenService = screenService;
        this.theaterService = theaterService;
        this.bookedSeatService = bookedSeatService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        AuthUser user = userService.findByEmail(Objects.requireNonNull(SecurityUtils.getUser()).getUsername()).get();
        model.addAttribute("user", user);
        List<DataDTO> data = bookingService.getData(user.getId());
        model.addAttribute("data", data);
        return "auth/profile";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        UserDetails user = SecurityUtils.getUser();
        model.addAttribute("user", user);
        List<Movie> movies = movieService.findAll();

        HashMap<UUID, String> images = new HashMap<>();
        for (Movie movie : movies) {
            List<Image> imagesList = imageService.findByMovieId(movie.getId());
            if (!imagesList.isEmpty()) {
                images.put(movie.getId(), imagesList.getFirst().getExtension());
            } else {
                images.put(movie.getId(), "default.jpg");
            }
        }
        model.addAttribute("movies", movies);
        model.addAttribute("images", images);
        return "main/index1";
    }

    @PostMapping("/submitReview")
    public String submitReview(@RequestParam("movieId") String  movieId, @RequestParam("rating") String rating, @RequestParam("comment") String comment, Model model) {
        AuthUser user = userService.findByEmail(Objects.requireNonNull(SecurityUtils.getUser()).getUsername()).get();
        reviewService.save(Review.builder().userId(user.getId())
                .movieId(UUID.fromString(movieId))
                .comment(comment)
                .rating(Double.valueOf(rating)).build());
        model.addAttribute("movieId", movieId);
        return "redirect:/movies?movieId=" + movieId;
    }

    @GetMapping("/movies")
    public String getMovieByQuery(@RequestParam("movieId") UUID movieId, Model model) {
        Movie movie = movieService.findById(movieId);
        model.addAttribute("movie", movie);
        List<Image> images = imageService.findByMovieId(movie.getId());
        List<String> paths = images.stream()
                .map(Image::getExtension)
                .toList();
        model.addAttribute("paths", paths);
        List<Show> shows = showService.findByMovieId(movieId);
        model.addAttribute("shows", shows);
        List<Screen> screens = shows.stream()
                .map(show -> screenService.findById(show.getScreenId())).distinct().toList();
        List<Theater> theaters = screens.stream()
                .map(screen -> theaterService.findById(screen.getTheaterId())).distinct().toList();

        HashMap<Theater, List<ShowDTO>> showDTOHashMap = new HashMap<>();
        for (Theater theater : theaters) {
            List<ShowDTO> showDTOList = screens.stream()
                    .filter(screen -> screen.getTheaterId().equals(theater.getId()))
                    .map(screen -> {
                        List<LocalDateTime> showTimes = shows.stream()
                                .filter(show -> show.getScreenId().equals(screen.getId()))
                                .map(Show::getShowTime)
                                .collect(Collectors.toList());

                        ShowDTO showDTO = new ShowDTO();
                        showDTO.setScreen(screen);
                        showDTO.setPrice(showDTO.getPrice());
                        showDTO.setShowTimes(showTimes);
                        return showDTO;
                    })
                    .collect(Collectors.toList());

            showDTOHashMap.put(theater, showDTOList);
        }
        model.addAttribute("showDTOHashMap", showDTOHashMap);
        List<Review> reviewsList = reviewService.findByMovieId(movieId);
        HashMap<AuthUser, Review> reviewHashMap = new HashMap<>();
        for (Review review : reviewsList) {
            reviewHashMap.put(userService.findById(review.getUserId()), review);
        }
        model.addAttribute("reviews", reviewHashMap);
        UUID userId = userService.findByEmail(Objects.requireNonNull(SecurityUtils.getUser()).getUsername()).get().getId();
        model.addAttribute("currUserId", userId);
//        Boolean currentUserCanWriteReview = bookingService.isUserSawMovie(userId, movieId);
        model.addAttribute("currentUserCanWriteReview", true);
        return "main/movie";
    }

    @GetMapping("/showDetails")
    public String getShowDetails(@RequestParam("showId") UUID showId, Model model) {
        Show show = showService.findById(showId);
        Screen screen = screenService.findById(show.getScreenId());
        Theater theater = theaterService.findById(screen.getTheaterId());

        model.addAttribute("show", show);
        model.addAttribute("screen", screen);
        model.addAttribute("theater", theater);
        model.addAttribute("price", show.getPrice());
        model.addAttribute("totalSeats", screen.getTotalSeats());

        List<BookedSeat> bookedSeats = bookedSeatService.findByShowId(showId);
        List<Integer> numbers;
        if (!bookedSeats.isEmpty()) {
            numbers = bookedSeats.stream().map(BookedSeat::getSeatNumber).toList();
        } else {
            numbers = new ArrayList<>();
        }
        model.addAttribute("numbers", numbers);
        return "auth/showDetails";
    }


}
