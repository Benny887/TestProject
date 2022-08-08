package com.example.sweater.controller;

import com.example.sweater.domain.Animals;
import com.example.sweater.domain.User;
import com.example.sweater.exception_handling.NoSuchAnimalException;
import com.example.sweater.repos.AnimalsRepo;
import com.example.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private AnimalsRepo animalsRepo;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
                           Map<String, Object> model) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("show")
    public String showAll(Map<String, Object> model) {
        Iterable<Animals> animals = animalsRepo.findAll();
        model.put("animals", animals);
        return "main";
    }

    @PostMapping("add")
    public String add(@AuthenticationPrincipal User user, @RequestParam String animalType, @RequestParam String birthDate,
                      @RequestParam String sex, @RequestParam String nickName) throws ParseException {
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = oldDateFormat.parse(birthDate);
        Animals animal = new Animals(animalType, date, sex, nickName, user);
        animalsRepo.save(animal);
        return "main";
    }

    @PostMapping("filterByMaster")
    public String filterByMaster(@AuthenticationPrincipal User user, Map<String, Object> model) {
        List<Animals> animals = animalsRepo.findByMaster(user);
        if (animals.size() != 0) {
            model.put("animals", animals);
        } else {
            throw new NoSuchAnimalException("There is no user's " + user.getUsername() + " animal in database");
        }
        return "main";
    }

    @PostMapping("filterById")
    public String filterById(@RequestParam String filterById, Map<String, Object> model) {
        Animals details = animalsRepo.findById(Integer.parseInt(filterById));
        if(details != null){
            model.put("details", details);
        } else {
            throw new NoSuchAnimalException("There is no animal with ID " + filterById + " in database");
        }
        return "main";
    }

    @PostMapping("mody")
    public String modify(@RequestParam int id, @RequestParam String animalType, @RequestParam String birthDate,
                         @RequestParam String sex, @RequestParam String nickName) throws ParseException {
        Animals animals = animalsRepo.findById(id);
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = oldDateFormat.parse(birthDate);
        if (animals != null) {
            animals.setAnimalType(animalType);
            animals.setBirthDate(date);
            animals.setSex(sex);
            animals.setNickName(nickName);
            animalsRepo.save(animals);
        } else {
            throw new NoSuchAnimalException("There is no animal with ID " + id + " in database");
        }
        return "main";
    }

    @PostMapping("del")
    public String remove(@RequestParam String id){
        Animals animals= animalsRepo.findById(Integer.parseInt(id));;
        if(!id.isEmpty() && animals != null) {
            animalsRepo.delete(animals);
        } else {
            throw new NoSuchAnimalException("There is no animal with ID " + id + " in database");
        }
        return "main";
    }

}

