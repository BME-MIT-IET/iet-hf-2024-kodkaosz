public class Main {
    public static Proto proto = new Proto();

    public static void main(String[] args) {
        MainWindow.getInstance().setVisible(true);
        while (true) {

            proto.playGame();
        }
    }
}