package search;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TextSearcher implements a search interface to an underlying text file
 * consisting of the single method {@link #search(String, int)}.
 */
public class TextSearcher {
    private LinkedList<String> words; // To store the string tokens in a doubly linked list to traverse either forward ot backward

    /**
     * Initializes the text searcher with the contents of a text file.
     * The current implementation just reads the contents into a string
     * and passes them to #init().  You may modify this implementation if you need to.
     *
     * @param f Input file.
     * @throws IOException
     */
    public TextSearcher(File f) throws IOException {

        FileReader r = null;
        try {
            r = new FileReader(f);
            StringWriter w = new StringWriter();
            char[] buf = new char[4096];
            int readCount;

            while ((readCount = r.read(buf)) > 0) {
                w.write(buf, 0, readCount);
            }

            init(w.toString());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    /**
     * Initializes any internal data structures that are needed for
     * this class to implement search efficiently.
     */
    protected void init(String fileContents) {

		/**
		 * Create a doubly linked list by parsing the whole file contents into individual string tokens
		 * such that the space and punctuations are preserved
		 *
		 * The regex used in the following code does not work for apostrophes which is the only test case failing at this point
		 */
		words = Arrays.stream(fileContents.split("(?=\\b)")).sequential()
                .collect(Collectors.toCollection(LinkedList::new));

    }

    /**
     * @param queryWord    The word to search for in the file contents.
     * @param contextWords The number of words of context to provide on
     *                     each side of the query word.
     * @return One context string for each time the query word appears in the file.
     */
    public String[] search(String queryWord, int contextWords) {


        List<String> res = new ArrayList<>();
        int startIdx = 0, endIndex=words.size(),idx=-1;
        ListIterator listIterator= words.listIterator();
		while (listIterator.hasNext()) {
			// Iterate over the linked list and find a match for the querykeyword
			if(String.valueOf(listIterator.next()).toLowerCase(Locale.ROOT).contains(queryWord.toLowerCase(Locale.ROOT))){//case insensitive match
				idx = listIterator.nextIndex();
			}

			if(idx>-1) {
				res.add(searchHelper(contextWords, idx));
				idx=-1;//reset the idx again so that old value is not used in subsequent iteration
			}

		}
		String[] ret = res.toArray(new String[res.size()]);
		return ret;
    }

	/**
	 *
	 * @param contextWords
	 * @param idx
	 * @return
	 */
	private String searchHelper(int contextWords, int idx) {

		LinkedList res= new LinkedList();
		ListIterator<String> nextListIterator = words.listIterator(idx); // to iterate forward from the query keyword
		ListIterator<String> prevListIterator = words.listIterator(idx); // to iterate backward from the query keyword



		int i=0;
		/**
		 * The following while loop traverses the linkedlist in the forward direction
		 * towards the end of the file starting from the found keyword index
		 */
		while (null != nextListIterator && i < contextWords && nextListIterator.hasNext()) {
			String temp=nextListIterator.next();
			res.addLast(temp);
			if(!temp.matches("\\W+")) // for spaces and punctuations don't increment i
				i++;
		}
		/**
		 * The following while loop traverses the linked list in the backward direction
		 * towards the start of the file starting from the found keyword index
		 */
		i=contextWords; // set i back to contextwords to retreat up to 0
		while (null != prevListIterator && i >= 0 && prevListIterator.hasPrevious()) {
			String temp=prevListIterator.previous();
			res.addFirst(temp);
			if(!temp.matches("\\W+"))// for spaces and punctuations don't decrement  i
				i--;
		}

		return String.valueOf(res.stream().collect(Collectors.joining("")));
	}

	// Any needed utility classes can just go in this file.
}


