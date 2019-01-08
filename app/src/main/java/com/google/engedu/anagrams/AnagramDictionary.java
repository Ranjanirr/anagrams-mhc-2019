/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static final String TAG = "AnagramDictionary";
    private Set<String> wordSet = new HashSet<>();//Set of all dictionary words
    private Map<String, List<String>> lettersToWord = new HashMap<>();//maps ordered version to all its anagrams
    private Map<Integer, ArrayList<String>> sizeToWords= new HashMap<>();//dictionary words of key's length
    private int wordLength = DEFAULT_WORD_LENGTH;//for the pick good word
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordSet.add(word);

            // add to hash map - sorted word to its possible dictionary words
            String sortedWord = sortLetters(word);
            if(!lettersToWord.containsKey(sortedWord)) {

                // put it in the map
                lettersToWord.put(sortedWord, new ArrayList<String>());

            }

            // update the list right here in the map
            lettersToWord.get(sortedWord).add(word);
            int length = word.length();
            if (!sizeToWords.containsKey(length)){
                sizeToWords.put(length, new ArrayList<String>() );
            }

                sizeToWords.get(length).add(word);

        }

    }
    /*returns if word is in dictionary and if the base is not in it*/
    public boolean isGoodWord(String word, String base) {

        return wordSet.contains(word) && !word.toLowerCase().contains(base);

    }
    /*Makes the string into a new one with the letters in alphabetical order*/
    public String sortLetters (String word) {

        char[] wordArray = word.toCharArray();
        Arrays.sort(wordArray);
        return new String(wordArray);

    }
    //Gets anagrams of the word entered (checks if each dictionary word is an anagram of the word)
    public List<String> getAnagrams(String targetWord) {

        ArrayList<String> result = new ArrayList<String>();

        String targetWordSorted = sortLetters(targetWord);

        for (String word : wordSet) {
            String wordSorted = sortLetters(word);
            if (wordSorted.equals(targetWordSorted)) {
                result.add(word);
            }
        }

        return result;
    }
    //finds all the anagrams of the word and the alphabet letters
    public List<String> getAnagramsWithOneMoreLetter(String word) {

        ArrayList<String> result = new ArrayList<String>();

        //for loop from a to z
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {

            // add a letter to the original word
            String temp = word + alphabet;

            // sort this new word
            temp = sortLetters(temp);

            if(lettersToWord.containsKey(temp)) {

                // get the list of values and add it to the result list
                ArrayList<String> tempList = new ArrayList<String>();
                tempList = (ArrayList<String>)lettersToWord.get(temp);

                result.addAll(tempList);
//                for (int i = 0; i<tempList.size(); i++) {
////                    result.add(tempList.get(i));
////                }

            }
        }
        // add a (etc) to the word
        // sortLetters(newWord)
        // check if the newWord is in the hashmap, and and then loop through its value list and add all its values to the result list



        return result;

    }

    public List<String> getAnagramsWithTwoMoreLetters(String word) {

        ArrayList<String> result = new ArrayList<String>();

        //for loop from a to z
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {

            // add a letter to the original word
            String temp = word + alphabet;


            // sort this new word
            temp = sortLetters(temp);

            if(lettersToWord.containsKey(temp)) {

                // get the list of values and add it to the result list
                ArrayList<String> tempList = new ArrayList<String>();
                tempList = (ArrayList<String>)lettersToWord.get(temp);

                result.addAll(tempList);
//                for (int i = 0; i<tempList.size(); i++) {
////                    result.add(tempList.get(i));
////                }

            }
        }
        // add a (etc) to the word
        // sortLetters(newWord)
        // check if the newWord is in the hashmap, and and then loop through its value list and add all its values to the result list



        return result;

    }

    public String pickGoodStarterWord() {
        // Log.i(TAG, "sortLetters(post) = " + sortLetters("post"));
        // Log.i(TAG, "getAnagrams(dog) test; " + getAnagrams("dog"));
        // Log.i(TAG, "lettersToWord test; " + lettersToWord.get("opst"));
        Log.i(TAG, "isGoodWord test for nonstop and post: " + isGoodWord("nonstop", "post"));
        Log.i(TAG, "get anagrams with one more letter for sam: " + getAnagramsWithOneMoreLetter("sam"));
        Log.i(TAG, "get five letter words: " + sizeToWords.get(5));
        // select random word from dictionary
        Random rand = new Random();
        List<String> wordsOfLength = sizeToWords.get(wordLength);//gets all the words of that specific length
        int index = rand.nextInt(wordsOfLength.size());//finds a random place
        // iterate until find a word that has at least MIN_NUM_ANAGRAMS anagrams
        int numAnagrams = 0;//
        String currentWord =  wordsOfLength.get(index);//random place in the word set of length
        while (numAnagrams < MIN_NUM_ANAGRAMS) {//makes sure the word has five anagrams at least
            index++;
            if (index == wordsOfLength.size()) {
                index = 0;
            }


            currentWord =  wordsOfLength.get(index);
            numAnagrams =getAnagramsWithOneMoreLetter(currentWord).size();//the size of the returned value of get anagrams


        }
        wordLength ++;
        return currentWord;

    }
}
