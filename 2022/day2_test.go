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
	assert.Equal(t, expected, parseStrategyGuide("day2-sample.txt"))
}

func TestTournamentScoreOnSample(t *testing.T) {
	assert.Equal(t, 15, tournamentScore(parseStrategyGuide("day2-sample.txt")))
}

func TestTournamentScoreOnInput(t *testing.T) {
	assert.Equal(t, 12679, tournamentScore(parseStrategyGuide("day2-input.txt")))
}
