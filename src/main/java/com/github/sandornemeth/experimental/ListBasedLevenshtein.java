package com.github.sandornemeth.experimental;

import java.util.Collection;
import java.util.Collections;

import static java.util.stream.Collectors.toList;

/**
 * List based levenshtein implementation.
 */
class ListBasedLevenshtein {

  private Collection<String> words;

  ListBasedLevenshtein(Collection<String> words) {
    this.words = Collections.unmodifiableCollection(words);
  }

  int distance(String word1, String word2) {
    char[] w1 = word1.toCharArray();
    char[] w2 = word2.toCharArray();
    int columns = w1.length + 1;
    int rows = w2.length + 1;

    // build first row
    int[] currentRow = new int[columns];
    currentRow[0] = 0;
    for (int i = 1; i < columns; i++) {
      currentRow[i] = currentRow[i - 1] + 1;
    }

    int[] previousRow;
    for (int row = 1; row < rows; row++) {
      previousRow = currentRow;
      currentRow = new int[columns];
      currentRow[0] = previousRow[0] + 1;
      for (int col = 1; col < columns; col++) {
        int insertCost = currentRow[col - 1] + 1;
        int deleteCost = previousRow[col] + 1;
        int replaceCost =
            w1[col - 1] != w2[row - 1] ? previousRow[col - 1] + 1 : previousRow[col - 1];
        currentRow[col] = Math.min(insertCost, Math.min(deleteCost, replaceCost));
      }
    }
    return currentRow[currentRow.length - 1];
  }

  Collection<String> search(String word, int cost) {
    return words.stream().filter(w -> distance(w, word) <= cost).collect(toList());
  }
}
