package testtask.exptracker.repository;
import org.springframework.data.repository.CrudRepository;
import testtask.exptracker.domain.Expense;
import java.util.List;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    List<Expense> findByTextOrComment(String text, String comment);
}
