package main

import (
	"fmt"
	"strings"
)

const (
	Rock     = "rock"
	Paper    = "paper"
	Scissors = "scissors"
	Win      = 6
	Draw     = 3
	Lose     = 0
)

var theirMoveTranslation = map[string]string{
	"A": Rock,
	"B": Paper,
	"C": Scissors,
}
var myMoveTranslation = map[string]string{
	"X": Rock,
	"Y": Paper,
	"Z": Scissors,
}

var resultTranslation = map[string]int{
	"X": Lose,
	"Y": Draw,
	"Z": Win,
}

var shapeScore = map[string]int{
	Rock:     1,
	Paper:    2,
	Scissors: 3,
}

type Round struct {
	They string
	Me   string
}

type RoundHint struct {
	They   string
	Result int
}

// Part 1 solution
func tournamentScorePart1(tournament []Round) int {
	roundScore := map[Round]int{
		Round{Rock, Rock}:         Draw,
		Round{Rock, Paper}:        Win,
		Round{Rock, Scissors}:     Lose,
		Round{Paper, Rock}:        Lose,
		Round{Paper, Paper}:       Draw,
		Round{Paper, Scissors}:    Win,
		Round{Scissors, Rock}:     Win,
		Round{Scissors, Paper}:    Lose,
		Round{Scissors, Scissors}: Draw,
	}
	return reduce(tournament, 0, func(total int, round Round) int {
		return total + shapeScore[round.Me] + roundScore[round]
	})
}

// Part 2 solution
func tournamentScorePart2(tournament []RoundHint) int {
	myNeededMove := map[RoundHint]string{
		RoundHint{Rock, Win}:      Paper,
		RoundHint{Rock, Draw}:     Rock,
		RoundHint{Rock, Lose}:     Scissors,
		RoundHint{Paper, Win}:     Scissors,
		RoundHint{Paper, Draw}:    Paper,
		RoundHint{Paper, Lose}:    Rock,
		RoundHint{Scissors, Win}:  Rock,
		RoundHint{Scissors, Draw}: Scissors,
		RoundHint{Scissors, Lose}: Paper,
	}
	return reduce(tournament, 0, func(total int, hint RoundHint) int {
		return total + shapeScore[myNeededMove[hint]] + hint.Result
	})
}

func parseStrategyGuidePart1(filename string) []Round {
	return parseStrategyGuide(filename, func(theirMoveCode string, myMoveCode string) Round {
		return Round{
			They: theirMoveTranslation[theirMoveCode],
			Me:   myMoveTranslation[myMoveCode],
		}
	})
}

func parseStrategyGuidePart2(filename string) []RoundHint {
	return parseStrategyGuide(filename, func(theirMoveCode string, resultCode string) RoundHint {
		return RoundHint{
			They:   theirMoveTranslation[theirMoveCode],
			Result: resultTranslation[resultCode],
		}
	})
}

func parseStrategyGuide[T any](filename string, move func(theirMove string, myMove string) T) []T {
	return reduceFileLines(filename, make([]T, 0),
		func(moves []T, line string) []T {
			var theirMove, myMove string
			fmt.Fscanf(strings.NewReader(line), "%s %s", &theirMove, &myMove)
			return append(moves, move(theirMove, myMove))
		})
}
