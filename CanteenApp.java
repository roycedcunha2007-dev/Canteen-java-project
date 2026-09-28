import com.canteen.Main;

/**
 * Root launcher class for CampusBite College Canteen Management System.
 *
 * To run:
 *   javac -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
 *   java -cp bin com.canteen.Main
 */
public class CanteenApp {
    public static void main(String[] args) {
        Main.main(args);
    }
}
