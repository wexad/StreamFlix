package com.wexad.controller.user;

import com.wexad.domains.bookedSeat.BookedSeat;
import com.wexad.domains.booking.Booking;
import com.wexad.domains.movie.Movie;
import com.wexad.domains.screen.Screen;
import com.wexad.domains.show.Show;
import com.wexad.domains.theater.Theater;
import com.wexad.domains.user.AuthUser;
import com.wexad.security.SecurityUtils;
import com.wexad.service.bookedSeat.BookedSeatService;
import com.wexad.service.booking.BookingService;
import com.wexad.service.movie.MovieService;
import com.wexad.service.screen.ScreenService;
import com.wexad.service.show.ShowService;
import com.wexad.service.theater.TheaterService;
import com.wexad.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Controller("/book-seats")
public class BookController {
    private final BookedSeatService bookedSeatService;
    private final ShowService showService;
    private final BookingService bookingService;
    private final UserService userService;
    private final MovieService movieService;
    private final ScreenService screenService;
    private final TheaterService theaterService;

    public BookController(BookedSeatService bookedSeatService, ShowService showService, BookingService bookingService, UserService userService, MovieService movieService, ScreenService screenService, TheaterService theaterService) {
        this.bookedSeatService = bookedSeatService;
        this.showService = showService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.movieService = movieService;
        this.screenService = screenService;
        this.theaterService = theaterService;
    }

    @PostMapping("book-seats")
    public String bookSeats(@RequestParam("showId") String showId, @RequestParam("selectedSeats") String selectedSeats, Model model) {
        if (selectedSeats.isBlank()) {
            return "redirect:/home";
        }
        String[] seatNumbersArray = selectedSeats.split(",");
        Show show = showService.findById(UUID.fromString(showId));
        Double bookingPrice = show.getPrice();
        double price = bookingPrice * seatNumbersArray.length;
        model.addAttribute("price", price);
        model.addAttribute("selectedSeats", selectedSeats);
        model.addAttribute("showId", showId);
        return "/main/payment";
    }

    @PostMapping("confirm-payment")
    public String confirmPayment(@RequestParam("selectedSeats") String selectedSeats, @RequestParam("showId") String showId, @RequestParam("cardNumber") Long cardNumber, Model model) {
        Integer[] array = Arrays.stream(selectedSeats.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
        for (Integer seatNumber : array) {
            bookedSeatService.save(new BookedSeat(showId, seatNumber));
            String email = Objects.requireNonNull(SecurityUtils.getUser()).getUsername();
            AuthUser currentUser = userService.findByEmail(email).get();
            Booking booking = Booking.builder().
                    userId(currentUser.getId())
                    .showId(UUID.fromString(showId))
                    .cardNumber(cardNumber)
                    .seatNumber(seatNumber)
                    .build();
            bookingService.save(booking);
        }
        Show show = showService.findById(UUID.fromString(showId));
        Movie movie = movieService.findById(show.getMovieId());
        Screen screen = screenService.findById(show.getScreenId());
        Theater theater = theaterService.findById(screen.getTheaterId());
        model.addAttribute("movie", movie);
        model.addAttribute("show", show);
        model.addAttribute("screen", screen);
        model.addAttribute("theater", theater);
        model.addAttribute("price", array.length * show.getPrice());
        model.addAttribute("array", array);
        return "/main/receipt";
    }
}
