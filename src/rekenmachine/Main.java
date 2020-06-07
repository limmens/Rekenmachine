package rekenmachine;

import javax.swing.*;

public class Main
{
    //maakt een nieuwe rekenmachine aan
    public static void main(String[] args)
    {
        Rekenmachine r = new Rekenmachine();
        r.setTitle("Rekenmachine door Loes Immens");
        r.setSize(400,500);
        r.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        r.setVisible(true);
    }
}
