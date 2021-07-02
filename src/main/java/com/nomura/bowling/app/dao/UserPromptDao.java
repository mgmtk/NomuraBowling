package com.nomura.bowling.app.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Scanner;

@Repository
public class UserPromptDao {

    private static final Logger LOG = LoggerFactory.getLogger(UserPromptDao.class);

    private final Scanner scanner;

    private static final int MAX_BOWLING_VALUE = 10;
    private static final int MIN_BOWLING_VALUE = 0;

    public UserPromptDao() {
        this.scanner = new Scanner(System.in);
    }

    public int getUserRoll(int pinMax) {
        return this.promptUser(pinMax);
    }

    public int getUserRoll() {
        return this.promptUser(MAX_BOWLING_VALUE);
    }

    private int promptUser(int pinMax){
        System.out.println("Enter Roll");
        while(true)
            try {
                int roll = Integer.parseInt(scanner.next());
                if(roll >= MIN_BOWLING_VALUE && roll <= pinMax)
                    return roll;
                throw new NumberFormatException(String.format("Please enter an integer between [0-%d]", pinMax));

            } catch(NumberFormatException e){
                LOG.error("Input exception occurred {}", e.getMessage());
                LOG.error("Please enter your roll again");
            }
    }
}
