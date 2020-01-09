package Model;

import Model.Exceptions.InvalidInputException;

public class RegularTask extends Item {


    public RegularTask(String name, String due, boolean complete, boolean leaveAsIs) throws InvalidInputException {
        super(name, due, complete, leaveAsIs);
        this.type = Type.REGULARTASK;
    }

    @Override
    public String printItem() {
        return printItemName() +
                 printItemDue() + printItemCompletionStatus() + printItemOverdueStatus();
    }

    @Override
    public String print(){
        String string0 = type.getType();
        String string1 = getName();
        String string2 = getDue().format(format);
        String string3 = String.valueOf(getComplete());
        String string4 = String.valueOf(getLeaveAsIs());
        return string0 + "," + string1 + "," + string2 + "," + string3 + "," + string4;
    }
}


