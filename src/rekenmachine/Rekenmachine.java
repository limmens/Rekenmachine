package rekenmachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * @author Loes Immens
 */

public class Rekenmachine extends JFrame implements ActionListener
{
    private final JPanel screenPanel;
    private final JTextArea screen;	//scherm van de rekenmachine
    private final JButton buttons[];	//rij met alle buttons
    private int cijfer;		//cijfer dat ingetoetst wordt met de buttons

    private final Getal getal1;	//getal vóór operator
    private final Getal getal2;	//getal na operator
    private final Getal antwoord;	//antwoord na berekening
    
    private boolean bezigMetGetal = false; //als true, dan wordt het nieuwe indrukte cijfer achteraan het huidige getal geplaatst
    private boolean puntIngedrukt = false;

    private String operatorString;		//de string van de operator (+,-,...) die op dit moment in gebruik is
    private boolean operatorIngedrukt;	//boolean om te kijken of er een operator is gekozen, eindigt invoer getal1

    private String teTonenString;       //alles wat tegelijk op het scherm getoond moet worden

    //constructor: stelt de gui en de defaultwaarden van de variabelen in
    public Rekenmachine()
    {
        //initialiseren variabelen
        JPanel holdsAll = new JPanel();					//bevat screenpanel en buttonpanel
        screenPanel = new JPanel();				//bevat textarea screen
        JPanel buttonPanel = new JPanel();				//bevat buttons in een gridlayout
        GridLayout grid = new GridLayout(6,3);			//voor buttons
        BorderLayout totalLayout = new BorderLayout();	//bevat screenpanel op NORTH en buttonpanel op CENTER

        //panels toevoegen
        holdsAll.setLayout(totalLayout);
        this.add(holdsAll);
        holdsAll.add(screenPanel, BorderLayout.NORTH);
        holdsAll.add(buttonPanel, BorderLayout.CENTER);

        //screen
        teTonenString = "0";
        screen = new JTextArea();
        screen.setEditable(false);
        screen.setText(teTonenString);
        screenPanel.add(screen);
        Dimension screenDimension = new Dimension(400,50);
        screenPanel.setPreferredSize(screenDimension);
        Font font = new Font("Arial", Font.BOLD, 32);
        screen.setFont(font);

        //buttons
        buttonPanel.setLayout(grid);
        int nButtons = 18;		//aantal buttons
        buttons = new JButton[nButtons];
        String[] buttonStrings = {  "1" , "2", "3",
                                    "4", "5", "6", 
                                    "7", "8", "9", 
                                    "0", ".", "AC", 
                                    "+", "-", "x",
                                    ":", "\u221A", "=",};	//tekst op de buttons
        for(int i = 0; i < buttons.length; i++)	//buttons instellen
        {
            buttons[i] = new JButton();
            buttons[i].setText(buttonStrings[i]);
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        //default operator
        operatorIngedrukt = false;
        operatorString = "";

        //getal 1, getal 2 en antwoord default instellen
        getal1 = new Getal(0);
        getal1.setVisible(true);	//enige getal dat op het begin zichtbaar is
        getal2 = new Getal(0);
        getal2.setVisible(false);
        antwoord = new Getal(0);	
        antwoord.setVisible(false);
    }

    //de berekening van getal 1, getal 2 en de operator, past het antwoord aan
    private void bereken(double g1, double g2, String op)
    {
        //if(g2 != 0)	//als er een getal2 is
        if(getal2.visible())
            switch(op) //rekent antwoord uit bij +, -, x en :
            {
                case " + ": antwoord.setGetal(g1 + g2); break;
                case " - ": antwoord.setGetal(g1 - g2); break;
                case " x ": antwoord.setGetal(g1 * g2); break;
                case " : ": antwoord.setGetal(g1 / g2); break;
                default: antwoord.setGetal(g1);
            }
        else if("\u221A ".equals(op))	//als de operator wortel is
            antwoord.setGetal(Math.sqrt(g1));
        else	//als er alleen een getal 1 is, wordt dat het antwoord
            antwoord.setGetal(g1);
        antwoord.setVisible(true);	//antwoord wordt op het scherm getoond
        getal1.setVisible(false);	//alleen antwoord wordt getoond, niet getal 1 en 2
        getal2.setVisible(false);
        getal1.setGetal(antwoord.getGetal()); //zo kun je met het antwoord verder rekenen
        getal2.clearGetal();
        bezigMetGetal = false;		//je kunt weer een nieuw getal intypen
        operatorIngedrukt = false;	//weer op default voor eventuele volgende berekening
        operatorString = "";
    }

    //wat gebeurt er als welke knop wordt ingedrukt?
    @Override
    public void actionPerformed(ActionEvent event)
    {
        antwoord.setVisible(false);	//als er een toets wordt ingedrukt, hoeft het vorige antwoord niet meer als antwoord op het scherm zichtbaar te zijn
        if(!bezigMetGetal)
            puntIngedrukt = false;	//want als er nog geen getal is, is het sowieso geen decimaal getal

        //als de 0 wordt ingedrukt
        //if(event.getSource() == buttons[9])		
        //	cijfer = 0;

        //als er een getal van 1 t/m 9 wordt ingetoetst
        for(int i = 0; i < 10; i++)				
        {
            if(event.getSource() == buttons[i])	
            {
                cijfer = i + 1;		//bv. cijfer 1 zit op plaats 0 in de rij van buttons
                if(cijfer == 10)
                    cijfer = 0;         //0 zit in de rij van buttons na de 9
                if(!bezigMetGetal)	//dan is het een nieuw getal
                {
                    if(!operatorIngedrukt)	//dan is het getal1
                    {
                        getal1.nieuwGetal(cijfer);
                        getal1.setVisible(true);
                    }
                    else	//getal2
                    {
                        getal2.nieuwGetal(cijfer);
                        getal2.setVisible(true);
                    }
                    bezigMetGetal = true;	//vanaf nu is het geen nieuw getal meer
                }
                else
                {
                    if(!puntIngedrukt)	//nog geen decimalen nodig
                    {
                        if(!operatorIngedrukt) 
                            getal1.addCijfer(cijfer);
                        else
                            getal2.addCijfer(cijfer);
                    }
                    else	//wel bezig met decimalen
                    {
                        if(!operatorIngedrukt)
                            getal1.addDecimaal(cijfer);
                        else
                            getal2.addDecimaal(cijfer);
                    }
                }
            }
        }

        //voor de operators +, -, x en :
        for(int i = 12; i < 16; i++)	
        {
            if(event.getSource() == buttons[i])
            {
                if(operatorIngedrukt && getal2.visible())	//verder rekenen als geen = is ingedrukt (bv. 5 x 2 - 7)
                    bereken(getal1.getGetal(), getal2.getGetal(), operatorString);
                getal2.clearGetal();
                operatorIngedrukt = true;
                bezigMetGetal = false;
                switch(i)
                {
                    case 12: operatorString = " + "; break;	//deze wordt gebruikt om de goede berekening te kiezen bij de methode bereken
                    case 13: operatorString = " - "; break;
                    case 14: operatorString = " x "; break;
                    case 15: operatorString = " : "; break;
                }
            }
        }

        //als de . wordt ingedrukt
        if(event.getSource() == buttons[10])	
        {
            puntIngedrukt = true;	//cijfers die vanaf nu worden ingevoerd zijn decimalen
            bezigMetGetal = true;       //voor het geval dat de 0 vóór de punt wordt overgeslagen
        }

        //als de AC wordt ingedrukt
        if(event.getSource() == buttons[11])	
        {
            getal1.clearGetal();		//alles wordt op de defaultwaarden teruggezet
            getal1.setVisible(true);
            getal2.clearGetal();
            getal2.setVisible(false);
            antwoord.clearGetal();
            antwoord.setVisible(false);
            operatorIngedrukt = false;
            bezigMetGetal = false;
        }

        //als de wortel wordt ingedrukt
        if(event.getSource() == buttons[16])	
        {
            if(operatorIngedrukt && getal2.visible())	//verder rekenen als geen = is ingedrukt (bv. 5 x 2 - 7)
                    bereken(getal1.getGetal(), getal2.getGetal(), operatorString);
            operatorString = "\u221A ";	//string voor wortelteken
            getal2.clearGetal();
            operatorIngedrukt = true;
            bezigMetGetal = false;
            bereken(getal1.getGetal(), 0, operatorString);	//geen getal 2 nodig
        }

        //als de = wordt ingedrukt
        if(event.getSource() == buttons[17])	
            bereken(getal1.getGetal(), getal2.getGetal(), operatorString);

        //op het scherm tonen wat getoond moet worden
        if(antwoord.visible())
            teTonenString = antwoord.getGetalString();

        if(getal2.visible())
        {
            if(puntIngedrukt & (getal2.getNDecimalen() == 0))
                teTonenString = getal1.getGetalString() + operatorString + getal2.getGetalString() + ".";
            else
                teTonenString = getal1.getGetalString() + operatorString + getal2.getGetalString();
        }
        else if(operatorIngedrukt)
            teTonenString = getal1.getGetalString() + operatorString;
        else if(getal1.visible())
        {
            if(puntIngedrukt & (getal1.getNDecimalen() == 0))
                teTonenString = getal1.getGetalString() + ".";
            else
                teTonenString = getal1.getGetalString();
        }

        screen.setFont(screen.getFont().deriveFont(getMatchingFontSize(screenPanel,teTonenString)));
        screen.setText(teTonenString);
    }

    //geleend van internet en aangepast
    private float getMatchingFontSize(JComponent comp, String string) 
    {
        int minSize = 20;
        int maxSize = 32;
        Dimension size = comp.getSize();

        if (comp.getFont() == null || string.isEmpty()) 
            return -1;

        //Init variables
        int width = size.width;
        int height = size.height;

        Font font = comp.getFont();
        int curSize = font.getSize();
        FontMetrics fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));
        while (fm.stringWidth(string) + 50 > width || fm.getHeight() > height) {
            maxSize--;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), maxSize));
            curSize = maxSize;
        }
        while (fm.stringWidth(string) + 50 < width || fm.getHeight() < height) {
            minSize++;
            fm = comp.getFontMetrics(new Font(font.getName(), font.getStyle(), minSize));
            curSize = minSize;
        }
        if (curSize < minSize) {
            curSize = minSize;
        }
        if (curSize > maxSize) {
            curSize = maxSize;
        }
        return curSize;
    }
}
