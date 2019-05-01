package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.ExpenseForm;
import testtask.exptracker.domain.User;
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
    public String saveExpense(
            @AuthenticationPrincipal User user,
            @Valid ExpenseForm expenseForm, BindingResult bindingResult
    ){

        if (bindingResult.hasErrors()) {
            return "expenses/form";
        }

        Expense expense = expenseForm.convertToExpense(user);
        expenseRepository.save(expense);

        return "expenses/result";
    }

    @GetMapping
    public String index(@RequestParam(required = false) String filter, Model model) {
        //Iterable<Expense> expenses = expenseRepository.findAll();
        Iterable<Expense> expenses;

        if (filter != null && !filter.isEmpty()) {
            expenses = expenseRepository.findByTextOrComment(filter, filter);
        } else {
            expenses = expenseRepository.findAll();
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);

        return "expenses/index";
    }

}
