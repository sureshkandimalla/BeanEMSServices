package com.bean.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.model.Vendor;
import com.bean.repository.VendorRepository;

@Service
public class VendorService {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VendorService.class);

	@Autowired
    private VendorRepository vendorRepository;

	public Optional<Vendor> saveVendor(com.bean.domain.Vendor vendor) {

		Vendor v = new Vendor();

		v.setVendorCompanyName(vendor.vendorCompanyName());
		v.setVendorEmail(vendor.emailId());
		v.setVendorName(vendor.vendorName());
		v.setVendorPhone(vendor.phone());
		v.setVendorStartDate(vendor.startDate());
		v.setVendorEndDate(vendor.endDate());
		v.setVendorStatus("Active");
		v.setEin(vendor.ein());
		v.setWebsite(vendor.webSite());
		v.setLastUpdated(LocalDate.now());
		v.setVendorContactEmail(vendor.emailId());
		v.setVendorAddress(formatAddress(vendor));
		return Optional.of(vendorRepository.save(v));
	}

	// Builds a single "street, street2, city, state zip" display string from
	// the onboarding form's separate address fields — vendorAddress on the
	// Vendor entity is just one String column, not a structured address.
	// Blank/missing components (e.g. no street2) are simply omitted rather
	// than leaving a stray ", ".
	private String formatAddress(com.bean.domain.Vendor vendor) {
		StringBuilder sb = new StringBuilder();
		appendPart(sb, vendor.streetAddress());
		appendPart(sb, vendor.streetAddress2());
		appendPart(sb, vendor.city());
		String stateZip = joinWithSpace(vendor.state(), vendor.zipCode());
		appendPart(sb, stateZip);
		return sb.length() > 0 ? sb.toString() : "";
	}

	private void appendPart(StringBuilder sb, String part) {
		if (part == null || part.isBlank()) return;
		if (sb.length() > 0) sb.append(", ");
		sb.append(part.trim());
	}

	private String joinWithSpace(String a, String b) {
		boolean hasA = a != null && !a.isBlank();
		boolean hasB = b != null && !b.isBlank();
		if (hasA && hasB) return a.trim() + " " + b.trim();
		if (hasA) return a.trim();
		if (hasB) return b.trim();
		return null;
	}

}
