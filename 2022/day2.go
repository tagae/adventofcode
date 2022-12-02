package main

import (
	"fmt"
	"strings"
)

const (
	Rock     = "rock"
	Paper    = "paper"
	Scissors = "scissors"
)

type Round struct {
	They string
	Me   string
}

func tournamentScore(tournament []Round) int {
	shapeScore := map[string]int{
		Rock:     1,
		Paper:    2,
		Scissors: 3,
	}
	roundScore := map[Round]int{
		Round{Rock, Rock}:         3,
		Round{Rock, Paper}:        6,
		Round{Rock, Scissors}:     0,
		Round{Paper, Rock}:        0,
		Round{Paper, Paper}:       3,
		Round{Paper, Scissors}:    6,
		Round{Scissors, Rock}:     6,
		Round{Scissors, Paper}:    0,
		Round{Scissors, Scissors}: 3,
	}
	return reduce(tournament, 0, func(total int, round Round) int {
		return total + shapeScore[round.Me] + roundScore[round]
	})
}

func parseStrategyGuide(filename string) []Round {
	moveTranslation := map[string]string{
		"A": Rock,
		"B": Paper,
		"C": Scissors,
		"X": Rock,
		"Y": Paper,
		"Z": Scissors,
	}
	return mapReduceInput(filename, make([]Round, 0),
		func(line string) bool {
			return false
		},
		func(moves []Round, line string) []Round {
			var theirMove, myMove string
			fmt.Fscanf(strings.NewReader(line), "%s %s", &theirMove, &myMove)
			return append(moves, Round{
				They: moveTranslation[theirMove],
				Me:   moveTranslation[myMove],
			})
		})
}
