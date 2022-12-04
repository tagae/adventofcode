package main

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

func prioritySumFromFile(filename string) int {
	return mapReduceInput(filename, 0, func(total int, rucksackItems string) int {
		return total + rucksackItemPriority(repeatedRucksackItem(rucksackItems))
	})
}
