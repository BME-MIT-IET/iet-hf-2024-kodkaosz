import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Felhasználói ablakot megvalósító osztály
 */
public class MainWindow extends JFrame {

    /**
     * Jelmagyarázatot tartalmazó panel
     */
    private class InfoPanel extends JPanel {
        /**
         * Panel háttere
         */
        private Image img;

        /**
         * Konstruktor
         */
        public InfoPanel() {
            try {
                img = ImageIO.read(new File("kepek/infopanel.png")).getScaledInstance(190,300,Image.SCALE_SMOOTH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setPreferredSize(new Dimension(190,300));
        }

        /**
         * Kirajzolás felüldefiniálása a háttér megjelenítése miatt
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    /**
     * Játékteret megvalósító osztáy
     */
    private class Canvas extends JPanel {
        /**
         * Panel háttere
         */
        private Image img;

        /**
         * Konstruktor
         */
        public Canvas() {
            try {
                img = ImageIO.read(new File("kepek/desert.png")).getScaledInstance(500,500,Image.SCALE_SMOOTH);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setPreferredSize(new Dimension(500,500));
            setLayout(new BorderLayout());
        }

        /**
         * Kirajzolás felüldefiniálása a háttér megjelenítéséért
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, null);
        }
    }

    /**
     * Menüsáv
     */
    private JMenuBar menuBar;
    /**
     * Menükategória
     */
    private JMenu menu;
    /**
     * Fájlolvasás menüpontja
     */
    private JMenuItem open;
    /**
     * Játéktér
     */
    private JPanel canvas;
    /**
     * Oldalsó panel
     */
    private JPanel sidebar;
    /**
     * Pontszámok panele
     */
    private JPanel scorePanel;
    /**
     * Jelmagyarázat panele
     */
    private JPanel infoPanel;
    /**
     * Pályaleíró kiválasztásához használt fájlkiválasztó
     */
    private JFileChooser fc;
    /**
     * Megihusúlt beolvasás figyelmeztető dialógusablaka
     */
    private JOptionPane failedOpening;

    /**
     * Inicializálja a menüsáv, menü, menüpont komponenseket
     */
    private void initMenu() {
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        open = new JMenuItem("Open");
        menu.add(open);
        menuBar.add(menu);
        open.addActionListener((e) -> {
            fc.showOpenDialog(null);
            if (!Main.proto.load(fc.getSelectedFile().getPath())) {
                JDialog jd = failedOpening.createDialog("ERROR");
                jd.setVisible(true);
            }
        });
    }

    /**
     * Inicializálja a játékteret
     */
    private void initCanvas() {
        canvas = new Canvas();
        canvas.setVisible(true);
    }

    /**
     * Inicializálja az oldalsó panelt
     */
    private void initSidebar() {
        sidebar = new JPanel();
        scorePanel = new JPanel();
        infoPanel = new InfoPanel();
        scorePanel.setPreferredSize(new Dimension(190,200));
        sidebar.add(scorePanel);
        sidebar.add(infoPanel);
        sidebar.setPreferredSize(new Dimension(190, 500));
        scorePanel.setBackground(Color.WHITE);
    }

    /**
     * Inicializálja a féjlkiválasztót
     */
    private void initFC() {
        fc = new JFileChooser();
        fc.setDialogTitle("Open pipeline network");
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Pipeline network data", "dat"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
    }

    /**
     * Inicializálja a figyelmeztető párbeszédablakot
     */
    private void initWarning() {
        failedOpening = new JOptionPane(JOptionPane.WARNING_MESSAGE);
        failedOpening.setMessage("Could not load file :/");
    }

    private void initScorePanel(){
        BoxLayout boxlayout = new BoxLayout(scorePanel, BoxLayout.Y_AXIS);
        scorePanel.setLayout(boxlayout);
        ScoreCounter plumberCounter = new ScoreCounter("Plumber: ");
        ScoreCounter saboteurCounter = new ScoreCounter("Saboteur: ");
        scorePanel.add(plumberCounter);
        scorePanel.add(saboteurCounter);

    }

    /**
     * Konstruktor
     */
    private MainWindow() {
        super("Drukmákor plumbing");
        initMenu();
        initCanvas();
        initSidebar();
        initFC();
        initWarning();
        initScorePanel();
        setJMenuBar(menuBar);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(725, 570);
        setResizable(false);
        setLayout(new FlowLayout());
        add(canvas);
        add(sidebar);
    }

    /**
     * Singelton mintához tartozó referencia az egyetlen példányra az osztályból
     */
    private static MainWindow instance = new MainWindow();

    /**
     * Visszaadja az egyetlen példányt az osztályból
     * @return
     */
    public static MainWindow getInstance() {
        return instance;
    }

    /**
     * Játéktér panel gettere
     * @return játékteret megvalósító panel
     */
    public JPanel getCanvas() {
        return canvas;
    }

    /**
     * Pontszámláló panel gettere
     * @return pontszámlálót megábafoglaló panel
     */
    public JPanel getScorePanel() {
        return sidebar;
    }

    /**
     * Kiüríti a játékteret
     */
    public void clearCanvas() {
        canvas.removeAll();
    }
}
