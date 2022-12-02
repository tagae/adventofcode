package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestStrategyGuideParsing(t *testing.T) {
	expected := []Round{
		{Rock, Paper},
		{Paper, Rock},
		{Scissors, Scissors},
	}
	assert.Equal(t, expected, parseStrategyGuide("day2_sample.txt"))
}

func TestTournamentScore(t *testing.T) {
	testCases := []struct {
		filename string
		score    int
	}{
		{filename: "day2_sample.txt", score: 15},
		{filename: "day2_input.txt", score: 12679},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.score, tournamentScore(parseStrategyGuide(testCase.filename)))
		})
	}
}
