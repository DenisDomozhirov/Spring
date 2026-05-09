package work.dir.Account;

public class Account {

    private final int id;
    private final int userID;
    private int moneyAmount;

    public Account(int id, int userID, int moneyAmount) {
        this.id = id;
        this.userID = userID;
        this.moneyAmount = moneyAmount;
    }

    public void setMoneyAmount(int moneyAmount){
        this.moneyAmount = moneyAmount;
    }


    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userID=" + userID +
                ", moneyAmount=" + moneyAmount +
                '}';
    }
}
