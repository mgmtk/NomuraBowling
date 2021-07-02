package com.nomura.bowling.app;

import com.nomura.bowling.app.services.BowlingScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@Profile("development")
public class BowlingGameController {

    private int frame = 0;

    @Autowired
    private BowlingScoreService bowlingScoreService;

    @PostConstruct
    public void startGame(){
        System.out.println("Welcoming to bowling, follow the prompts as they appear.");
        int score = 0;
        while(frame++ < 10) {
            bowlingScoreService.bowlFrame(frame);
            if(frame == 10){
                bowlingScoreService.handleFinalFrame();
            }
            score = bowlingScoreService.calculateScoreCard();
        }
        System.out.println("Thank you for playing bowling!");
        System.out.printf("Your final score is: %d%n", score);
    }
}
