package net.javaguides.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Vendor;
import net.javaguides.springboot.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class VendorController {

  @Autowired
  private VendorRepository vendorRepository;

  @GetMapping("/vendors")
  public List<Vendor> getAllVendors() {
    return vendorRepository.findAll();
  }

  @PostMapping("/vendors")
  public Vendor createVendor(@RequestBody Vendor vendor) {
    return vendorRepository.save(vendor);
  }

  @GetMapping("/vendors/{id}")
  public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
    Vendor vendor = vendorRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Vendor not exist with id :" + id)
      );
    return ResponseEntity.ok(vendor);
  }

  @PutMapping("/vendors/{id}")
  public ResponseEntity<Vendor> updateVendor(
    @PathVariable Long id,
    @RequestBody Vendor vendorDetails
  ) {
    Vendor vendor = vendorRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Vendor not exist with id :" + id)
      );

    vendor.setVendorName(vendorDetails.getVendorName());

    Vendor updatedVendor = vendorRepository.save(vendor);
    return ResponseEntity.ok(updatedVendor);
  }

  @DeleteMapping("/vendors/{id}")
  public ResponseEntity<Map<String, Boolean>> deleteVendor(
    @PathVariable Long id
  ) {
    Vendor vendor = vendorRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("Vendor not exist with id :" + id)
      );

    vendorRepository.delete(vendor);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return ResponseEntity.ok(response);
  }
}
