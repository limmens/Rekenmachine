package rekenmachine;

//Houdt het getal bij als double

import java.text.DecimalFormat;

public final class Getal
{
    private double resultaat;	//het getal waar het om gaat
    private int nCijfersVoorKomma;
    private int nDecimalen;		//het aantal decimalen, begint te tellen bij het eerste cijfer na de punt
    private boolean visible;	//moet het getal op het scherm worden getoond
    private String resultaatString;

    //constructor: maakt nieuw getal aan met cijfer dat wordt meegegeven
    public Getal(int cijfer)
    {
        nieuwGetal(cijfer);
    }

    //geeft het getal terug als double
    public double getGetal()
    {
        return resultaat;
    }

    //stelt het getal in op een meegegeven waarde
    public void setGetal(double g)
    {
        resultaat = g;
    }

    //geeft getal als string terug
    public String getGetalString()
    {
        if(Double.isInfinite(resultaat))
            resultaatString = "Berekening kan niet worden uitgevoerd.";
        else
            resultaatString = fmt(resultaat);
        return resultaatString;
    }

    //geeft aantal decimalen terug
    public int getNDecimalen()
    {
        return nDecimalen;
    }

    //geeft terug of getal zichtbaar moet zijn 
    public boolean visible()
    {
        return visible;
    }

    //stelt in of getal zichtbaar moet zijn
    public void setVisible(boolean bool)
    {
        visible = bool;
    }

    //het getal wordt gelijk aan het meegegeven cijfer
    public void nieuwGetal(int cijfer)
    {
        resultaat = cijfer;
        nDecimalen = 0;
        nCijfersVoorKomma = 1;
    }

    //voegt een cijfer toe aan het getal
    public void addCijfer(int cijfer)
    {
        if((nCijfersVoorKomma+nDecimalen) <9)
        {
            resultaat = resultaat * 10 + cijfer;
            nCijfersVoorKomma ++;
        }
    }

    //voegt een decimaal toe aan het getal
    public void addDecimaal(int cijfer)
    {
        if((nCijfersVoorKomma+nDecimalen) <9 && nDecimalen < 4)
        {
            nDecimalen++;
            double decimaalGetal = cijfer;	//tijdelijke variabele
            for(int i = 0; i < nDecimalen; i++)
                    decimaalGetal /= 10;	//om het cijfer op de juiste plek achter de komma in te voegen
            resultaat += decimaalGetal;
        }
    }

    //zet het getal weer op 0
    public void clearGetal()
    {
        resultaat = 0;
        nDecimalen = 0;
        nCijfersVoorKomma = 1;
    }
    
    //methode geleend van internet, zorgt ervoor dat er geen onnodige decimalen worden getoond
    private String fmt(double d)
    {
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(d);
    }

}
