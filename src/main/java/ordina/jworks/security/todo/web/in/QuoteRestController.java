package ordina.jworks.security.todo.web.in;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/quote")
public class QuoteRestController {

    private final WebClient webClient;

    public QuoteRestController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping
    public Quote getQuote(){
        return webClient.get()
                        .uri("http://localhost:8888/api/quotes/random")
                        .retrieve()
                        .bodyToMono(Quote.class)
                        .block();
    }

    static class Quote {
        private final long id;
        private final String text;

        @JsonCreator
        Quote(@JsonProperty("id") long id,
              @JsonProperty("text") String text) {
            this.id = id;
            this.text = text;
        }

        public long getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }

}
