package ordina.jworks.security.todo.web.in;

import ordina.jworks.security.todo.exception.RecordNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Maarten Casteels
 * @since 2021
 */
@ControllerAdvice(annotations = Controller.class)
public class ErrorAndExceptionHandlerForMvc {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(RecordNotFoundException.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        logger.error("Request: " + req.getRequestURL() + " raised " + ex);

        final ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
