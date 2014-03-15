/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author panfilov_ms
 */
public class FlavourImpl implements Flavour{

    @Override
    public int compareTo(Flavour o) {
        if (this.hashCode() == o.hashCode()){
            return 0;
        }else{
            return -1;
        }
    }
    
}
