package testtask.exptracker.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.exceptions.BadRequestException;
import testtask.exptracker.exceptions.ForbiddenException;
import testtask.exptracker.exceptions.NotFoundException;
import testtask.exptracker.repository.ExpenseRepository;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getExpenses(User user, String filter, String strDateFrom, String strDateTo) {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;
        try {
            if (!StringUtils.isEmpty(strDateFrom)) {
                dateFrom = LocalDate.parse(strDateFrom);
            }
            if (!StringUtils.isEmpty(strDateTo)) {
                dateTo = LocalDate.parse(strDateTo);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException();
        }
        return expenseRepository.filterByAllParams(user, filter, dateFrom, dateTo);
    }

    public Double getTotalExpenses(User user, String strDateFrom, String strDateTo) {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;
        try {
            if (!StringUtils.isEmpty(strDateFrom)) {
                dateFrom = LocalDate.parse(strDateFrom);
            }
            if (!StringUtils.isEmpty(strDateTo)) {
                dateTo = LocalDate.parse(strDateTo);
            }
        } catch (DateTimeParseException e) {
            throw new BadRequestException();
        }
        Double result = expenseRepository.sumByAllParams(user, null, dateFrom, dateTo);
        return result == null ? 0.0 : result;
    }

    public Double getAverageExpenses(
            User user,
            String strDateFrom,
            String strDateTo,
            Double total
    ) {
        LocalDate dateFrom;
        LocalDate dateTo;
        if (total.equals(0.0)) {
            return 0.0;
        } else {
            if(StringUtils.isEmpty(strDateFrom)){
                dateFrom = expenseRepository.minDate(user);
            } else {
                try{
                    dateFrom = LocalDate.parse(strDateFrom);
                } catch (DateTimeParseException e) {
                    throw new BadRequestException();
                }
            }
            if(StringUtils.isEmpty(strDateTo)){
                dateTo = LocalDate.now();
            } else {
                try{
                    dateTo = LocalDate.parse(strDateTo);
                } catch (DateTimeParseException e) {
                    throw new BadRequestException();
                }
            }
            Long countDays = DAYS.between(dateFrom, dateTo) + 1;
            return total/countDays;
        }
    }

    public Expense getOneExpense(User currentUser, Long id) {
        Optional<Expense> oExpense = expenseRepository.findById(id);
        if (!oExpense.isPresent()) {
            throw new NotFoundException();
        }
        Expense expense = oExpense.get();
        if (!currentUser.isAdmin() && !currentUser.getId().equals(expense.getAuthor().getId())) {
            throw new ForbiddenException();
        }
        return expense;
    }

    public Expense addNewExpense(User currentUser, Expense expense) {
        expense.setAuthor(currentUser);
        return expenseRepository.save(expense);
    }

    public Expense editExpense(User currentUser, Expense expenseFromDb, Expense expense) {
        if (!currentUser.isAdmin() && !currentUser.getId().equals(expenseFromDb.getAuthor().getId())) {
            throw new ForbiddenException();
        }
        BeanUtils.copyProperties(expense, expenseFromDb, "id", "author");
        return expenseRepository.save(expenseFromDb);
    }

    public void deleteExpense(User currentUser, Expense expense) {
        if (!currentUser.isAdmin() && !currentUser.getId().equals(expense.getAuthor().getId())) {
            throw new ForbiddenException();
        }
        expenseRepository.delete(expense);
    }
}
