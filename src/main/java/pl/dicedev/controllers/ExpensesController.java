package pl.dicedev.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import pl.dicedev.services.ExpensesService;
import pl.dicedev.services.dtos.ExpensesDto;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/expenses")
public class ExpensesController {
    private final ExpensesService expensesService;

    public ExpensesController(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @GetMapping
    public List<ExpensesDto> getAllExpenses() {
        return expensesService.getAllExpenses();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void saveExpenses(@RequestBody ExpensesDto expensesDto) {
        expensesService.saveExpenses(expensesDto);
    }

    @PutMapping(value = "/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExpensesDto updateExpenses(@RequestBody ExpensesDto expensesDto,@PathVariable UUID id) {
       return expensesService.updateExpenses(expensesDto,id);
    }

    @DeleteMapping
    public void deleteExpenses(@RequestBody UUID uuid) {
        expensesService.deleteExpenses(uuid);
    }

    @GetMapping("/filter")
    public List<ExpensesDto> filterExpenses(@RequestParam Map<String, String> filters) {
        return expensesService.getFilteredExpenses(filters);
    }
    
}
