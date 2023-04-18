package preprocessor;

public class Preprocessor {

    public String preprocess(String sourceCode) {
        sourceCode = removeComment(sourceCode);
        sourceCode = removeEmptyLine(sourceCode);
        return sourceCode;
    }

    private String removeComment(String sourceCode) {
        StringBuilder buffer = new StringBuilder();
        for (String line : sourceCode.split("\n"))
            buffer.append(line.split("//")[0]).append("\n");
        return buffer.toString();
    }

    private String removeEmptyLine(String sourceCode) {
        StringBuilder buffer = new StringBuilder();
        for (String line : sourceCode.split("\n"))
            if (!line.equals(""))
                buffer.append(line).append("\n");
        return buffer.toString();
    }
}
