package client.scenes;

import client.utils.Configuration;
import com.google.inject.Inject;
import commons.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettleDebtsLogic {
    private final MainCtrl mainCtrl;
    private final Configuration configuration;

    @Inject
    public SettleDebtsLogic(MainCtrl mainCtrl, Configuration configuration) {
        this.mainCtrl = mainCtrl;
        this.configuration = configuration;
    }

    public class Debt {
        private final Integer eventId;
        private final User owed;
        private final User indebted;
        private final Double amount;

        public User getOwed() {
            return owed;
        }

        public User getIndebted() {
            return indebted;
        }

        public Double getAmount() {
            return amount;
        }

        public Debt(Integer eventId, User owed, User indebted, Double amount) {
            this.eventId = eventId;
            this.owed = owed;
            this.indebted = indebted;
            this.amount = amount;
        }

        public Integer getEventId() {
            return eventId;
        }
    }

    public List<Debt> getDebts(Integer eventId) {
        // PLACEHOLDER: Get debts here
        List<Debt> debts = new ArrayList<>();

        User user1 = new User(0, "Mik Mettemaker", "mik@qudical.nl", "", "213123", 0.0);
        User user2 = new User(0, "Ton Superbos", "", "NL2393284", "324245", 0.0);

        debts.add(new Debt(eventId, user1, user2, 5.0));
        debts.add(new Debt(eventId, user2, user1, 5.0));

        return debts;
    }

    public void sendReminder(Debt debt) {
        // COMPLETE THIS
    }

    public void markReceived(Debt debt) {
        // COMPLETE THIS

        // Keep this, though.
        mainCtrl.settleDebtsPage(debt.getEventId());
    }

    boolean isValidEmail(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    boolean isEmailConfigured() {
        return configuration.getMailConfig() != null;
    }
}
