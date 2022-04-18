package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StockController {

	@RequestMapping("/")
    public String home(){
        return "I represent a working controller endpoint that can be run on localhost";
    }
}
