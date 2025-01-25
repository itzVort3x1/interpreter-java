import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void run(String source, String command){
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        if(command.equals("evaluate")){
            Parser parser = new Parser(tokens);
            Expr expression = parser.parse();
            if (hadError) return;
            interpreter.interpret(expression);
        }

        if(command.equals("parse")){
            Parser parser = new Parser(tokens);
            Expr expression = parser.parse();
            if (hadError) return;
            System.out.println(new AstPrinter().print(expression));
        }

        if(command.equals("tokenize")){
            for(Token token: tokens){
                System.out.println(token);
            }
        }

        if (hadError) return;
    }

    static void error(int line, String message) {;
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

}
