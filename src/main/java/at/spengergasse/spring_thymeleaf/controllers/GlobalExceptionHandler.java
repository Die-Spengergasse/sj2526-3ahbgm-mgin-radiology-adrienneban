package at.spengergasse.spring_thymeleaf.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseError(DataAccessException ex, Model model) {
        model.addAttribute("errorMessage",
                "The database is currently unavailable or a database error has occurred.");
        model.addAttribute("technicalDetail", ex.getMostSpecificCause().getMessage());
        return "db_error";
    }

    @ExceptionHandler({SQLException.class,
            java.net.ConnectException.class,
            org.springframework.jdbc.CannotGetJdbcConnectionException.class})
    public ModelAndView handleConnectionError(Exception ex, Model model) {
        ModelAndView mav = new ModelAndView("db_error");
        mav.addObject("errorMessage",
                "The database is currently unavailable or a database error has occurred.");
        mav.addObject("technicalDetail", ex.getMessage());
        return mav;
    }
}
