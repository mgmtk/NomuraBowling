package com.nomura.bowling.app.services;

import com.nomura.bowling.app.dao.UserPromptDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javafx.util.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class BowlingScoreService {

    private Map<Integer,Pair<Integer, Integer>> scores;

    private static final int FINAL_FRAME_NUM = 10;
    private static final int MAX_BOWLING_VALUE = 10;
    private static final int MIN_BOWLING_VALUE = 0;

    @Autowired
    UserPromptDao promptDao;

    public BowlingScoreService() {
        scores = new LinkedHashMap<>();
    }

    public void setScores(Map<Integer,Pair<Integer, Integer>> scores){
        this.scores = scores;
    }

    public void bowlFrame(int frame) {
        System.out.println("\nBeginning of Frame: " + frame);

        int roll1 = promptDao.getUserRoll();
        System.out.println("First Roll: " + roll1);
        if(roll1 == MAX_BOWLING_VALUE) {
            scores.put(frame, new Pair<>(roll1, 0));
            System.out.println("Nice Strike!");
            System.out.println("End of Frame: " + frame + "\n");
            return;
        }

        int roll2 = promptDao.getUserRoll(MAX_BOWLING_VALUE - roll1);
        int frameScore = roll1 + roll2;
        System.out.println("Second Roll: " + roll2);

        if(frameScore == MAX_BOWLING_VALUE) {
            scores.put(frame, new Pair<>(roll1, roll2));
            System.out.println("Nice Spare!");
            System.out.println("End of Frame: " + frame + "\n");
            return;
        }

        scores.put(frame, new Pair<>(roll1, roll2));
        System.out.println("Frame Score: " + frameScore);
        System.out.println("End of Frame: " + frame + "\n");

    }

    public int calculateScoreCard(){
        int acc = 0;
        System.out.printf("Score Card%n");
        for(int k: scores.keySet()) {
            Pair<Integer, Integer> framePair = scores.get(k);

            System.out.printf("\t Frame: %d%n",k);
            System.out.print("\t Frame Score: ");

            if(framePair.getKey() == MAX_BOWLING_VALUE && framePair.getValue() == MIN_BOWLING_VALUE) {
                System.out.println("X");
                acc += calculateStrikeBonus(k);
            }
            else if(framePair.getKey() + framePair.getValue() == MAX_BOWLING_VALUE) {
                System.out.printf("%d /%n", framePair.getKey());
                acc += calculateSpareBonus(k);
            }
            else {
                System.out.println(framePair.getKey() + " " + framePair.getValue());
            }
            acc += framePair.getKey() + framePair.getValue();
            System.out.printf("\t Total Score: %d%n", acc);
        }
        return acc;

    }

    public void handleFinalFrame(){
            int finalFrameRoll1 = scores.get(FINAL_FRAME_NUM).getKey();
            int finalFrameRoll2 = scores.get(FINAL_FRAME_NUM).getValue();

            if(finalFrameRoll1 == MAX_BOWLING_VALUE){
                System.out.println("You got a strike in the final frame, please roll two extra balls");
                int roll1 = promptDao.getUserRoll();
                int roll2 = promptDao.getUserRoll();
                scores.replace(FINAL_FRAME_NUM, new Pair<>(MAX_BOWLING_VALUE, roll1 + roll2));
            }
            else if(finalFrameRoll1 + finalFrameRoll2 == MAX_BOWLING_VALUE){
                System.out.println("You got a spare in the final frame, please roll two extra balls");
                int roll = promptDao.getUserRoll();
                scores.replace(FINAL_FRAME_NUM, new Pair<>(finalFrameRoll1, finalFrameRoll2 + roll));
            }
    }

    private int calculateStrikeBonus(int k) {
        if(this.scores.containsKey(k+1)) {
            if(this.scores.get(k+1).getKey() != MAX_BOWLING_VALUE)
                return this.scores.get(k+1).getKey() + this.scores.get(k+1).getValue();
            else if(this.scores.containsKey(k+2))
                return this.scores.get(k+1).getKey() + this.scores.get(k+2).getKey();
            else if(this.scores.get(k+1).getKey() >= MAX_BOWLING_VALUE &&
                    this.scores.get(k+1).getKey() >= MAX_BOWLING_VALUE)
                return 10 + 10; //Edge case for strikes of final frame
        }
        System.out.println("\t Strike bonus points will be added next frame");
        return 0;
    }

    private int calculateSpareBonus(int k){
        if(this.scores.containsKey(k+1)) {
            return this.scores.get(k+1).getKey();
        }
        System.out.println("\t Spare bonus points will be added next frame");
        return 0;
    }


}
