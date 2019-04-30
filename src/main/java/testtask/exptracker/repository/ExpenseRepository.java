package testtask.exptracker.repository;
import org.springframework.data.repository.CrudRepository;
import testtask.exptracker.domain.Expense;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
}
