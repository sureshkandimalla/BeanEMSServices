package com.bean.deserializer;

import com.bean.model.Employee;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EmployeeDeserializer extends StdDeserializer<Employee> {

    public EmployeeDeserializer() {
        super(Employee.class);
    }

    @Override
    public Employee deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        // Accept plain number: "employee": 1  or  "employeeId": 1
        if (p.currentToken() == JsonToken.VALUE_NUMBER_INT) {
            long id = p.getLongValue();
            if (id == 0) return null;
            Employee emp = new Employee();
            emp.setEmployeeId(id);
            return emp;
        }
        if (p.currentToken() == JsonToken.VALUE_NULL) {
            return null;
        }
        // Accept full object: "employee": {"employeeId": 1, ...}
        if (p.currentToken() == JsonToken.START_OBJECT) {
            return p.readValueAs(Employee.class);
        }
        return null;
    }
}
