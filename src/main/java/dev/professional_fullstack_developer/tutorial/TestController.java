package dev.professional_fullstack_developer.tutorial;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(
        path = "/api/v1"
)
@Controller
public class TestController {

    public TestController() {
    }

    // @ResponseBody automatically wraps return type, as if it was ResponseEntity<String>
    @GetMapping(
            path = "/test"
    )
    @ResponseBody
    public Response testHelloWorld(@RequestParam(name = "name", required = false) String name) {

        return new Response("Hello %s!".formatted(name == null || name.isBlank() ? "World" : name));
    }

    public record Response(String message) {
    }

}
