package coderschool.lamtran.todoapp.Utils;

/**
 * Created by lamtran on 2/7/17.
 */

public class Utils {
    public static String getPriorityTitle(int position) {
        String priorityTitle = "";

        switch (position) {
            case 0:
                priorityTitle = "HIGHT";
                break;

            case 1:
                priorityTitle = "MEDIUM";
                break;

            case 2:
                priorityTitle = "LOW";
                break;
        }

        return priorityTitle;
    }
}
