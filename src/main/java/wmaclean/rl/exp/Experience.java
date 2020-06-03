package wmaclean.rl.exp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Used to store the past of experience of the agent
 */
public class Experience {
    /**
     * Stores all experience
     */
    private final List<ExperienceItem> experienceItems;

    /**
     * How many items to store. Once this is reached, a random experience is deleted each time a new one is added
     */
    private static final int maxSize = 100000;

    /**
     * Default constructor
     */
    public Experience(){
        this.experienceItems = new ArrayList<ExperienceItem>();
    }

    /**
     * Get a particular experience item
     * @param index - index of experience item
     * @return - index-th experience item
     */
    public ExperienceItem get(int index){
        return this.experienceItems.get(index);
    }

    /**
     * Add an ExperienceItem point to the set
     * @param item - ExperienceItem to add
     */
    public void add(ExperienceItem item){
        this.experienceItems.add(item);

        if(this.size() > maxSize){
            this.removeRandom();
        }
    }

    /**
     * removes a random experience item from the set
     */
    private void removeRandom() {
        Random r = new Random();
        int index = r.nextInt(this.size());
        this.experienceItems.remove(index);
    }

    /**
     * clears the experience
     */
    public void clear(){
        this.experienceItems.clear();
    }

    /**
     * Getter for the size
     * @return - size of the list of experience items
     */
    public int size(){
        return this.experienceItems.size();
    }

    /**
     * Gets a random set of experience items
     * @param batchSize - how many items to get
     * @return - random list of experience items
     */
    public List<ExperienceItem> randomBatch(int batchSize) {
        Collections.shuffle(this.experienceItems);

        List<ExperienceItem> res = new ArrayList<ExperienceItem>();

        for (int i = 0; i < batchSize; i++){
            res.add(this.get(i));
        }

        return res;
    }
}
