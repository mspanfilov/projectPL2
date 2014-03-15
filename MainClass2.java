
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author panfilov_ms
 */
public class MainClass2 {
    
    	public static void main(String[] args) {
                
                System.out.println("quantity eaters: "  + args[0]);
                System.out.println("quantity candies: "  + args[1]);
                System.out.println("quantity flavours: "  + args[2]);
                System.out.println("time(sec.) to work: "  + args[3]);
            	System.out.println("begin");
                
                //create set eaters of candy
                Set<CandyEater> candyEaters = new HashSet<CandyEater>();
                for(int i=0; i<Integer.parseInt(args[0]); i++){
                    candyEaters.add(new CandyEaterImpl());
                }
                
                //create array flavours
                List<Flavour> flavours = new ArrayList<Flavour>();
                for(int i=0; i<Integer.parseInt(args[2]); i++){
                    flavours.add(new FlavourImpl());
                }
                
                //create queue candies
                int j = 0;
                BlockingQueue<Candy> candies = new LinkedBlockingQueue<Candy>();
                System.out.println("queue candies:");
                for(int i=0; i<Integer.parseInt(args[1]); i++){
                    CandyImpl candy = new CandyImpl();
                    //assign flavors to candies in order
                    candy.setFlavour(flavours.get(j));
                    if (j < flavours.size()-1){
                            j++;
                    }else{
                        j = 0;
                    }
                    System.out.println("candy " + candy.hashCode() + " with flavour " + candy.getFlavour().hashCode());
                    candies.add(candy);
                }
                
		CandyEatingFacilityImpl cefi = new CandyEatingFacilityImpl();

		cefi.launch(candies, candyEaters);
          
		try {
			TimeUnit.SECONDS.sleep(Long.parseLong(args[3]));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("end");
		cefi.shutdown();
		
	}
    
}
