package nathaniel.watispend;

/**
 * Created by Nathaniel on 12/17/2015.
 */
public class UserValues {
    private static UserValues mInstance= null;

    public int encryptedPin;
    public int encryptedStudentNum;
    public double totalBalance;
    public double mealPlan;
    public double flex;

    protected UserValues(){}
    // This singleton class stores global constants for use in any activity.
    public static synchronized UserValues getInstance(){
        if(null == mInstance){
            mInstance = new UserValues();
        }
        return mInstance;
    }
}
