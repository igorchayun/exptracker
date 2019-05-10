package testtask.exptracker.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.User;
import testtask.exptracker.repository.ExpenseRepository;
import testtask.exptracker.service.ExpenseService;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class ExpensesRestController {

    private final ExpenseRepository expenseRepository;

    private final ExpenseService expenseService;

    @Autowired
    public ExpensesRestController(ExpenseRepository expenseRepository, ExpenseService expenseService) {
        this.expenseRepository = expenseRepository;
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> list(@AuthenticationPrincipal User currentUser,
                              @RequestParam(required = false, defaultValue = "") String filter,
                              @RequestParam(required = false, defaultValue = "") String dateFrom,
                              @RequestParam(required = false, defaultValue = "") String dateTo
    ) {
        return expenseService.getExpenses(currentUser, filter, dateFrom, dateTo);
    }

    @GetMapping("{id}")
    public Expense getOne(@PathVariable("id") Expense expense) {
        return expense;
    }

    @PostMapping
    public Expense create(@RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @PutMapping("{id}")
    public Expense update(@PathVariable("id") Expense expenseFromDb, @RequestBody Expense expense) {
        BeanUtils.copyProperties(expense, expenseFromDb, "id");
        return expenseRepository.save(expenseFromDb);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Expense expense) {
        expenseRepository.delete(expense);
    }
}
