package day1

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestSampleInputParsing(t *testing.T) {
	assert.Equal(t, [][]int{
		{1000, 2000, 3000},
		{4000},
		{5000, 6000},
		{7000, 8000, 9000},
		{10000},
	}, parseInput("sample.txt"))
}

func TestMaxCaloriesOnSampleInput(t *testing.T) {
	assert.Equal(t, 24000, findMostCalories(parseInput("sample.txt")))
}

func TestMaxCaloriesOnPart1Input(t *testing.T) {
	assert.Equal(t, 69177, findMostCalories(parseInput("part1.txt")))
}
