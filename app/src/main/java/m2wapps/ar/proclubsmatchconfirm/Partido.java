package m2wapps.ar.proclubsmatchconfirm;

/**
 * Created by mariano on 19/08/2016.
 */
public class Partido {
    private String  hora;
    private int equipo1, equipo2;
    public Partido(int equipo1, int equipo2, String year) {
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.hora = year;
    }
    public int getEquipo1() {
        return equipo1;
    }

    public String getYear() {
        return hora;
    }

    public int getEquipo2() {
        return equipo2;
    }
}
