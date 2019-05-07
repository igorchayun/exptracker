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
import testtask.exptracker.service.ExpenseService;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ExpensesController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/expenses")
    public String expensesList(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "") String dateFrom,
            @RequestParam(required = false, defaultValue = "") String dateTo,
            Model model
    ) {
        List<Expense> expenses = expenseService.getExpenses(currentUser, filter, dateFrom, dateTo);

        Double totalExpenses = expenseService.getTotalExpenses(currentUser, filter, dateFrom, dateTo);

        Double averageExpenses = expenseService.getAverageExpenses(
                currentUser,
                filter,
                dateFrom,
                dateTo,
                totalExpenses
        );

        model.addAttribute( "user", currentUser);
        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("averageExpenses", averageExpenses);

        return "expenses";
    }

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

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-expenses/{user}")
    public String userExpenses(
            @PathVariable User user,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "") String dateFrom,
            @RequestParam(required = false, defaultValue = "") String dateTo,
            Model model
    ) {
        List<Expense> expenses = expenseService.getExpenses( user, filter, dateFrom, dateTo);

        Double totalExpenses = expenseService.getTotalExpenses( user, filter, dateFrom, dateTo);

        Double averageExpenses = expenseService.getAverageExpenses(
                user,
                filter,
                dateFrom,
                dateTo,
                totalExpenses
        );

        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("averageExpenses", averageExpenses);

        return "expenses";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-expenses")
    public String expensesListOfAllUsers(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "") String dateFrom,
            @RequestParam(required = false, defaultValue = "") String dateTo,
            Model model
    ) {

        List<Expense> expenses = expenseService.getExpenses( null, filter, dateFrom, dateTo);

        Double totalExpenses = expenseService.getTotalExpenses( null, filter, dateFrom, dateTo);

        Double averageExpenses = expenseService.getAverageExpenses(
                null,
                filter,
                dateFrom,
                dateTo,
                totalExpenses
        );

        model.addAttribute("user", currentUser);
        model.addAttribute("expenses", expenses);
        model.addAttribute("filter", filter);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("averageExpenses", averageExpenses);

        return "expenses";

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/expenses/{expense}/delete")
    public String deleteExpense(@PathVariable Expense expense) {

        expenseRepository.delete(expense);
        return "redirect:/expenses";

    }
}
