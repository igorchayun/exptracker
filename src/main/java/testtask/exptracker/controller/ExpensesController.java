package testtask.exptracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import testtask.exptracker.domain.Expense;
import testtask.exptracker.repos.ExpenseRepo;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    @Autowired
    private ExpenseRepo expenseRepo;
    @GetMapping("/new")
    public String expenseForm(
    //        @RequestParam(name="name", required=false, defaultValue="World") String name,
    //        Map<String, Object> model
    ) {
    //    model.put("name", name);
        return "expenses/expenseForm";
    }
    @GetMapping
    public String index(Map<String, Object> model) {
        Iterable<Expense> expenses = expenseRepo.findAll();
        model.put("expenses", expenses);
        return "expenses/index";
    }

    @PostMapping("/new")
    public String saveExpense(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
                              @RequestParam String text, @RequestParam Double cost,
                              @RequestParam @Size(min=2, max=10) String comment){
        //далее нужна обработка ошибок
        //LoggerFactory.getLogger(this.getClass()).info("new message");
        Expense expense = new Expense(date, time, text, cost, comment);
        expenseRepo.save(expense);
        return "expenses/expenseForm";
    }
}
