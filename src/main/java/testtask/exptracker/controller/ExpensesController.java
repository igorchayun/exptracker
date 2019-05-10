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
import testtask.exptracker.service.ExpenseService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ExpensesController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

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
            @AuthenticationPrincipal User currentUser,
            @Valid Expense expense,
            BindingResult bindingResult
    ){
        if (bindingResult.hasErrors()) {
            return "expenseAdd";
        }
        expenseService.addNewExpense(currentUser, expense);
        return "redirect:/expenses";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/expenses/{id}")
    public String showEditExpenseForm(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("expense",expenseService.getOneExpense(currentUser, id));
        return "expenseEdit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/expenses/{id}")
    public String editExpense(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("id") Expense expenseFromDb,
            @Valid Expense expense,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "expenseEdit";
        }
        expenseService.editExpense(currentUser, expenseFromDb, expense);
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
    public String deleteExpense(@AuthenticationPrincipal User currentUser, @PathVariable Expense expense) {
        expenseService.deleteExpense(currentUser, expense);
        return "redirect:/expenses";
    }
}
