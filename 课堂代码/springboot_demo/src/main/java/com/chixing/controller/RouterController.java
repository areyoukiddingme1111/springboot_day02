package com.chixing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class RouterController {

    @RequestMapping("index")
    public String index(){
        System.out.println("index....");
        return "index";
    }

    @RequestMapping("noteall")
    public String noteall(){
        System.out.println("index....");
        return "note/note_all";
    }
}
