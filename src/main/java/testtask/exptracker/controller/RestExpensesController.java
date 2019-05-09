package testtask.exptracker.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.repository.ExpenseRepository;
import testtask.exptracker.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class RestExpensesController {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<Expense> list() {
        return expenseService.getExpenses(null, null, null, null);
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
