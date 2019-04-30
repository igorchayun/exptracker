package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.domain.ExpenseForm;
import testtask.exptracker.repos.ExpenseRepo;

import javax.validation.Valid;
import java.util.Map;

@Controller
//@RequestMapping("/expenses")
public class ExpensesController implements WebMvcConfigurer {

    @Autowired
    private ExpenseRepo expenseRepo;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @GetMapping("/")
    public String showForm(ExpenseForm expenseForm) {
        return "form";
    }

    @PostMapping("/")
    public String saveExpense(@Valid ExpenseForm expenseForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "form";
        }

        Expense expense = expenseForm.convertToExpense();
        expenseRepo.save(expense);
        return "redirect:/results";
    }

    @GetMapping("/list")
    public String index(Map<String, Object> model) {
        Iterable<Expense> expenses = expenseRepo.findAll();
        model.put("expenses", expenses);
        return "index";
    }

}
