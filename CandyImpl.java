/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panfilov_ms
 */
public class CandyImpl implements Candy{

    Flavour flavour;
    
    @Override
    public Flavour getFlavour() {
        return flavour;
    }

    public void setFlavour(Flavour flavour) {
        this.flavour = flavour;
    }
    
}
