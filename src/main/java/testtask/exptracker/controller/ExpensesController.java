package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.ExpenseRepository;
import javax.validation.Valid;

@Controller
public class ExpensesController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/expenses/new")
    public String showAddExpenseForm(Expense expense, Model model) {
        model.addAttribute("expense", expense);
        return "expenseAdd";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping("/expenses/new")
    public String addExpense(
            @AuthenticationPrincipal User user,
            @Valid Expense expense,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "expenseAdd";
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
                expenses = expenseRepository.findByAuthorAndText(currentUser, filter);
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
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model
    ) {
        Iterable<Expense> expenses;
        if (filter != null && !filter.isEmpty()) {
            expenses = expenseRepository.findByAuthorAndText(user, filter);
        } else {
            expenses = user.getExpenses();
        }

        model.addAttribute("expenses", expenses);
        return "expenses";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/expenses/{expense}")
    public String showEditExpenseForm(@PathVariable Expense expense, Model model) {
        model.addAttribute("expense",expense);
        return "expenseEdit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/expenses/{expense}")
    public String editExpense(
            @PathVariable Long expense,
            @Valid Expense editedExpense,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "expenseEdit";
        }

        expenseRepository.save(editedExpense);
        return "redirect:/expenses";
    }
}
