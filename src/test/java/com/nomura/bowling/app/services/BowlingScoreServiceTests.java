package com.nomura.bowling.app.services;

import com.nomura.bowling.app.dao.UserPromptDao;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class BowlingScoreServiceTests {

    @InjectMocks
    BowlingScoreService bowlingScoreService;

    @Mock
    UserPromptDao promptDao;

    @Test
    void testBowlFrame(){
        when(promptDao.getUserRoll())
                .thenReturn(5);

        when(promptDao.getUserRoll(5))
                .thenReturn(4);

        bowlingScoreService.bowlFrame(1);
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(9);

        verify(promptDao).getUserRoll();
        verify(promptDao).getUserRoll(5);
    }

    @Test
    void testHandleFinalFrameWithStrike() {
        Map<Integer, Pair<Integer, Integer>> mockScores = new LinkedHashMap<>() {{
            put(10, new Pair<>(10,0));
        }};

        when(promptDao.getUserRoll())
                .thenReturn(5)
                .thenReturn(4);

        bowlingScoreService.setScores(mockScores);
        bowlingScoreService.handleFinalFrame();
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(19);

        verify(promptDao, times(2)).getUserRoll();
    }

    @Test
    void testHandleFinalFrameWithSpare() {
        Map<Integer, Pair<Integer, Integer>> mockScores = new LinkedHashMap<>() {{
            put(10, new Pair<>(5,5));
        }};

        when(promptDao.getUserRoll())
                .thenReturn(10);

        bowlingScoreService.setScores(mockScores);
        bowlingScoreService.handleFinalFrame();
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(20);

        verify(promptDao, times(1)).getUserRoll();
    }

    @Test
    void testHandleFinalFrameWithAllStrikes() {
        Map<Integer, Pair<Integer, Integer>> mockScores = new LinkedHashMap<>() {{
            put(10, new Pair<>(10,0));
        }};

        when(promptDao.getUserRoll())
                .thenReturn(10)
                .thenReturn(10);

        bowlingScoreService.setScores(mockScores);
        bowlingScoreService.handleFinalFrame();
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(30);

        verify(promptDao, times(2)).getUserRoll();
    }

    @Test
    void testPrintScoreCard() {
        Map<Integer, Pair<Integer, Integer>> mockScores = new LinkedHashMap<>() {{
            put(1, new Pair<>(6, 2));
            put(2, new Pair<>(7,2));
            put(3, new Pair<>(3,4));
            put(4, new Pair<>(8,2));
            put(5, new Pair<>(9,0));
            put(6, new Pair<>(10,0));
            put(7, new Pair<>(10,0));
            put(8, new Pair<>(10,0));
            put(9, new Pair<>(6,3));
            put(10, new Pair<>(8,9));
        }};

        bowlingScoreService.setScores(mockScores);
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(153);
    }

    @Test
    void testPerfectGame() {
        Map<Integer, Pair<Integer, Integer>> mockScores = new LinkedHashMap<>() {{
            put(1, new Pair<>(10, 0));
            put(2, new Pair<>(10,0));
            put(3, new Pair<>(10,0));
            put(4, new Pair<>(10,0));
            put(5, new Pair<>(10,0));
            put(6, new Pair<>(10,0));
            put(7, new Pair<>(10,0));
            put(8, new Pair<>(10,0));
            put(9, new Pair<>(10,0));
            put(10, new Pair<>(10,0));
        }};

        when(promptDao.getUserRoll())
                .thenReturn(10)
                .thenReturn(10);

        bowlingScoreService.setScores(mockScores);
        bowlingScoreService.handleFinalFrame();
        assertThat(bowlingScoreService.calculateScoreCard()).isEqualTo(300);

        verify(promptDao, times(2)).getUserRoll();
    }

}
