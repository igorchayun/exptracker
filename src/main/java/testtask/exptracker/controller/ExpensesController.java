package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.ExpenseForm;
import testtask.exptracker.repository.ExpenseRepository;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpensesController implements WebMvcConfigurer {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("expenses/results");
    }

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

        return "expenses/results";
    }

    @GetMapping
    public String index(Map<String, Object> model) {
        Iterable<Expense> expenses = expenseRepository.findAll();
        model.put("expenses", expenses);
        return "expenses/index";
    }

}
