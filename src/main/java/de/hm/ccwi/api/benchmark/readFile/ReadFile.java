package de.hm.ccwi.api.benchmark.readFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile {

    private BufferedReader br;

    public ReadFile (String path) throws FileNotFoundException {
        FileReader fr = new FileReader(path);
        br = new BufferedReader(fr);
    }

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
