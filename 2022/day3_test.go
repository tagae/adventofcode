package main

import (
	"fmt"
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestRepeatedRucksackItem(t *testing.T) {
	testCases := []struct {
		items    string
		repeated int32
	}{
		{items: "vJrwpWtwJgWrhcsFMMfFFhFp", repeated: 'p'},
		{items: "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL", repeated: 'L'},
		{items: "PmmdzqPrVvPwwTWBwg", repeated: 'P'},
		{items: "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", repeated: 'v'},
		{items: "ttgJtRGJQctTZtZT", repeated: 't'},
		{items: "CrZsJsPPZsGzwwsLwLmpwMDw", repeated: 's'},
	}
	for _, testCase := range testCases {
		t.Run(testCase.items, func(t *testing.T) {
			assert.Equal(t, testCase.repeated, repeatedRucksackItem(testCase.items))
		})
	}
}

func TestRucksackItemPriority(t *testing.T) {
	testCases := []struct {
		item     int32
		priority int
	}{
		{item: 'a', priority: 1},
		{item: 'z', priority: 26},
		{item: 'A', priority: 27},
		{item: 'Z', priority: 52},
	}
	for _, testCase := range testCases {
		t.Run(fmt.Sprintf("Item %c", testCase.item), func(t *testing.T) {
			assert.Equal(t, testCase.priority, rucksackItemPriority(testCase.item))
		})
	}
}

func TestRucksackItemPrioritySum(t *testing.T) {
	testCases := []struct {
		filename    string
		prioritySum int
	}{
		{filename: "day3_sample.txt", prioritySum: 157},
		{filename: "day3_input.txt", prioritySum: 7597},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.prioritySum, prioritySumFromFile(testCase.filename))
		})
	}
}
