package main

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
	}, parseInput("day1-sample.txt"))
}

func TestMaxCaloriesOnSample(t *testing.T) {
	assert.Equal(t, 24000, findMostCaloricInventory(parseInput("day1-sample.txt")))
}

func TestMaxCaloriesOnInput(t *testing.T) {
	assert.Equal(t, 69177, findMostCaloricInventory(parseInput("day1-input.txt")))
}

func TestTopThreeCaloriesOnSample(t *testing.T) {
	assert.Equal(t, 45000, findThreeTopInventories(parseInput("day1-sample.txt")))
}

func TestTopThreeCaloriesOnInput(t *testing.T) {
	assert.Equal(t, 207456, findThreeTopInventories(parseInput("day1-input.txt")))
}
