package com.crossover.trial.weather.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class TestController {

    @GET
    @Path("/test")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @GET
    @Path("/test1")
    public String test1() {
        return "Test1";
    }
    
    @GET
    @Path("/test2")
    public String test2() {
        return "Test2";
    }
}
