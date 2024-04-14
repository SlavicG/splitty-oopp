package client.scenes;

import client.utils.Configuration;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.dto.Expense;
import commons.dto.Mail;
import commons.dto.MailConfig;
import commons.dto.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettleDebtsLogic {
    private final MainCtrl mainCtrl;
    private final Configuration configuration;

    private ServerUtils server;

    @Inject
    public SettleDebtsLogic(MainCtrl mainCtrl, Configuration configuration, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.configuration = configuration;
        this.server = server;
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
        /*
        List<Debt> debts = new ArrayList<>();

        User user1 = new User(0, "Mik Mettemaker", "mik@qudical.nl", "", "213123", 0.0);
        User user2 = new User(0, "Ton Superbos", "", "NL2393284", "324245", 0.0);

        debts.add(new Debt(eventId, user1, user2, 5.0));
        debts.add(new Debt(eventId, user2, user1, 5.0));

        return debts;
        */

        List<Debt> debts = new ArrayList<>();
        var debts_random = server.getAllDebtsInEvent(eventId);


        List<User> indebted = new ArrayList<>(), debtee = new ArrayList<>();

        for (var x : debts_random.entrySet()) {
            if (x.getValue() < 0) {
                User user = server.getUserById(eventId, x.getKey());
                user.setDebt(x.getValue());
                indebted.add(user);
            } else if (x.getValue() > 0) {
                User user = server.getUserById(eventId, x.getKey());
                user.setDebt(x.getValue());
                debtee.add(user);

            }
        }
        Collections.sort(indebted, new Comparator<User>() {
            @Override
            public int compare(User h1, User h2) {
                return h1.getDebt().compareTo(h2.getDebt());
            }
        });
        Collections.sort(debtee, new Comparator<User>() {
            @Override
            public int compare(User h1, User h2) {
                return h2.getDebt().compareTo(h1.getDebt());
            }
        });
        int i = 0, j = 0;
        while (i < indebted.size() && j < debtee.size()) {
            if (-indebted.get(i).getDebt() >= debtee.get(j).getDebt()) {
                indebted.get(i).setDebt(indebted.get(i).getDebt() + debtee.get(j).getDebt());
                debts.add(new Debt(eventId, indebted.get(i), debtee.get(j), debtee.get(j).getDebt()));
                if (indebted.get(i).getDebt().compareTo(0.0) == 0)
                    i++;
                j++;
            } else {
                debtee.get(j).setDebt(indebted.get(i).getDebt() + debtee.get(j).getDebt());
                debts.add(new Debt(eventId, indebted.get(i), debtee.get(j), -indebted.get(i).getDebt()));
                i++;
            }
        }
        return debts;
    }
    private static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    public void setMailConfig(MailConfig mailConfig) {
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setUsername(mailConfig.getUsername());
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailConfig.getProps().getProperty("mail.transport.protocol"));
        props.put("mail.smtp.auth", mailConfig.getProps().getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", mailConfig.getProps().getProperty("mail.smtp.starttls.enable"));
        props.put("mail.debug", mailConfig.getProps().getProperty("mail.debug"));
    }
    public void sendEmail(Mail mail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailSender.getUsername());
        mailMessage.setCc(mailSender.getUsername());
        mailMessage.setTo(mail.getToMail());
        mailMessage.setText(mail.getBody());
        mailMessage.setSubject(mail.getSubject());
        mailSender.send(mailMessage);
    }
    public void sendReminder(Debt debt) {
        var mc = Executors.newVirtualThreadPerTaskExecutor();

        Configuration configuration = new Configuration();;
        if(configuration.getMailConfig() == null) return;

        setMailConfig(configuration.getMailConfig());
        Mail mailRequest = new Mail(debt.getIndebted().getEmail(),
                "Payment Reminder",
                "Hello, " + debt.getIndebted().getName() +
                        "! You still owe " + debt.getAmount() +
                        " to " + debt.getOwed().getName() +
                        "! Don't forget to pay your debts!");
        mc.execute(() -> sendEmail(mailRequest));
    }

    public void markReceived(Debt debt) {
        var ls = new ArrayList<Integer>();
        ls.add(debt.getOwed().getId());
        Expense expense = new Expense(debt.getEventId(), debt.getAmount(),
                "Settle Debts",
                debt.getIndebted().getId(),
                LocalDate.now(), ls,
                server.getEventById(debt.getEventId()).getTags().get(0).getId());
        server.addExpense(expense, debt.getEventId());
                /*
                Integer id, Double amount, String description,
                   Integer payerId, LocalDate date, List<Integer> splitBetween, Integer tagId
                 */
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
