package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.ExpenseRepository;
import javax.validation.Valid;
import java.util.Set;

@Controller
public class ExpensesController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/expenses/new")
    public String showForm(Expense expense, Model model) {
        model.addAttribute("expense", expense);
        return "expenseEdit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/expenses/new")
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

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/expenses")
    public String expensesList(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        Iterable<Expense> expenses;

        if (currentUser.isAdmin()) {
            if (filter != null && !filter.isEmpty()) {
                expenses = expenseRepository.findByTextOrComment(filter, filter);
            } else {
                expenses = expenseRepository.findAll();
            }
        } else {
            if (filter != null && !filter.isEmpty()) {
                expenses = expenseRepository.findByAuthorAndTextOrComment(currentUser, filter, filter);
            } else {
                expenses = expenseRepository.findByAuthor(currentUser);
            }
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);

        return "expenses";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-expenses/{user}")
    public String userExpenses(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model
    ) {
        Set<Expense> expenses = user.getExpenses();
        model.addAttribute("expenses", expenses);
        return "expenses";
    }
}
