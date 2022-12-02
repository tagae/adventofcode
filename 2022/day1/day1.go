package day1

import (
	"bufio"
	"os"
	"strconv"
)

// Part 1 solution
func findMostCaloricInventory(inventories [][]int) int {
	max := 0
	for _, inventory := range inventories {
		sum := sumInventory(inventory)
		if sum > max {
			max = sum
		}
	}
	return max
}

func sumInventory(inventory []int) int {
	calories := 0
	for _, item := range inventory {
		calories += item
	}
	return calories
}

func parseInput(filename string) [][]int {
	return mapInput(filename, func(scanner *bufio.Scanner) [][]int {
		var inventories [][]int
		currentInventory := parseInventory(scanner)
		for len(currentInventory) > 0 {
			inventories = append(inventories, currentInventory)
			currentInventory = parseInventory(scanner)
		}
		return inventories
	})
}

func parseInventory(scanner *bufio.Scanner) []int {
	return reduceInput(scanner, make([]int, 0),
		func(line string) bool {
			return len(line) <= 0
		},
		func(inventory []int, line string) []int {
			entry, err := strconv.Atoi(line)
			if err != nil {
				panic(err)
			}
			return append(inventory, entry)
		})
}

func reduceInput[A any](scanner *bufio.Scanner, initValue A, shortCircuit func(string) bool, f func(A, string) A) A {
	accumulator := initValue
	for scanner.Scan() {
		text := scanner.Text()
		if shortCircuit(text) {
			break
		}
		accumulator = f(accumulator, text)
	}
	return accumulator
}

func mapInput[T any](filename string, f func(*bufio.Scanner) T) T {
	file, err := os.Open(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()
	return f(bufio.NewScanner(file))
}
