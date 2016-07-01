package com.github.sandornemeth.experimental;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

public class ListBasedLevenshteinTest {

  @Test
  public void simpleLevenshteinTest() throws Exception {
    List<String> lines = Files.readAllLines(Paths.get("/usr/share/dict/words"));
    ListBasedLevenshtein lev = new ListBasedLevenshtein(lines);

    long start = System.currentTimeMillis();
    Collection<String> results = lev.search("gooper", 1);
    long end = System.currentTimeMillis();
    results.forEach(System.out::println);
    System.out.println(String.format("Took %d ms", end - start));
  }

  @Test
  public void trieTest() throws Exception {
    long start, end;
    Trie trie = new Trie();
    start = System.currentTimeMillis();
    Files.readAllLines(Paths.get("/usr/share/dict/words")).forEach(trie::insert);
    end = System.currentTimeMillis();
    System.out.println(String.format("Read to trie took %d ms", end - start));

    start = System.currentTimeMillis();
    Collection<Trie.Result> results = trie.search("gooper", 2);
    end = System.currentTimeMillis();
    results.stream().map(Trie.Result::toString).forEach(System.out::println);
    System.out.println(String.format("Took %d ms", end - start));
  }
}