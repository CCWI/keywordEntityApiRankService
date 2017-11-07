package de.hm.ccwi.api.benchmark.readFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Marcel
 * @project twitterDataExtraction
 * @email mk@mkarrasch.de
 * @createdOn 26.11.2016
 * @package de.mk.twitterDataExtraction
 */

public class ReadFile {

    private BufferedReader br;

    /**
     * Read Input File. Input File must consist of Twitter JSON messages
     *
     * @param path Path to the Import File
     * @throws FileNotFoundException File is not available
     */
    public ReadFile (String path) throws FileNotFoundException {
        FileReader fr = new FileReader(path);
        br = new BufferedReader(fr);
    }

    /**
     * Return the next line of the Import File
     *
     * @return Return the next line of the Import File
     * @throws IOException General File Error
     */
    public String nextLine () throws IOException {
        String zeile = br.readLine();
        if (zeile != null) {
            return zeile;
        } else {
            br.close();
            return null;
        }
    }
}
