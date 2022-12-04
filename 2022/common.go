package main

import (
	"bufio"
	"os"
)

func mapInput[T any](filename string, f func(*bufio.Scanner) T) T {
	file, err := os.Open(filename)
	if err != nil {
		panic(err)
	}
	defer file.Close()
	return f(bufio.NewScanner(file))
}

func reduce[A any, T any](array []T, initValue A, f func(A, T) A) A {
	accumulator := initValue
	for _, item := range array {
		accumulator = f(accumulator, item)
	}
	return accumulator
}

func reduceInputWithShortCircuit[A any](scanner *bufio.Scanner, initValue A, shortCircuit func(string) bool, f func(A, string) A) A {
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

func mapReduceInputWithShortCircuit[A any](filename string, initValue A, shortCircuit func(string) bool, m func(A, string) A) A {
	return mapInput(filename, func(scanner *bufio.Scanner) A {
		return reduceInputWithShortCircuit(scanner, initValue, shortCircuit, m)
	})
}

func mapReduceInput[A any](filename string, initValue A, m func(A, string) A) A {
	neverShortCircuit := func(s string) bool { return false }
	return mapReduceInputWithShortCircuit(filename, initValue, neverShortCircuit, m)
}
