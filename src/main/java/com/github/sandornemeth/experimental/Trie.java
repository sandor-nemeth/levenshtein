package com.github.sandornemeth.experimental;

import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

class Trie {

  private final Node root;

  Trie() {
    root = new Node();
  }

  void insert(String word) {
    root.insert(root, word);
  }

  Collection<Result> search(String word, int cost) {
    int[] currentRow = IntStream.rangeClosed(0, word.length()).toArray();
    List<Result> results = new ArrayList<>();
    for (Integer letter : root.children.keySet()) {
      searchRecursive(root.children.get(letter), letter, currentRow, results, word, cost);
    }
    return results;
  }

  void searchRecursive(Node node, int letter, int[] previousRow, List<Result> results,
      String word, int cost) {
    char[] warr = word.toCharArray();
    int columns = word.length() + 1;
    int[] currentRow = new int[columns];
    currentRow[0] = previousRow[0] + 1;

    for (int column = 1; column < columns; column++) {
      int insertCost = currentRow[column - 1] + 1;
      int deleteCost = previousRow[column] + 1;
      int replaceCost =
          warr[column - 1] != letter ? previousRow[column - 1] + 1 : previousRow[column - 1];
      currentRow[column] = Math.min(insertCost, Math.min(deleteCost, replaceCost));
    }

    if (currentRow[currentRow.length - 1] <= cost && null != node.word) {
      results.add(new Result(node.word, currentRow[currentRow.length - 1]));
    }

    if (Ints.min(currentRow) <= cost) {
      for (Integer let : node.children.keySet()) {
        searchRecursive(node.children.get(let), let, currentRow, results, word, cost);
      }
    }
  }

  static class Node {
    private final Map<Integer, Node> children;
    private String word;

    Node() {
      word = null;
      children = new HashMap<>();
    }

    void insert(Node self, String word) {
      Node node = self;
      char[] chars = word.toCharArray();
      for (char ch : chars) {
        node = node.children.computeIfAbsent((int) ch, (key) -> new Node());
      }
      node.word = word;
    }
  }

  static class Result {
    String word;
    int cost;

    Result(String word, int cost) {
      this.word = word;
      this.cost = cost;
    }

    public String getWord() {
      return word;
    }

    public int getCost() {
      return cost;
    }

    @Override
    public String toString() {
      return String.format("[word: %s, cost: %d]", word, cost);
    }
  }
}
