package ie.atu.sw;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TextComparator {
    Path textFileA = null;
    Path textFileB = null;
    Path stopWordsFile = null;

    public void setTextFileA(String textFileAPath) {
        this.textFileA = Paths.get(textFileAPath);
    }

    public void setTextFileB(String textFileBPath) {
        this.textFileB = Paths.get(textFileBPath);
    }

    public void setStopWordsFile(String stopWordsFilePath) {
        this.stopWordsFile = Paths.get(stopWordsFilePath);
    }

    public Path getTextFileA() {
        return textFileA;
    }

    public Path getTextFileB() {
        return textFileB;
    }

    public Path getStopWordsFile() {
        return stopWordsFile;
    }

    public void uploadTextFileA() {
    };
}
