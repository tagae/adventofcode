package main

import (
	"github.com/stretchr/testify/assert"
	"testing"
)

func TestSectionParsing(t *testing.T) {
	testCases := []struct {
		filename string
		sections [][2]SectionRange
	}{
		{filename: "day4_sample.txt",
			sections: [][2]SectionRange{
				{{2, 4}, {6, 8}},
				{{2, 3}, {4, 5}},
				{{5, 7}, {7, 9}},
				{{2, 8}, {3, 7}},
				{{6, 6}, {4, 6}},
				{{2, 6}, {4, 8}},
			}},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.sections, parseCleaningSectionRanges(testCase.filename))
		})
	}
}

func TestFindFullyContainedRanges(t *testing.T) {
	testCases := []struct {
		filename string
		count    int
	}{
		{filename: "day4_sample.txt", count: 2},
		{filename: "day4_input.txt", count: 651},
	}
	for _, testCase := range testCases {
		t.Run(testCase.filename, func(t *testing.T) {
			assert.Equal(t, testCase.count,
				len(findFullyContainedRanges(parseCleaningSectionRanges(testCase.filename))))
		})
	}
}
