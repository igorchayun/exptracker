package testtask.exptracker.repos;
import org.springframework.data.repository.CrudRepository;
import testtask.exptracker.domain.Expense;

public interface ExpenseRepo extends CrudRepository<Expense, Long> {
}
