package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.ExpenseForm;
import testtask.exptracker.repository.ExpenseRepository;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/new")
    public String showForm(ExpenseForm expenseForm) {
        return "expenses/form";
    }

    @PostMapping("/new")
    public String saveExpense(@Valid ExpenseForm expenseForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "expenses/form";
        }

        Expense expense = expenseForm.convertToExpense();
        expenseRepository.save(expense);

        return "expenses/result";
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        model.put("expenses", expenses);
        return "expenses/index";
    }

}
