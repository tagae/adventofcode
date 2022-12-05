package main

import (
	"fmt"
	"strings"
)

type SectionRange [2]int

func parseCleaningSectionRanges(filename string) [][2]SectionRange {
	return reduceFileLines(filename, make([][2]SectionRange, 0),
		func(sectionRanges [][2]SectionRange, line string) [][2]SectionRange {
			pairOfRanges := [2]SectionRange{}
			fmt.Fscanf(strings.NewReader(line), "%d-%d,%d-%d",
				&pairOfRanges[0][0], &pairOfRanges[0][1],
				&pairOfRanges[1][0], &pairOfRanges[1][1])
			return append(sectionRanges, pairOfRanges)
		})
}

func containsFully(a, b SectionRange) bool {
	return a[0] <= b[0] && b[1] <= a[1]
}

func findFullyContainedRanges(sectionRanges [][2]SectionRange) []SectionRange {
	return reduce(sectionRanges, make([]SectionRange, 0), func(result []SectionRange, pair [2]SectionRange) []SectionRange {
		if containsFully(pair[0], pair[1]) {
			return append(result, pair[1])
		} else if containsFully(pair[1], pair[0]) {
			return append(result, pair[0])
		} else {
			return result
		}
	})
}
