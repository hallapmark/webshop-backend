package ee.markh.webshopbackend.controller;

import ee.markh.webshopbackend.entity.Employee;
import ee.markh.webshopbackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // annab võimekuse API päringuid siia faili teha
//@CrossOrigin(origins = "http://localhost:5173") // pole vaja enam, securityConfigis yhe korra
public class EmployeeController {
    // base URL - localhost:8080
    // API endpoint - employees

    @Autowired
    private EmployeeRepository employeeRepository;

    // localhost:8080/employees --> käivitan selle funktsiooni
    @GetMapping("employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    // tegusõna tavaliselt ei panda siia endpoint nime sisse. annotation on tegusõna
    @PostMapping("employees")
    public List<Employee> addEmployee(@RequestBody Employee employee) {
        if (employee.getId() != null) {
            throw new RuntimeException("Cannot add employee with id");
        }
        employeeRepository.save(employee);
        return employeeRepository.findAll();
    }

    // req superadmin
    @PostMapping("many-employees")
    public List<Employee> addManyEmployees(@RequestBody List<Employee> employees) {
        for (Employee employee : employees) {
            if (employee.getId() != null) {
                throw new RuntimeException("Cannot add employee with id");
            }
        }
        employeeRepository.saveAll(employees);
        return employeeRepository.findAll();
    }

    // requestparamiga
    // localhost:8080/employees?id=
    @DeleteMapping("employees")
    public List<Employee> deleteEmployee(@RequestParam String id) {
        employeeRepository.deleteById(id);
        return employeeRepository.findAll();
    }

    // localhost:8080/employees/uuid-uuid
    @GetMapping("employees/{id}")
    public Employee getEmployee(@PathVariable String id) {
        return employeeRepository.findById(id).orElse(null);
    }
    // RequestParam ja PathVariable --> mõlemaid võib üldjuhul kasutada
    // siiski kindlasti Requestparami vaja järgnevatel juhtudel:
    // 1. kui on valikuline (nullable) (RequestParam(required = false))
    // 2. kui on mitu parameetrit. Pathvariable puhul läheks segaseks mis mille jaoks on. eelistatud requestparam siis

    @PutMapping("employees")
    public List<Employee> editEmployee(@RequestBody Employee employee) {
        if (employee.getId() == null) {
            throw new RuntimeException("Cannot edit employee without id");
        }
        employeeRepository.save(employee);
        return employeeRepository.findAll();
    }

}
