package testtask.exptracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.ExpenseRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getExpenses(User user, String filter, String dateFrom, String dateTo) {
        return expenseRepository.filterByAllParams(
                user,
                filter,
                dateFrom.equals("") ? null : LocalDate.parse(dateFrom),
                dateTo.equals("") ? null : LocalDate.parse(dateTo)
        );
    }

    public Double getTotalExpenses(User user, String filter, String dateFrom, String dateTo) {
        Double result = expenseRepository.sumByAllParams(
                user,
                filter,
                dateFrom.equals("") ? null : LocalDate.parse(dateFrom),
                dateTo.equals("") ? null : LocalDate.parse(dateTo)
        );
        return result == null ? 0.0 : result;
    }

    public Double getAverageExpenses(User user, String filter, String dateFrom, String dateTo, Double total) {
        if (total.equals(0.0)) {
            return 0.0;
        } else {
            Long countDays = expenseRepository.countDaysByAllParams(
                    user,
                    filter,
                    dateFrom.equals("") ? null : LocalDate.parse(dateFrom),
                    dateTo.equals("") ? null : LocalDate.parse(dateTo)
            );
            return total/countDays;
        }

    }

//    public Expense getOneExpense(String strId) {
//        try {
//            Long id = Long.valueOf(strId);
//            Expense expense = expenseRepository.getOne(id);
//            return expense;
//        } catch (NumberFormatException nfe) {
//            //наверное стоит выдавать 404
//            return null;
//        } catch (EntityNotFoundException enf) {
//            return null;
//        }
//    }
}
