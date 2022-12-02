package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestStrategyGuideParsingPart1(t *testing.T) {
	expected := []Round{
		{Rock, Paper},
		{Paper, Rock},
		{Scissors, Scissors},
	}
	assert.Equal(t, expected, parseStrategyGuidePart1("day2_sample.txt"))
}

func TestStrategyGuideParsingPart2(t *testing.T) {
	expected := []RoundHint{
		{Rock, Draw},
		{Paper, Lose},
		{Scissors, Win},
	}
	assert.Equal(t, expected, parseStrategyGuidePart2("day2_sample.txt"))
}

func TestTournamentScorePart1(t *testing.T) {
	testCases := []struct {
		filename string
		score    int
	}{
		{filename: "day2_sample.txt", score: 15},
		{filename: "day2_input.txt", score: 12679},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.score, tournamentScorePart1(parseStrategyGuidePart1(testCase.filename)))
		})
	}
}

func TestTournamentScorePart2(t *testing.T) {
	testCases := []struct {
		filename string
		score    int
	}{
		{filename: "day2_sample.txt", score: 12},
		{filename: "day2_input.txt", score: 14470},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.score, tournamentScorePart2(parseStrategyGuidePart2(testCase.filename)))
		})
	}
}
