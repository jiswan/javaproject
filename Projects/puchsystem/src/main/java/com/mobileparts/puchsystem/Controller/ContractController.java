package com.mobileparts.puchsystem.Controller;

import com.mobileparts.puchsystem.model.Employee;
import com.mobileparts.puchsystem.repository.EmployeeRepository;
import com.mobileparts.puchsystem.service.ContractExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contracts")
public class ContractController
{

    @Autowired
    public ContractExtensionService contractExtensionService;

    @Autowired
    public EmployeeRepository employeeRepository;

    @GetMapping("/expiring")
    public ResponseEntity<List<Employee>> getExpiringContracts()
    {
        List<Employee> expiringContracts = contractExtensionService.findContractsExpiringSoon();
        return ResponseEntity.ok(expiringContracts);
    }

    @PostMapping("/sendextensions")
    public ResponseEntity<Map<String,Object>> sendExtensionEmail()
    {
        contractExtensionService.sendContractExtensionEmails();
        long pendingCount = employeeRepository.countByExtensionRequested(true);

        Map<String,Object> response = new HashMap<>();
        response.put("success",true);
        response.put("message","Email sent successfully");
        response.put("pendingRequests",pendingCount);
        response.put("timestamp", LocalDate.now());

        return ResponseEntity.ok(response);

    }

    @GetMapping("/extension")
    public ResponseEntity<Map<String,String>> acceptExtension(@RequestParam String token)
    {
        String result = contractExtensionService.acceptContractExtension(token);
        // Build response
        Map<String, String> response = new HashMap<>();

        if (result.contains("SUCCESS"))
        {
            response.put("status", "success");
            response.put("message", result);
            return ResponseEntity.ok(response);
        }
        else {
            response.put("status", "error");
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/permanet")
    public ResponseEntity<Map<String,String>> acceptPermanent(@RequestParam String token)
    {
        String result = contractExtensionService.acceptPermanentPosition(token);
        Map<String,String> response = new HashMap<>();
        if (result.contains("CONGRATULATIONS")) {
            response.put("status", "success");
            response.put("message", result);
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", result);
            return ResponseEntity.badRequest().body(response);
        }
    }


    @GetMapping("/statistics")
    public ResponseEntity<Map<String,Object>> getStatistics()
    {
        long totalEmployee = employeeRepository.count();
        long permanentCount = employeeRepository.countByEmployeeType("Permanent");
        long contractCount = employeeRepository.countByEmployeeType("Contract");
        long pendingRequest = employeeRepository.countByExtensionRequested(true);

        List<Employee> expiringContracts = contractExtensionService.findContractsExpiringSoon();

        Map<String,Object> response = new HashMap<>();
        response.put("totalEmployee",totalEmployee);
        response.put("permanentCount",permanentCount);
        response.put("contractCount",contractCount);
        response.put("Pending Request",pendingRequest);
        response.put("Expiring Employee Count",expiringContracts.size());
        response.put("timestamp",LocalDate.now());
        return ResponseEntity.ok(response);

    }

}



