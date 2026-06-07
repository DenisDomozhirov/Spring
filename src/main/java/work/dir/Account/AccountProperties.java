package work.dir.Account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {

    private final int defaultAmount;
    private final double moneyCommission;

    public AccountProperties(
            @Value("${account.default-amount}") int defaultAmount,
            @Value("${account.transfer-commission}") double moneyCommission
    ){
        this.defaultAmount = defaultAmount;
        this.moneyCommission = moneyCommission;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public double getMoneyCommission() {
        return moneyCommission;
    }
}
