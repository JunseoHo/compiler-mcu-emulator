package preprocessor;

import main.Constant;

public class Preprocessor {

    private String code;

    public void preprocess(String code) {
        this.code = code;
        removeComment();
        removeEmptyLine();
        printCode();
    }

    public String getCode() {
        return code;
    }

    public void printCode() {
        System.out.println(Constant.PREPROCESSOR_PRINT_HEADER + code);
    }

    private void removeComment() {
        String tmp = "";
        boolean isComment = false;
        for (char c : code.toCharArray()) {
            if (isComment) {
                if (c == '\n') {
                    isComment = false;
                    tmp += c;
                }
            } else {
                if (c == ';') isComment = true;
                else tmp += c;
            }
        }
        code = tmp;
    }

    private void removeEmptyLine() {
        String tmp = "";
        String[] lines = code.split("\n");
        for (String line : lines) if (!line.equals("")) tmp += line + '\n';
        code = tmp;
    }

}
