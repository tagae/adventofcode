package main

import "bufio"

func repeatedRucksackItem(items string) int32 {
	firstHalf := items[:len(items)/2]
	secondHalf := items[len(items)/2:]
	set := make(map[int32]bool)
	for _, chr := range firstHalf {
		set[chr] = true
	}
	for _, chr := range secondHalf {
		if set[chr] {
			return chr
		}
	}
	return 0
}

func rucksackItemPriority(item int32) int {
	if item >= 'a' {
		return int(item - 'a' + 1)
	} else {
		return int(item - 'A' + 27)
	}
}

func repeatedRucksackItemPrioritySumFromFile(filename string) int {
	return reduceFileLines(filename, 0, func(total int, rucksackItems string) int {
		return total + rucksackItemPriority(repeatedRucksackItem(rucksackItems))
	})
}

func commonItemPrioritySumFromFile(filename string) int {
	return scanFile(filename, func(scanner *bufio.Scanner) int {
		groupLines := [3]string{}
		index := 0
		total := 0
		for scanner.Scan() {
			groupLines[index] = scanner.Text()
			if index == 2 {
				total = total + rucksackItemPriority(commonRucksackGroupItem(groupLines))
				index = 0
			} else {
				index = index + 1
			}
		}
		return total
	})
}

func commonRucksackGroupItem(rucksackGroup [3]string) int32 {
	itemCount := make(map[int32]int)
	for _, rucksack := range rucksackGroup {
		foundRucksackItems := make(map[int32]bool)
		for _, item := range rucksack {
			if !foundRucksackItems[item] {
				foundRucksackItems[item] = true
				itemCount[item] = itemCount[item] + 1
			}
		}
	}
	for item, count := range itemCount {
		if count == 3 {
			return item
		}
	}
	return 0
}
