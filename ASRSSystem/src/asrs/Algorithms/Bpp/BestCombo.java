package asrs.Algorithms.Bpp; /**
 * Created by ewart on 28-4-2015.
 */
import asrs.Models.Product;

import java.util.ArrayList;
import java.util.Stack;

public class BestCombo {

    /** Set a value for target sum */
    private int target;
    private int sumInStack, bestStackId;

    private Stack<Product> stack;
    private ArrayList<Product> bestCombination;


    public BestCombo(int arrayLength, int targetNumber) {
        this.stack =  new Stack<>();
        this.bestCombination =  new ArrayList<>();
        this.target = targetNumber;
        this.sumInStack = 0;

        // bestStackId is equal to length of data array.
        // if arrayLength is higher then 2 times the target bestStackId equals target times 2 to prevent unnecessary calculations.
        if( arrayLength > targetNumber) {
            this.bestStackId = targetNumber;
        } else {
            this.bestStackId = arrayLength + 1;
        }
    }


    public int getBestStackId() {
        return bestStackId;
    }

    public ArrayList<Product> calculateTarget(ArrayList<Product> data, int fromIndex, int endIndex) {

        //if solution uses a lower index then the previous
        if (sumInStack == target && fromIndex < bestStackId) {
            this.bestStackId = fromIndex;
            this.bestCombination.clear();
            for (Product p : stack) {
                bestCombination.add(p);
            }
        }

        for (int currentIndex = fromIndex; currentIndex < endIndex; currentIndex++) {
            if (sumInStack + data.get(currentIndex).getSize() <= target) {
                stack.push(data.get(currentIndex));
                sumInStack += data.get(currentIndex).getSize();

                /*
                * Make the currentIndex +1, and then use recursion to proceed
                * further.
                */
                calculateTarget(data, currentIndex + 1, endIndex);
                sumInStack -= stack.pop().getSize();
            }
        }
        return bestCombination;
    }

}
