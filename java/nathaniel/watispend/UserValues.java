package nathaniel.watispend;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    public double currentWeekly;
    public double currentDaily;
    public double suggestWeekly;
    public double suggestDaily;
    public Calendar termStart;
    public Calendar termEnd;
    public HashMap<Calendar, ArrayList<Transaction>> transactions;
    public ArrayList<Double> chartData;
    public boolean chartChange;

    protected UserValues(){}

    public static synchronized UserValues getInstance(){
        if(null == mInstance){
            mInstance = new UserValues();
        }
        return mInstance;
    }
}
