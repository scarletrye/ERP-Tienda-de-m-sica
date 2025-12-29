package grupoB.erp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import grupoB.erp.domain.Invoice;
import grupoB.erp.domain.Order;
import grupoB.erp.domain.Product;
import grupoB.erp.domain.Task;
import grupoB.erp.domain.User;
import grupoB.erp.service.InvoiceService;
import grupoB.erp.service.OrderService;
import grupoB.erp.service.ProductService;
import grupoB.erp.service.TaskService;
import grupoB.erp.domain.Warehouse;
import grupoB.erp.dto.ItemDTO;
import grupoB.erp.dto.OrderDTO;
import grupoB.erp.dto.TaskDTO;
import grupoB.erp.dto.UserDTO;
import grupoB.erp.service.UserService;
import grupoB.erp.service.WarehouseService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/user/{id}/update")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @ModelAttribute UserDTO data) {
        if (data == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        User user = userService.getById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        user.setUsername(data.getUsername());
        user.setEmail(data.getEmail());
        user.setPhone(data.getPhone());
        user.setAddress(data.getAddress());
        if (data.getNewPassword() != null) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            user.setPassword(bcrypt.encode(data.getNewPassword()));
        }
        try {
            userService.save(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not update the entity");
        }
        return ResponseEntity.ok("Updated successfully");
    }

    @DeleteMapping("/user/{id}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resorce not found");
        try {
            userService.delete(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not delete the entity");
        }
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/product/add")
    public ResponseEntity<String> addProduct(@ModelAttribute Product data) {
        if (data == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        Product existingProduct = productService.getByRef(data.getRef());
        if (existingProduct != null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Entity already exists");
        try {
            productService.save(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not add the entity");
        }
        return ResponseEntity.ok("Added successfully");
    }

    @PostMapping("/product/{id}/update")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @ModelAttribute Product data) {
        if (data == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        Product product = productService.getById(id);
        if (product == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        product.setRef(data.getRef());
        product.setName(data.getName());
        product.setDescription(data.getDescription());
        product.setCost(data.getCost());
        product.setPrice(data.getPrice());
        product.setMinStock(data.getMinStock());
        product.setMaxStock(data.getMaxStock());
        product.setForSale(data.isForSale());
        try {
            productService.save(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not update the entity");
        }
        return ResponseEntity.ok("Updated successfully");
    }

    @DeleteMapping("/product/{id}/delete")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        try {
            productService.delete(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not delete the entity");
        }
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/warehouse/add")
    public ResponseEntity<String> addWarehouse(@ModelAttribute Warehouse data) {
        if (data == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        Warehouse existingWarehouse = warehouseService.getByRef(data.getRef());
        if (existingWarehouse != null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Entity already exists");
        try {
            warehouseService.save(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not add the entity");
        }
        return ResponseEntity.ok("Added successfully");
    }

    @PostMapping("/warehouse/{id}/update")
    public ResponseEntity<String> updateWarehouse(@PathVariable Long id, @ModelAttribute Warehouse data) {
        if (data == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        Warehouse warehouse = warehouseService.getById(id);
        if (warehouse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        warehouse.setRef(data.getRef());
        warehouse.setAddress(data.getAddress());
        warehouse.setPhone(data.getPhone());
        warehouse.setOpen(data.isOpen());
        try {
            warehouseService.save(warehouse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not update the entity");
        }
        return ResponseEntity.ok("Updated successfully");
    }

    @DeleteMapping("/warehouse/{id}/delete")
    public ResponseEntity<String> deleteWarehouse(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.getById(id);
        if (warehouse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        try {
            warehouseService.delete(warehouse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not delete the entity");
        }
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/order/add")
    public ResponseEntity<String> addOrder(@ModelAttribute OrderDTO orderDTO) {
        if (orderDTO == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");
        Order existingOrder = orderService.getByRef(orderDTO.getRef());
        if (existingOrder != null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Entity already exists");
        Warehouse warehouse = warehouseService.getByRef(orderDTO.getWarehouse());
        if (warehouse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Warehouse not found");
        User user = userService.getById(orderDTO.getUser());
        if (user == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: User not found");
        Order order = new Order();
        order.setRef(orderDTO.getRef());
        order.setType(orderDTO.getType());
        order.setWarehouse(warehouse);
        order.setUser(user);
        Invoice invoice = new Invoice();
        long amount = 0;
        if (orderDTO.getItems() != null) {
            for (ItemDTO item : orderDTO.getItems()) {
                Product product = productService.getByRef(item.getProduct());
                if (product == null)
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Not Found: Product with reference " + item.getProduct() + " not found");
                amount += product.getPrice() * item.getAmount();
            }
        }
        invoice.setRef(order.getRef());
        invoice.setOrder(order);
        invoice.setAmount(amount);
        if (amount == 0) {
            invoice.setTax(0);
            invoice.setTotal(0);
        } else {
            invoice.setTax(21.0);
            invoice.setTotal(invoice.getAmount() + (invoice.getTax() / invoice.getAmount() * 100));
        }
        try {
            orderService.save(order);
            invoiceService.save(invoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not add the entity");
        }
        return ResponseEntity.ok("Added successfully");
    }

    @PostMapping("/task/add")
    public ResponseEntity<String> addTask(@ModelAttribute TaskDTO task) {
        if(task == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request: No data was given");    

        try {
            Task newTask = new Task( );
            newTask.setName(task.getName());
            newTask.setDescription(task.getDescription());

            String dateTimeString = task.getDate() + "T" + task.getTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
            newTask.setDate(localDateTime);
            Task.TaskPriority priority = Task.TaskPriority.valueOf(task.getPriority());
            newTask.setPriority(priority);

            taskService.saveTask(newTask);
        } catch (Exception e) {
            log.error("Could not add the entity", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Could not add the entity");
        }
        
        return ResponseEntity.ok("Added successfully");
    }

    @DeleteMapping("/task/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        Task task = taskService.findTaskById(id);
        if (task == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: Resource not found");
        try {
            taskService.delete(task);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error: Could not delete the entity");
        }
        return ResponseEntity.ok("Deleted successfully");
    }
}
