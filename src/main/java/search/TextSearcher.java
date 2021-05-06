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
    private LinkedList<String> words;

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

        /*
         * TODO
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

        /*
         * TODO
         */
        List<String> res = new ArrayList<>();
        int startIdx = 0, endIndex=words.size(),idx=-1;
        ListIterator listIterator= words.listIterator();
		while (listIterator.hasNext()) {
			if(String.valueOf(listIterator.next()).toLowerCase(Locale.ROOT).contains(queryWord.toLowerCase(Locale.ROOT))){
				idx = listIterator.nextIndex();
			}

			if(idx>-1) {
				res.add(searchHelper(contextWords, idx));
				idx=-1;//reset the idx again
			}
			//words.remove(idx);
		}
		String[] ret = res.toArray(new String[res.size()]);
		return ret;
    }

	private String searchHelper(int contextWords, int idx) {

		LinkedList res= new LinkedList();
		ListIterator<String> nextListIterator = words.listIterator(idx);
		ListIterator<String> prevListIterator = words.listIterator(idx);



		int i=0;
		while (null != nextListIterator && i < contextWords && nextListIterator.hasNext()) {
			String temp=nextListIterator.next();
			res.addLast(temp);
			if(!temp.matches("\\W+"))
				i++;
		}

		i=contextWords;
		while (null != prevListIterator && i >= 0 && prevListIterator.hasPrevious()) {
			String temp=prevListIterator.previous();
			res.addFirst(temp);
			if(!temp.matches("\\W+"))
				i--;
		}

		return String.valueOf(res.stream().collect(Collectors.joining("")));
	}

	// Any needed utility classes can just go in this file.
}


