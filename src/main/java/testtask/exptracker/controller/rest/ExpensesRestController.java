package testtask.exptracker.controller.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.service.ExpenseService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
@Api(value = "/api/v1/expenses", description = "Operations with expenses")
public class ExpensesRestController {
    private final ExpenseService expenseService;
    @Autowired
    public ExpensesRestController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @ApiOperation(value = "Displays a list of expenses authorized user")
    @GetMapping
    public List<Expense> list(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "") String dateFrom,
            @RequestParam(required = false, defaultValue = "") String dateTo
    ) {
        return expenseService.getExpenses(currentUser, filter, dateFrom, dateTo);
    }

    @ApiOperation(value = "Display an expense by id")
    @GetMapping("{id}")
    public Expense getOne(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        return expenseService.getOneExpense(currentUser, id);
    }

    @ApiOperation(value = "Create new expense")
    @PostMapping
    public Expense create(@AuthenticationPrincipal User currentUser, @RequestBody Expense expense) {
        return expenseService.addNewExpense(currentUser, expense);
    }

    @ApiOperation(value = "Edit expense by id")
    @PutMapping("{id}")
    public Expense update(
            @AuthenticationPrincipal User currentUser,
            @PathVariable("id") Expense expenseFromDb,
            @RequestBody Expense expense
    ) {
        return expenseService.editExpense(currentUser, expenseFromDb, expense);
    }

    @ApiOperation(value = "Delete expense by id")
    @DeleteMapping("{id}")
    public void delete(@AuthenticationPrincipal User currentUser, @PathVariable("id") Expense expense) {
        expenseService.deleteExpense(currentUser, expense);
    }

    @ApiOperation(value = "Displays a list of all expenses all users. " +
            "Available only for the user authorized with the admin role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public List<Expense> listAll(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "") String dateFrom,
            @RequestParam(required = false, defaultValue = "") String dateTo
    ) {
        return expenseService.getExpenses(null, filter, dateFrom, dateTo);
    }

}
