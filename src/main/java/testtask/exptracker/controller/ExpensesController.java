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
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.ExpenseRepository;
import javax.validation.Valid;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @GetMapping("/new")
    public String showForm(Expense expense, Model model) {
        model.addAttribute("expense", expense);
        return "expenseEdit";
    }

    @PostMapping("/new")
    public String saveExpense(
            @AuthenticationPrincipal User user,
            @Valid Expense expense,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "expenseEdit";
        }

        expense.setAuthor(user);
        expenseRepository.save(expense);

        return "redirect:/expenses";
    }

    @GetMapping
    public String expensesList(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Expense> expenses;

        if (filter != null && !filter.isEmpty()) {
            expenses = expenseRepository.findByTextOrComment(filter, filter);
        } else {
            expenses = expenseRepository.findAll();
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);

        return "expenses";
    }

}
