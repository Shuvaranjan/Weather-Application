import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;

public class WeatherApplicationGUI extends JFrame implements ActionListener {
    private JSONObject weatherData;

    int mouseX, mouseY;
    // private static final Color TEXT_COLOR = new Color(255, 255, 255);
    // private static final Color BUTTON_COLOR = new Color(255, 255, 255);
    // private static final Color SEARCHFIELD_COLOR = new Color(255, 255, 255);

    private JPanel settingPanel, dragPanel;
    private JTextField searchfield;
    private JButton searchButton, closeButton, settings, closePanel;
    private JLabel weatherConditionImg, humidityImg, windSpeedImg;
    private JLabel temperatureText, weatherConditionDesc, humidityText, windSpeedText;

    // create Constructor
    public WeatherApplicationGUI() {
        // set the size of this GUI
        setSize(550, 750);
        setResizable(false);

        // set the layout of GUI
        setLayout(null);

        // set GUI launched middle of the screen
        setLocationRelativeTo(null);

        // Properly close the GUI, when click close button
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set background color
        getContentPane().setBackground(new Color(17, 17, 17));
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(50,50,50), 1));

        setUndecorated(true);

        // setShape(new RoundRectangle2D.Double(0, 0, 550, 750, 20, 20));

        // Container pane = getContentPane();
        // pane.setLayout(null);
        // pane.setBackground(Color.BLUE);

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("Images/weather_icon.png"));
        setIconImage(icon.getImage());
        addComponents();

    }

    private void addComponents() {

        dragPanel = new JPanel();
        dragPanel.setBounds(0, 0, 470, 20);
        dragPanel.setBackground(null);
        dragPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

        });
        dragPanel.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen() - mouseX;
                int y = e.getYOnScreen() - mouseY;
                setLocation(x, y);
            }

        });
        add(dragPanel);

        searchfield = new JTextField();
        searchfield.setBounds(85, 30, 300, 35);
        searchfield.setBackground(new Color(24, 24, 24));
        searchfield.setForeground(Color.WHITE);
        searchfield.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        // searchfield.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        searchfield.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        searchfield.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                searchfield.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 2));

            }
            
        });
       
        add(searchfield);

        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("Images/search.png"));
        Image i1 = icon.getImage().getScaledInstance(27, 27, Image.SCALE_SMOOTH);
        ImageIcon i2 = new ImageIcon(i1);
        searchButton = new JButton(i2);
        searchButton.setFocusable(false);
        searchButton.setBackground(new Color(207, 0, 0));
        searchButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 1));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(385, 30, 60, 35);
        searchButton.addActionListener(this);
        add(searchButton);

        closeButton = new JButton("Close");
        closeButton.setBounds(470, 0, 80, 20);
        closeButton.setBorderPainted(false);
        closeButton.setFocusable(false);
        closeButton.setBackground(new Color(97, 97, 97));
        closeButton.setForeground(new Color(30, 30, 30));
        closeButton.setFont(new Font("Dialog", Font.BOLD, 15));
        closeButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(132, 132, 132));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(97, 97, 97));
            }

        });
        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });
        add(closeButton);

        // add Weather Image
        ImageIcon wIcon = new ImageIcon(ClassLoader.getSystemResource("Images/sad.png"));
        Image wIcon2 = wIcon.getImage().getScaledInstance(256, 256, Image.SCALE_DEFAULT);
        ImageIcon wIcon3 = new ImageIcon(wIcon2);
        weatherConditionImg = new JLabel(wIcon3);
        weatherConditionImg.setBounds(130, 150, 256, 256);
        add(weatherConditionImg);

        // add temperature
        temperatureText = new JLabel("0° C");
        temperatureText.setBounds(50, 400, 450, 54);
        temperatureText.setForeground(Color.WHITE);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureText.setFont(new Font("Dialog", Font.PLAIN, 30));
        add(temperatureText);

        // weather condition description
        weatherConditionDesc = new JLabel("Unknown");
        weatherConditionDesc.setBounds(50, 450, 450, 54);
        weatherConditionDesc.setForeground(Color.WHITE);
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        weatherConditionDesc.setFont(new Font("Dialog", Font.BOLD, 35));
        add(weatherConditionDesc);

        // add humidity
        ImageIcon hIcon = new ImageIcon(ClassLoader.getSystemResource("Images/humidity.png"));
        Image hIcon2 = hIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        ImageIcon hIcon3 = new ImageIcon(hIcon2);
        humidityImg = new JLabel(hIcon3);
        humidityImg.setBounds(20, 650, 80, 80);
        add(humidityImg);

        // humidity
        humidityText = new JLabel("<html><body><b>Humidity</b><br> 0%</body></html>");
        humidityText.setBounds(120, 660, 450, 54);
        humidityText.setForeground(Color.WHITE);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 18));
        add(humidityText);

        // add WindSpeed Image
        ImageIcon wiIcon = new ImageIcon(ClassLoader.getSystemResource("Images/wind-speed.png"));
        Image wiIcon2 = wiIcon.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        ImageIcon wiIcon3 = new ImageIcon(wiIcon2);
        windSpeedImg = new JLabel(wiIcon3);
        windSpeedImg.setBounds(320, 650, 80, 80);
        add(windSpeedImg);

        // windspeed
        windSpeedText = new JLabel("<html><body><b>WindSpeed</b><br> 0km/h</body></html>");
        windSpeedText.setBounds(420, 660, 450, 54);
        windSpeedText.setForeground(Color.WHITE);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 18));
        add(windSpeedText);

        settingPanel = new JPanel();
        settingPanel.setLayout(null);
        settingPanel.setBackground(new Color(12, 12, 12));
        settingPanel.setBounds(0, 0, 300, 750);
        settingPanel.setVisible(false);
        add(settingPanel);

        // add setting button
        settings = new JButton(new ImageIcon(ClassLoader.getSystemResource("Images/setting.png")));
        settings.setBounds(5, 20, 55, 55);
        settings.setForeground(new Color(255, 255, 255));
        settings.setBackground(null);
        settings.setBorderPainted(false);
        settings.setFocusable(false);
        settings.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(false);
                dragPanel.setVisible(false);
                searchfield.setVisible(false);
                searchButton.setVisible(false);
                weatherConditionDesc.setVisible(false);
                weatherConditionImg.setVisible(false);
                temperatureText.setVisible(false);
                windSpeedImg.setVisible(false);
                windSpeedText.setVisible(false);
                humidityImg.setVisible(false);
                humidityText.setVisible(false);
                closeButton.setEnabled(false);
                settingPanel.setVisible(true);

            }

        });
        add(settings);

        closePanel = new JButton(new ImageIcon(ClassLoader.getSystemResource("Images/close.png")));
        closePanel.setBounds(18, 35, 25,25);
        closePanel.setForeground(new Color(255, 255, 255));
        closePanel.setBackground(null);
        closePanel.setBorderPainted(false);
        closePanel.setFocusable(false);
        closePanel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);
                dragPanel.setVisible(true);
                searchfield.setVisible(true);
                searchButton.setVisible(true);
                weatherConditionDesc.setVisible(true);
                weatherConditionImg.setVisible(true);
                temperatureText.setVisible(true);
                windSpeedImg.setVisible(true);
                windSpeedText.setVisible(true);
                humidityImg.setVisible(true);
                humidityText.setVisible(true);
                closeButton.setEnabled(true);
                settingPanel.setVisible(false);

            }

        });
        settingPanel.add(closePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherApplicationGUI().setVisible(true);
                // System.out.println(WeatherApplication.getLocationData("Tokyo"));

            }

        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // get Location from User
            String userInput = searchfield.getText();

            // validate userInput - remove whiteSpace to ensure non-empty Text
            if (userInput.replaceAll("\\s", "").length() <= 0) {
                searchfield.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
                JOptionPane.showMessageDialog(null, "Please enter a valid location");
                return;

            }
            // retrieve weather data
            weatherData = WeatherApplication.getWeatherData(userInput);

            // update GUI
            revalidate();

            // update weather image 
            String weatherCondition = (String) weatherData.get("weather_condition");

            // depending on weather condition, we will update the weather image that corresponds with condition
            switch (weatherCondition) {
                case "Clear":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\clear.png"));
                    break;
                case "Cloudy":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\cloudy.png"));
                    break;
                case "Rain":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\rainy.png"));
                    break;
                case "Fogg":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\fogg.png"));
                    break;
                case "Snow":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\snow.png"));
                    break;
                case "ThunderStorm":
                    weatherConditionImg.setIcon(loadImage("Weather\\src\\Images\\thunder.png"));
                    break;
            
            }

            // update temperature text
            double temperature = (double) weatherData.get("temperature");
            temperatureText.setText( temperature + "° C");

            // update Weather Condition text
            weatherConditionDesc.setText(weatherCondition);

            // update Humidity Text
            long humidity = (long) weatherData.get("humidity");
            humidityText.setText("<html><body><b>Humidity</b><br>" + humidity + " %</body></html>");

            // update Wind Speed Text
            double windSpeed = (double) weatherData.get("wind_speed");
            windSpeedText.setText("<html><body><b>WindSpeed</b><br>" + windSpeed + " km/h</body></html>");



        }
    }
    
    // used to create Images in our Gui Components
    private ImageIcon loadImage(String resourcePath) {
        try {
            // read the image file from the path given
            BufferedImage image = ImageIO.read(new File(resourcePath));

            // return an imageIcon so that our component can render it
            return new ImageIcon(image);
        } catch (IOException e) {
            System.out.println("Could not load image !");
        }
        
        return null;
    }
}
