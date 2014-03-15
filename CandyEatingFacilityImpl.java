
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *
 * @author panfilov_ms
 */
public class CandyEatingFacilityImpl implements CandyEatingFacility{
	
	private class InnerCandyEaterImpl implements Runnable {
		
		CandyEater candyEater;

		public InnerCandyEaterImpl(CandyEater candyEater) {
			this.candyEater = candyEater;
		}
		
	    @Override
	    public void run() {
	    	Lock lockCandy = new ReentrantLock();
	    	
	        try {
				barrier.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // eaters eat candy until all candy is not empty, then wait for shutdown
	        System.out.println("start eating");
	        while(true){
	        	lockCandy.lock();
	        	try {
	        		boolean candyFromBuffer = false;
	        		Flavour flavourCurrent;
	        		for (Candy i : bufferCandyList){
	        			if (!eatingFlavourSet.contains(i.getFlavour())) {
	    	        		flavourCurrent = i.getFlavour();
		        			eatingFlavourSet.add(flavourCurrent);
			        		this.candyEater.eat(i);
							if (bufferCandyList.remove(i)){
								System.out.println(" start eating candy " + i.hashCode() + " (flavour: " + flavourCurrent.hashCode() + ") eater: " + this.candyEater.hashCode());
						
			    				TimeUnit.MILLISECONDS.sleep(2000);
			    				eatingFlavourSet.remove(flavourCurrent);
								System.out.println(" end eating candy " + i.hashCode() + " (flavour: " + flavourCurrent.hashCode() + ") eater: " + this.candyEater.hashCode());
			    				candyFromBuffer = true;
			    				break;
	        				}
	        			}
	        		}
	        		
	        		if (!candyFromBuffer && candies.isEmpty()){
	        			System.out.println("eater: " + this.candyEater.hashCode() + " finished");
	        			break;
	        		}

	        		if (!candyFromBuffer && !candies.isEmpty()){
	        			Candy candyCurrent = candies.take();
	        			flavourCurrent = candyCurrent.getFlavour();
	        			if (eatingFlavourSet.contains(flavourCurrent)){
	        				bufferCandyList.add(candyCurrent);
	        			}else{
	        				eatingFlavourSet.add(flavourCurrent);
		        			this.candyEater.eat(candyCurrent);
							System.out.println(" start eating candy " + candyCurrent.hashCode() + " (flavour: " + flavourCurrent.hashCode() + ") eater: " + this.candyEater.hashCode());
			        	
		    				TimeUnit.MILLISECONDS.sleep(2000);
		    				eatingFlavourSet.remove(flavourCurrent);
		    				System.out.println(" end eating candy " + candyCurrent.hashCode() + " (flavour: " + flavourCurrent.hashCode() + ") eater: " + this.candyEater.hashCode());
	        			}
	        		}
	    		} catch (InterruptedException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
				} finally {
					lockCandy.unlock();
				}
	    		
	        }
	        
	    }
	    
	}
	    
    
    private ExecutorService service; 						// service for streams control
    private CyclicBarrier barrier;							// barrier to run streams at the same time
    private BlockingQueue<Candy> candies;
    private CopyOnWriteArrayList<Candy> bufferCandyList;	// ordered list candies that are not in the queue but should be eaten
    private CopyOnWriteArraySet<Flavour> eatingFlavourSet;	// tastes that are eaten at this time
    
    
    @Override
    public void launch(BlockingQueue<Candy> candies, Set<CandyEater> candyEaters) {
    	
    	if (candies == null || candyEaters == null){
    		throw new IllegalArgumentException("candies or candyEaters is null");
    	}
    	
        service = Executors.newFixedThreadPool(candyEaters.size()); // create pool of quantity eaters of candy
        this.barrier = new CyclicBarrier(candyEaters.size());		// create barrier
        this.candies = candies;
        this.bufferCandyList = new CopyOnWriteArrayList<Candy>();
        this.eatingFlavourSet = new CopyOnWriteArraySet<Flavour>();
        for(CandyEater i : candyEaters){
            service.execute(new InnerCandyEaterImpl(i));
        }
        System.out.println("All CandyEaters prestarted");
    }

    @Override
    public void shutdown() {

			service.shutdownNow() ;
			System.exit(0);

    } 
    
}
